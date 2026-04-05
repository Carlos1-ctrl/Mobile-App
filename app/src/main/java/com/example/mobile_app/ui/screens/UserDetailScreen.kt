package com.example.mobile_app.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mobile_app.data.model.User
import com.example.mobile_app.ui.components.CameraCapture
import com.example.mobile_app.ui.theme.AccentBlue
import com.example.mobile_app.ui.theme.AccentGlow
import com.example.mobile_app.ui.theme.AccentPurple
import com.example.mobile_app.ui.theme.DarkBackground
import com.example.mobile_app.ui.theme.DarkCard
import com.example.mobile_app.ui.theme.DarkSurface
import com.example.mobile_app.ui.theme.ErrorRed
import com.example.mobile_app.ui.theme.TextPrimary
import com.example.mobile_app.ui.theme.TextSecondary
import com.example.mobile_app.utils.calculateDistanceKm
import com.example.mobile_app.utils.createPdf
import com.example.mobile_app.utils.hasPermission
import com.example.mobile_app.utils.saveContact
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


/**
 * Pantalla que muestra el detalle del usuario
 * Muestra innformacion completa del usuario y permite:
 * -Compartir contacto en whatsapp
 * -Guardar contacto en contactos
 * -Exportar Informacion en PDF
 * -Ver ubicacion en Google Maps
 * -Tomar fotografia persistente
 * -Validar Credenciales del Usuario con un formulario
 * @param user Usuario a mostrar
 * @param nav Controlador de navegacion para volver a la lista
 */
@SuppressLint("MissingPermission")
@Composable
fun UserDetailScreen(user: User, nav: NavController) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope=rememberCoroutineScope()

    val context = LocalContext.current
    var distanceText by remember { mutableStateOf("Calculando distancia...") }

    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginResult by remember { mutableStateOf<String?>(null) }


    var hasLocationPermission by remember {
        mutableStateOf(hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (!granted) distanceText = "Permiso de ubicación denegado"
    }


    var hasContactPermission by remember {
        mutableStateOf(hasPermission(context, Manifest.permission.WRITE_CONTACTS))
    }
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasContactPermission = granted
        if (granted) saveContact(context, user)
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            if (hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                fusedClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLat = user.location.coordinates.latitude.toDoubleOrNull() ?: 0.0
                        val userLon = user.location.coordinates.longitude.toDoubleOrNull() ?: 0.0
                        val dist = calculateDistanceKm(it.latitude, it.longitude, userLat, userLon)
                        distanceText = "Distancia: %.2f km".format(dist)
                    } ?: run {
                        distanceText = "No se pudo obtener ubicación"
                    }
                }
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
Scaffold(
    snackbarHost = {SnackbarHost(snackBarHostState)}
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(AccentPurple, DarkBackground)))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = user.picture.large,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, AccentGlow, CircleShape)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "${user.name.first} ${user.name.last}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow("📧", user.email)
                InfoRow("📞", user.phone)
                InfoRow("📍", "${user.location.street.name} ${user.location.street.number}, ${user.location.city}, ${user.location.country}")
                InfoRow("📏", distanceText)
                InfoRow("👤", "Username: ${user.login.username}")
                InfoRow("🔑", "Password: ${user.login.password}")
            }
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            ActionButton("💬 Compartir por WhatsApp", AccentBlue) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${user.name.first} ${user.name.last} | ${user.phone} | ${user.email}")
                    setPackage("com.whatsapp")
                }
                context.startActivity(intent)
            }
            Spacer(Modifier.height(8.dp))

            ActionButton("👤 Guardar Contacto", AccentPurple) {

                if (hasContactPermission){
                    saveContact(context, user)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "✅ Contacto guardado: ${user.name.first} ${user.name.last}",
                        duration = SnackbarDuration.Short
                    )
                }
            }
                else contactPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)
            }

            Spacer(Modifier.height(8.dp))
            ActionButton("📄 Guardar PDF", AccentGlow) { createPdf(context, user) }
            Spacer(Modifier.height(8.dp))
            ActionButton("🗺️ Ver en Maps", Color(0xFF34D399)) {
                val lat = user.location.coordinates.latitude
                val lng = user.location.coordinates.longitude
                val label = "${user.name.first} ${user.name.last}"
                val uri = Uri.parse("geo:0,0?q=$lat,$lng($label)")
                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    val fallbackUri = Uri.parse("https://maps.google.com/?q=$lat,$lng")
                    context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
                }
            }
            Spacer(Modifier.height(16.dp))
            CameraCapture(userEmail = user.email)
            Spacer(Modifier.height(8.dp))

            Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "🔐 Credenciales del usuario",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp
                )
                Text(
                    "Ingresa las credenciales de ${user.name.first}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    label = { Text("Correo Electronico") },
                    isError = usernameError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = TextSecondary,
                        focusedLabelColor = AccentPurple,
                        cursorColor = AccentPurple,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                usernameError?.let {
                    Text(it, color = ErrorRed, fontSize = 12.sp)
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = TextSecondary,
                        focusedLabelColor = AccentPurple,
                        cursorColor = AccentPurple,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                passwordError?.let {
                    Text(it, color = ErrorRed, fontSize = 12.sp)
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        usernameError = when {
                            usernameInput.isBlank() -> "El correo no puede estar vacío"
                            !usernameInput.contains("@") -> "Correo inválido"
                            !usernameInput.contains(".") -> "Correo inválido"
                            else -> null
                        }
                        passwordError = if (passwordInput.isBlank()) "La contraseña no puede estar vacía" else null

                        if (usernameError == null && passwordError == null) {
                            loginResult = if (
                                usernameInput == user.email &&
                                passwordInput == user.login.password
                            ) "✅ Credenciales correctas" else "❌ Credenciales incorrectas"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                ) {
                    Text("Validar", color = Color.White, fontWeight = FontWeight.Bold)
                }

                loginResult?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it,
                        color = if (it.contains("✅")) Color(0xFF34D399) else ErrorRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

            Spacer(Modifier.height(16.dp))


            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(AccentPurple, AccentBlue))
                )
            ) {
                Text("← Volver", color = TextPrimary)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
}

/**
 * filtra la informacion con icono y texto.
 * Usada dentro de la card de detalle usuario
 * @param icon Emoji o icono como String
 * @param text Texto a mostrar
 */
@Composable
fun InfoRow(icon: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Text(text, color = TextSecondary, fontSize = 13.sp)
    }
    HorizontalDivider(color = DarkSurface, thickness = 0.5.dp)
}

/**
 * Boton de accion reutilizable con color personalizado.
 * @param text Texto del boton
 * @param color Color principal del boton
 * @param onClick Callback al hacer click
 */
@Composable
fun ActionButton(text: String, color: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Text(text, color = color, fontWeight = FontWeight.Medium)
    }
}
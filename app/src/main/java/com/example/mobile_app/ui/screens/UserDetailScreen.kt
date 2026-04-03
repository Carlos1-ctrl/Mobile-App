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
import com.example.mobile_app.ui.theme.TextPrimary
import com.example.mobile_app.ui.theme.TextSecondary
import com.example.mobile_app.utils.calculateDistanceKm
import com.example.mobile_app.utils.createPdf
import com.example.mobile_app.utils.hasPermission
import com.example.mobile_app.utils.saveContact
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun UserDetailScreen(user: User, nav: NavController) {
    val context = LocalContext.current
    var distanceText by remember { mutableStateOf("Calculando distancia...") }


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
                if (hasContactPermission) saveContact(context, user)
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
            CameraCapture()
            Spacer(Modifier.height(8.dp))
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
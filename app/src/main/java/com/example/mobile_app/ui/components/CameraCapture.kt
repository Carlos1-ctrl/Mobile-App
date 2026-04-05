package com.example.mobile_app.ui.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.mobile_app.data.datastore.PhotoManager
import com.example.mobile_app.utils.hasPermission
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

/**
 * Composable que permite tomar una foto y mostrarla.
 * La foto se guarda de forma persistente asociada al email del usuario.
 * Cada foto nueva usa un archivo con timestamp para evitar caché de Coil.
 *
 * @param userEmail Email del usuario para asociar la foto
 */
@Composable
fun CameraCapture(userEmail: String) {
    val context = LocalContext.current
    val photoManager = remember { PhotoManager(context) }
    val scope = rememberCoroutineScope()

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var initialized by remember { mutableStateOf(false) }
    val safeEmail = remember { userEmail.replace(Regex("[^a-zA-Z0-9]"), "_") }

    var hasCameraPermission by remember {
        mutableStateOf(hasPermission(context, Manifest.permission.CAMERA))
    }

    var currentPhotoFile by remember {
        mutableStateOf(File(context.filesDir, "photo_${safeEmail}_${System.currentTimeMillis()}.jpg"))
    }
    var currentUri by remember {
        mutableStateOf(
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                currentPhotoFile
            )
        )
    }


    LaunchedEffect(userEmail) {
        val savedUri = photoManager.getPhoto(userEmail).first()
        if (!savedUri.isNullOrEmpty()) {
            photoUri = savedUri.toUri()
        }
        initialized = true
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = currentUri
            scope.launch {
                photoManager.savePhoto(userEmail, currentUri.toString())
            }
        }
    }

    if (!initialized) return

    Column(modifier = Modifier.fillMaxWidth()) {

        if (photoUri == null) {
            Button(
                onClick = {
                    currentPhotoFile = File(
                        context.filesDir,
                        "photo_${safeEmail}_${System.currentTimeMillis()}.jpg"
                    )
                    currentUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        currentPhotoFile
                    )
                    if (hasCameraPermission) {
                        cameraLauncher.launch(currentUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B46C1).copy(alpha = 0.15f)
                )
            ) {
                Text(
                    "📷 Tomar Foto",
                    color = Color(0xFF818CF8),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        photoUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = it,
                contentDescription = "Foto del usuario",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, Color(0xFF818CF8), RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {

                    currentPhotoFile.delete()

                    photoUri = null

                    scope.launch {
                        photoManager.savePhoto(userEmail, "")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC8181).copy(alpha = 0.15f)
                )
            ) {
                Text(
                    "🗑️ Eliminar Foto",
                    color = Color(0xFFFC8181),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
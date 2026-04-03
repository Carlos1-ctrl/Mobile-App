package com.example.mobile_app.ui.components

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage

import com.example.mobile_app.utils.hasPermission
import java.io.File

@Composable
fun CameraCapture(){
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var hasCameraPermission by remember {
        mutableStateOf(hasPermission(context, Manifest.permission.CAMERA))
    }
    val  permissionLauncher= rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }
    val photoFile = remember {
        File(context.filesDir, "photo_${System.currentTimeMillis()}.jgp")
    }
    val uri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }
    val cameraLauncher =  rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if(success) photoUri = uri
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Button(onClick = {
            if (hasCameraPermission){
                cameraLauncher.launch(uri)
            }
            else{
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Tomar Foto")
        }
        photoUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model= it,
                contentDescription = "Foto Tomada",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
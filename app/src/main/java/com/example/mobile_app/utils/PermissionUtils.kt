package com.example.mobile_app.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

fun hasPermission(context: Context, permission: String ): Boolean{
    return ContextCompat.checkSelfPermission(context, permission)==
            PackageManager.PERMISSION_GRANTED
}

@Composable
fun RequestPermission(
    permission: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
){
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { garanted ->
        if (garanted) onGranted() else onDenied()
    }
    LaunchedEffect(Unit) {
        launcher.launch(permission)
    }
}

package com.example.mobile_app.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

/**
 * Verifica si la app tiene un permiso específico concedido.
 * @param context Contexto de la aplicación
 * @param permission Permiso a verificar (e.g. Manifest.permission.CAMERA)
 * @return true si el permiso está concedido, false en caso contrario
 */
fun hasPermission(context: Context, permission: String ): Boolean{
    return ContextCompat.checkSelfPermission(context, permission)==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Composable que solicita un permiso al usuario al ser lanzado.
 *
 * @param permission Permiso a solicitar
 * @param onGranted Callback ejecutado si el usuario concede el permiso
 * @param onDenied Callback ejecutado si el usuario deniega el permiso
 */

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

package com.example.mobile_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
@Composable
fun ErrorBanner(message: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(12.dp)
    ){
        Text(text = message, color = Color.White)
    }

}
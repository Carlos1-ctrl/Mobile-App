package com.example.mobile_app.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary        = AccentPurple,
    onPrimary      = Color.White,
    secondary      = AccentBlue,
    onSecondary    = Color.White,
    tertiary       = AccentGlow,
    background     = DarkBackground,
    onBackground   = TextPrimary,
    surface        = DarkSurface,
    onSurface      = TextPrimary,
    surfaceVariant = DarkCard,
    error          = ErrorRed,
    onError        = Color.White
)

@Composable
fun MobileAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = Typography,
        content     = content
    )
}
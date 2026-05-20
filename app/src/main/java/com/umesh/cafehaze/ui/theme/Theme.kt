package com.umesh.cafehaze.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFFF7F6E5),
    onPrimary = Color.White,

    background = Color(0xFFF5E8C7), // 🌿 your color
    onBackground = Color(0xFF3E2723),

    surface = Color.White,
    onSurface = Color(0xFF3E2723),

    surfaceVariant = Color(0xFFE8F0D5), // soft green tint
    onSurfaceVariant = Color(0xFF5A6B2C)
)

@Composable
fun CafeHazeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
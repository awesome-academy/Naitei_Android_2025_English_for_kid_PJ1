package com.example.englishappforkid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val LightColorScheme = lightColorScheme(
    primary = Color(color = 0xFF2196F3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFF5F5F5),
    onPrimary = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    onPrimary = Color.Black
)

@Composable
fun EnglishAppForKidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

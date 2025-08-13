package com.example.englishappforkid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColorScheme =
    lightColorScheme(
        primary = Color(color = 0xFF2196F3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFF5F5F5),
        onPrimary = Color.White,
    )

private val darkColorScheme =
    darkColorScheme(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212),
        onPrimary = Color.Black,
    )

@Composable
fun englishAppForKidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

package com.example.englishappforkid.presentation.screens.playvideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

class FullscreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FullscreenVideo()
        }
    }
}

@Composable
fun FullscreenVideo() {
    // Lấy trình phát đang hoạt động từ PlayerManager
    val exoPlayer = remember { PlayerManager.currentPlayer }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    )
}

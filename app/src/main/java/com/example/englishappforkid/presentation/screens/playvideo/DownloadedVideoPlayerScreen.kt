package com.example.englishappforkid.presentation.screens.downloads

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.englishappforkid.presentation.playvideo.DBHelper
import com.example.englishappforkid.presentation.playvideo.VideoPlayerViewModel
import com.example.englishappforkid.presentation.playvideo.suggestedVideoItem
import com.example.englishappforkid.presentation.screens.playvideo.formatTime
import kotlinx.coroutines.delay

@Composable
fun downloadedVideoPlayerScreen(
    videoId: String,
    navController: NavHostController,
    playerViewModel: VideoPlayerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    val videoItem = remember(videoId) { dbHelper.getVideoById(videoId) }
    val allDownloadedVideos = remember { dbHelper.getAllVideos() }

    var isFullscreen by remember { mutableStateOf(false) }

    if (videoItem == null) {
        Text("Không tìm thấy video.", modifier = Modifier.padding(16.dp))
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    playerViewModel.initializePlayer(context)
    val exoPlayer = playerViewModel.exoPlayer

    DisposableEffect(videoId) {
        playerViewModel.playVideo(videoItem.videoId)
        onDispose { exoPlayer.pause() }
    }

    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }

    DisposableEffect(exoPlayer) {
        val listener =
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                    isPlaying = isPlayingValue
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("VideoPlayer", "Player Error: ", error)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        duration = exoPlayer.duration
                    }
                }
            }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = exoPlayer.currentPosition
            delay(500)
        }
    }

    BackHandler(enabled = isFullscreen) {
        isFullscreen = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isFullscreen) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                AndroidView(
                    factory = { PlayerView(it).apply { useController = false } },
                    modifier = Modifier.fillMaxSize(),
                    update = { it.player = exoPlayer },
                )
                IconButton(
                    onClick = { isFullscreen = false },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                ) {
                    Icon(Icons.Default.FullscreenExit, contentDescription = "Thoát toàn màn hình", tint = Color.White)
                }
            }
        } else {
            val suggestedVideos =
                remember(videoId) {
                    allDownloadedVideos.filter { it.id != videoId }.shuffled().take(3)
                }
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White).padding(8.dp),
            ) {
                AndroidView(
                    factory = { PlayerView(it).apply { useController = false } },
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    update = { it.player = exoPlayer },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f,
                    onValueChange = { value ->
                        val newPosition = (value * duration).toLong()
                        exoPlayer.seekTo(newPosition)
                        currentPosition = newPosition
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(text = "${formatTime(currentPosition)} / ${formatTime(duration)}")
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { if (isPlaying) exoPlayer.pause() else exoPlayer.play() }) {
                        Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Phát/Tạm dừng")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        dbHelper.removeVideo(videoItem.id)
                        Toast.makeText(context, "Đã xóa video!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa")
                    }
                    IconButton(onClick = { isFullscreen = true }) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Toàn màn hình")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp)).padding(12.dp),
                ) {
                    Text(text = videoItem.description, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Next videos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    items(suggestedVideos, key = { it.id }) { suggestion ->
                        suggestedVideoItem(
                            videoItem = suggestion,
                            onClick = { navController.navigate("downloaded_player/${suggestion.id}") },
                        )
                    }
                }
            }
        }
    }
}

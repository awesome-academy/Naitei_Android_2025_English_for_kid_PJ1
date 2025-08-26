package com.example.englishappforkid.presentation.screens.downloads

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.englishappforkid.data.model.VideoItem
import com.example.englishappforkid.presentation.playvideo.DBHelper
import com.example.englishappforkid.presentation.playvideo.VideoPlayerViewModel
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun downloadedVideoPlayerScreen(
    videoId: String,
    navController: NavHostController,
    playerViewModel: VideoPlayerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    val videoItem by remember(videoId) { mutableStateOf(dbHelper.getVideoById(videoId)) }
    val allDownloadedVideos = remember { dbHelper.getAllVideos() }

    var isFullscreen by remember { mutableStateOf(false) }

    if (videoItem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy video đã tải.")
        }
        LaunchedEffect(Unit) {
            delay(1500)
            navController.popBackStack()
        }
        return
    }

    playerViewModel.initializePlayer()
    val exoPlayer = playerViewModel.exoPlayer

    DisposableEffect(videoItem) {
        videoItem?.let { playerViewModel.playVideo(it) }
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
                    Log.e("VideoPlayer", "Error: ", error)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        duration = exoPlayer.duration.coerceAtLeast(0)
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

    BackHandler(enabled = isFullscreen) { isFullscreen = false }

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
                    Icon(Icons.Default.FullscreenExit, "Exit full screen", tint = Color.White)
                }
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        windowInsets = WindowInsets(0),
                        title = { Text(videoItem!!.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại")
                            }
                        },
                        colors =
                            TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                                navigationIconContentColor = Color.Black,
                            ),
                    )
                },
            ) { innerPadding ->
                val suggestedVideos =
                    remember(videoId) {
                        allDownloadedVideos.filter { it.videoId != videoId }.shuffled().take(4)
                    }

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color.White)
                            .verticalScroll(rememberScrollState()),
                ) {
                    AndroidView(
                        factory = { PlayerView(it).apply { useController = false } },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(horizontal = 8.dp),
                        update = { it.player = exoPlayer },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF49B0AB))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { if (isPlaying) exoPlayer.pause() else exoPlayer.play() }) {
                            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Phát/Tạm dừng", tint = Color.White)
                        }
                        Slider(
                            value = if (duration > 0) currentPosition.toFloat() else 0f,
                            onValueChange = { exoPlayer.seekTo(it.toLong()) },
                            valueRange = 0f..(duration.toFloat().takeIf { it > 0 } ?: 0f),
                            modifier = Modifier.weight(1f),
                            colors =
                                SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = Color.White,
                                    inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f),
                                ),
                        )
                        IconButton(onClick = {
                            videoItem?.localPath?.let {
                                val file = File(it)
                                if (file.exists()) {
                                    file.delete()
                                }
                            }
                            dbHelper.removeVideoById(videoItem!!.videoId)
                            Toast.makeText(context, "deleted video!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.White)
                        }
                        IconButton(onClick = { isFullscreen = true }) {
                            Icon(Icons.Default.Fullscreen, "Toàn màn hình", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                    ) {
                        Text(text = videoItem!!.description, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Next videos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        suggestedVideos.chunked(2).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowItems.forEach { suggestion ->
                                    suggestedVideoItem(
                                        videoItem = suggestion,
                                        onClick = {
                                            navController.navigate("downloaded_player/${suggestion.videoId}")
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (rowItems.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun suggestedVideoItem(
    videoItem: VideoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column {
            AsyncImage(
                model = videoItem.thumbnailUrl,
                contentDescription = videoItem.title,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(90.dp),
            )
            Text(
                text = videoItem.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

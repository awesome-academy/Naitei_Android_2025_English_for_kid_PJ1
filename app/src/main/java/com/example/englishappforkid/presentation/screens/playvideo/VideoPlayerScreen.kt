package com.example.englishappforkid.presentation.playvideo

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
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
import com.example.englishappforkid.data.VideoDataSource
import com.example.englishappforkid.data.model.VideoItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun videoScreen(
    videoId: String,
    navController: NavHostController,
    playerViewModel: VideoPlayerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val videoItem = remember(videoId) { VideoDataSource.getVideoById(videoId) }
    val dbHelper = remember { DBHelper(context) }

    var isFullscreen by remember { mutableStateOf(false) }

    if (videoItem == null) {
        // Có thể hiển thị một màn hình lỗi ở đây
        Text("Video not found!")
        return
    }

    playerViewModel.initializePlayer(context)
    val exoPlayer = playerViewModel.exoPlayer

    DisposableEffect(videoId) {
        playerViewModel.playVideo(videoItem.videoId)
        onDispose {
            // Tạm dừng khi màn hình bị hủy
            exoPlayer.pause()
        }
    }

    // Các state và effect để theo dõi trạng thái của player
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

    // Xử lý nút back của hệ thống khi ở chế độ toàn màn hình
    BackHandler(enabled = isFullscreen) {
        isFullscreen = false
    }

    // Giao diện chính
    Box(modifier = Modifier.fillMaxSize()) {
        if (isFullscreen) {
            // CHẾ ĐỘ TOÀN MÀN HÌNH
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
                    Icon(Icons.Default.FullscreenExit, "Exit Fullscreen", tint = Color.White)
                }
            }
        } else {
            // CHẾ ĐỘ BÌNH THƯỜNG
            Scaffold(
                topBar = {
                    TopAppBar(
                        windowInsets = WindowInsets(0), // Bỏ khoảng trắng thừa ở status bar
                        title = {
                            Text(
                                text = videoItem.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
                // Lấy danh sách video gợi ý
                val suggestedVideos =
                    remember(videoId) {
                        VideoDataSource.videoStories
                            .filter { it.id != videoId }
                            .shuffled()
                            .take(4)
                    }

                // Sử dụng LazyColumn để toàn bộ màn hình có thể cuộn nếu nội dung quá dài
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding) // Padding bắt buộc của Scaffold
                            .background(Color.White),
                ) {
                    // Video player
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

                    // Khung điều khiển
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
                            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Play/Pause", tint = Color.White)
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
                            if (dbHelper.isVideoExists(videoItem.videoId)) {
                                Toast.makeText(context, "The story already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                dbHelper.addVideo(videoItem)
                                Toast.makeText(context, "Story saved!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Download, "Download", tint = Color.White)
                        }
                        IconButton(onClick = { isFullscreen = true }) {
                            Icon(Icons.Default.Fullscreen, "Fullscreen", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mô tả video
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                    ) {
                        Text(text = videoItem.description, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tiêu đề video gợi ý
                    Text(
                        text = "Next Videos",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    )

                    // Lưới video gợi ý
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(suggestedVideos, key = { it.id }) { suggestion ->
                            suggestedVideoItem(
                                videoItem = suggestion,
                                onClick = { navController.navigate("video_player/${suggestion.id}") },
                            )
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
        // Bỏ width và padding cố định để item vừa với lưới
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

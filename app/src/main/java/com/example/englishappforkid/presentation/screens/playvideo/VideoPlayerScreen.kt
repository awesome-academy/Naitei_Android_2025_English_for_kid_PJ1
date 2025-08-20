package com.example.englishappforkid.presentation.playvideo

import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.englishappforkid.data.VideoDataSource
import com.example.englishappforkid.data.model.VideoItem
import com.example.englishappforkid.presentation.screens.playvideo.FullscreenActivity
import com.example.englishappforkid.presentation.screens.playvideo.PlayerManager
import com.example.englishappforkid.presentation.screens.playvideo.formatTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.UUID

@Composable
fun videoScreen(
    videoId: String,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val videoItem = remember(videoId) { VideoDataSource.getVideoById(videoId) }
    val scope = rememberCoroutineScope()

    if (videoItem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Video not found!")
        }
        return
    }

    val suggestedVideos =
        remember(videoId) {
            VideoDataSource.videoStories
                .filter { it.id != videoId }
                .shuffled()
                .take(3)
        }

    val exoPlayer =
        remember(videoId) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(
                    androidx.media3.common.MediaItem
                        .fromUri(videoItem.videoId),
                )
                prepare()
                playWhenReady = true
            }
        }

    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }

    DisposableEffect(exoPlayer) {
        PlayerManager.currentPlayer = exoPlayer

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

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            if (PlayerManager.currentPlayer == exoPlayer) {
                PlayerManager.currentPlayer = null
            }
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = exoPlayer.currentPosition
            delay(500)
        }
    }

    var lifecycleKey by remember { mutableStateOf(UUID.randomUUID()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    lifecycleKey = UUID.randomUUID()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp),
    ) {
        key(lifecycleKey) {
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        useController = false
                        player = exoPlayer
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(220.dp),
            )
        }

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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                if (isPlaying) exoPlayer.pause() else exoPlayer.play()
            }) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                scope.launch {
                    val videoUrl = videoItem.videoId
                    // SỬA LỖI: Xác thực URL trước khi tải
                    if (!videoUrl.startsWith("https://firebasestorage.googleapis.com")) {
                        Toast.makeText(context, "Invalid download URL", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    withContext(Dispatchers.IO) {
                        try {
                            val connection = URL(videoUrl).openStream()
                            val file =
                                File(
                                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                                    "${videoItem.title.replace(" ", "_")}.mp4",
                                )
                            val output = FileOutputStream(file)
                            connection.copyTo(output)
                            output.close()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Downloaded: ${file.name}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }) {
                Icon(Icons.Default.Download, contentDescription = "Download")
            }
            IconButton(onClick = {
                val intent = Intent(context, FullscreenActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(8.dp))
                    .padding(12.dp),
        ) {
            Text(
                text = videoItem.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Next Videos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(suggestedVideos, key = { it.id }) { suggestion ->
                SuggestedVideoItem(
                    videoItem = suggestion,
                    onClick = {
                        navController.navigate("video_player/${suggestion.id}")
                    },
                )
            }
        }
    }
}

@Composable
fun SuggestedVideoItem(
    videoItem: VideoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .width(160.dp)
                .padding(end = 8.dp)
                .clickable(onClick = onClick),
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

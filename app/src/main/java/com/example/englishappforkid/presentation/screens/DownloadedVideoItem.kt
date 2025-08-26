package com.example.englishappforkid.presentation.screens.downloads

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.englishappforkid.data.model.VideoItem
import com.example.englishappforkid.presentation.playvideo.DBHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DownloadsViewModel(
    private val dbHelper: DBHelper,
) : ViewModel() {
    private val _downloadedVideos = MutableStateFlow<List<VideoItem>>(emptyList())
    val downloadedVideos: StateFlow<List<VideoItem>> = _downloadedVideos

    fun loadDownloadedVideos() {
        _downloadedVideos.value = dbHelper.getAllVideos()
    }

    fun deleteVideo(videoId: String) {
        dbHelper.removeVideoById(videoId)
        loadDownloadedVideos()
    }
}

class DownloadsViewModelFactory(
    private val dbHelper: DBHelper,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DownloadsViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun downloadsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }
    val viewModel: DownloadsViewModel = viewModel(factory = DownloadsViewModelFactory(dbHelper))

    val downloadedVideos by viewModel.downloadedVideos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDownloadedVideos()
    }

    Scaffold(
        topBar = {
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                color = Color.White,
                shadowElevation = 4.dp,
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Downloaded Video",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(downloadedVideos, key = { it.id }) { video ->
                downloadedVideoItem(
                    videoItem = video,
                    onClick = {
                        navController.navigate("downloaded_player/${video.id}")
                    },
                    onDeleteClick = {
                        viewModel.deleteVideo(video.id)
                    },
                )
            }
        }
    }
}

@Composable
fun downloadedVideoItem(
    videoItem: VideoItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Hiển thị ảnh thumbnail của video
            AsyncImage(
                model = videoItem.thumbnailUrl,
                contentDescription = videoItem.title,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(width = 120.dp, height = 70.dp)
                        .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Hiển thị tiêu đề video
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = videoItem.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
            }

            // Nút xóa
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa video",
                    tint = Color.Gray,
                )
            }
        }
    }
}

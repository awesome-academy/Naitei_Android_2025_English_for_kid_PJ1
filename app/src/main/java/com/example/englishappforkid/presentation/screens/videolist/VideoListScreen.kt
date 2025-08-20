package com.example.englishappforkid.presentation.screens.videolist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.VideoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun videoListScreen(
    navController: NavHostController,
    viewModel: VideoListViewModel = viewModel(),
) {
    val videos by viewModel.videos.collectAsState()
    val isListView by viewModel.isListView.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val filteredVideos =
        remember(videos, searchQuery, selectedLetter) {
            val currentSelectedLetter = selectedLetter
            videos.filter { video ->
                val matchesSearch = video.title.contains(searchQuery, ignoreCase = true)
                val matchesLetter =
                    currentSelectedLetter == null ||
                        video.title.startsWith(
                            currentSelectedLetter,
                            ignoreCase = true,
                        )
                matchesSearch && matchesLetter
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.short_stories)) },
                actions = {
                    // Nút xóa bộ lọc
                    if (searchQuery.isNotEmpty() || selectedLetter != null) {
                        IconButton(onClick = {
                            searchQuery = ""
                            selectedLetter = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear Filter")
                        }
                    }
                    // Nút chuyển đổi view
                    IconButton(onClick = { viewModel.toggleView() }) {
                        Icon(
                            imageVector = if (isListView) Icons.Default.GridView else Icons.AutoMirrored.Filled.ViewList,
                            contentDescription = "Switch View",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                placeholder = { Text(stringResource(R.string.search_stories)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            )
            alphabetFilter(
                selectedLetter = selectedLetter,
                onLetterClick = { letter ->
                    selectedLetter = if (selectedLetter == letter) null else letter
                },
            )
            if (isListView) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredVideos, key = { it.id }) { video ->
                        videoListItem(videoItem = video, onClick = {
                            Toast
                                .makeText(context, "Playing ${video.title}", Toast.LENGTH_SHORT)
                                .show()
                        })
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(filteredVideos, key = { it.id }) { video ->
                        videoGridItem(videoItem = video, onClick = {
                            Toast
                                .makeText(context, "Playing ${video.title}", Toast.LENGTH_SHORT)
                                .show()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun alphabetFilter(
    selectedLetter: String?,
    onLetterClick: (String) -> Unit,
) {
    val alphabet = ('A'..'Z').toList()
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        alphabet.forEach { letter ->
            Button(
                onClick = { onLetterClick(letter.toString()) },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            if (selectedLetter ==
                                letter.toString()
                            ) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                        contentColor =
                            if (selectedLetter ==
                                letter.toString()
                            ) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    ),
                modifier = Modifier.padding(horizontal = 2.dp),
            ) {
                Text(letter.toString())
            }
        }
    }
}

@Composable
fun videoListItem(
    videoItem: VideoItem,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = videoItem.thumbnailUrl,
                contentDescription = videoItem.title,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .width(120.dp)
                        .aspectRatio(16 / 9f)
                        .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = videoItem.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// Thêm lại hàm VideoGridItem
@Composable
fun videoGridItem(
    videoItem: VideoItem,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(8.dp)
                .aspectRatio(0.8f)
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = videoItem.thumbnailUrl,
                contentDescription = videoItem.title,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            )
            Text(
                text = videoItem.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

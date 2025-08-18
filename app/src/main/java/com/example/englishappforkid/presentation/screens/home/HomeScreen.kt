package com.example.englishappforkid.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishappforkid.data.model.VideoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val onLetterClick: (String) -> Unit = { letter ->
        selectedLetter = if (selectedLetter == letter) null else letter
    }

    val onItemClick: (VideoItem) -> Unit = { videoItem ->
        Toast.makeText(context, "Clicked on ${videoItem.title}", Toast.LENGTH_SHORT).show()
    }

    val filteredVideos = remember(uiState.videos, selectedLetter, searchQuery) {
        val currentSelectedLetter = selectedLetter
        uiState.videos.filter { video ->
            val matchesLetter = currentSelectedLetter == null || video.title.startsWith(
                currentSelectedLetter,
                ignoreCase = true
            )
            val matchesSearch = video.title.contains(searchQuery, ignoreCase = true)
            matchesLetter && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Short Stories") }, actions = {
                if (selectedLetter != null || searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        selectedLetter = null
                        searchQuery = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Filter"
                        )
                    }
                }
                IconButton(onClick = { homeViewModel.toggleView() }) {
                    Icon(imageVector = if (uiState.isListView) Icons.Default.GridView else Icons.Default.ViewList, contentDescription = "Switch View")
                }
            })
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search videos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") })

            AlphabetFilter(onLetterClick = onLetterClick)
            if (uiState.isListView) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredVideos, key = { it.id }) { videoItem ->
                        TopicListItem(videoItem = videoItem, onClick = { onItemClick(videoItem) })
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredVideos, key = { it.id }) { videoItem ->
                        TopicGridItem(videoItem = videoItem, onClick = { onItemClick(videoItem) })
                    }
                }
            }
        }
    }
}

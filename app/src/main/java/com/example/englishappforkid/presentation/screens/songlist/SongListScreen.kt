package com.example.englishappforkid.presentation.screens.songlist

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
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
import androidx.navigation.NavHostController
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.presentation.screens.videolist.alphabetFilter
import com.example.englishappforkid.presentation.screens.videolist.videoGridItem
import com.example.englishappforkid.presentation.screens.videolist.videoListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun songListScreen(
    navController: NavHostController,
    viewModel: SongListViewModel = viewModel(),
) {
    val songs by viewModel.songs.collectAsState()
    val isListView by viewModel.isListView.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val filteredSongs =
        remember(songs, searchQuery, selectedLetter) {
            val currentSelectedLetter = selectedLetter
            songs.filter { song ->
                val matchesSearch = song.title.contains(searchQuery, ignoreCase = true)
                val matchesLetter =
                    currentSelectedLetter == null ||
                        song.title.startsWith(
                            currentSelectedLetter,
                            ignoreCase = true,
                        )
                matchesSearch && matchesLetter
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Songs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (searchQuery.isNotEmpty() || selectedLetter != null) {
                        IconButton(onClick = {
                            searchQuery = ""
                            selectedLetter = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear Filter")
                        }
                    }
                    IconButton(onClick = { viewModel.toggleView() }) {
                        Icon(
                            imageVector = if (isListView) Icons.Default.GridView else Icons.AutoMirrored.Filled.ViewList,
                            contentDescription = "Switch View",
                        )
                    }
                    IconButton(onClick = { navController.navigate(ScreenRoutes.VIDEO_LIST) }) {
                        Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "Go to Video List")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                placeholder = { Text("Search songs...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            )
            alphabetFilter(
                selectedLetter = selectedLetter,
                onLetterClick = { letter ->
                    selectedLetter = if (selectedLetter == letter) null else letter
                },
            )
            if (isListView) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredSongs, key = { it.id }) { song ->
                        videoListItem(videoItem = song, onClick = {
                            // SỬA LỖI Ở ĐÂY
                            navController.navigate("song_player/${song.id}")
                            Toast
                                .makeText(context, "Playing ${song.title}", Toast.LENGTH_SHORT)
                                .show()
                        })
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(filteredSongs, key = { it.id }) { song ->
                        videoGridItem(videoItem = song, onClick = {
                            // SỬA LỖI Ở ĐÂY
                            navController.navigate("song_player/${song.id}")
                            Toast
                                .makeText(context, "Playing ${song.title}", Toast.LENGTH_SHORT)
                                .show()
                        })
                    }
                }
            }
        }
    }
}

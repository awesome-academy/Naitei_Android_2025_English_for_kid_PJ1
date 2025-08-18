//package com.example.englishappforkid.presentation.screens.home
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.GridView
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.ViewList
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.example.englishappforkid.data.DataSource
//import com.example.englishappforkid.data.model.VideoItem
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun contentListScreen() {
//    val allVideos = remember { DataSource.shortStories }
//
//    var isListView by remember { mutableStateOf(true) }
//    var selectedLetter by remember { mutableStateOf<String?>(null) }
//    var searchQuery by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//    val onLetterClick: (String) -> Unit = { letter ->
//        selectedLetter = if (selectedLetter == letter) null else letter
//    }
//
//    val onItemClick: (VideoItem) -> Unit = { videoItem ->
//        Toast.makeText(context, "Clicked on ${videoItem.title}", Toast.LENGTH_SHORT).show()
//    }
//
//    // Logic lọc kết hợp cả từ khóa tìm kiếm và chữ cái
//    val filteredVideos = remember(allVideos, selectedLetter, searchQuery) {
//        val currentSelectedLetter =
//            selectedLetter // Tạo một bản sao không null để compiler nhận diện
//        allVideos.filter { video ->
//            val matchesLetter = currentSelectedLetter == null || video.title.startsWith(
//                currentSelectedLetter, ignoreCase = true
//            )
//            val matchesSearch = video.title.contains(searchQuery, ignoreCase = true)
//            matchesLetter && matchesSearch
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Short Stories") }, actions = {
//                if (selectedLetter != null || searchQuery.isNotEmpty()) {
//                    IconButton(onClick = {
//                        selectedLetter = null
//                        searchQuery = ""
//                    }) {
//                        Icon(
//                            imageVector = Icons.Default.Close, contentDescription = "Clear Filter"
//                        )
//                    }
//                }
//                IconButton(onClick = { isListView = !isListView }) {
//                    Icon(
//                        imageVector = if (isListView) Icons.Default.GridView else Icons.Default.ViewList,
//                        contentDescription = "Switch View",
//                    )
//                }
//            })
//        }) { innerPadding ->
//        Column(modifier = Modifier.padding(innerPadding)) {
//            // Thêm thanh tìm kiếm
//            TextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                placeholder = { Text("Search videos...") },
//                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") })
//            AlphabetFilter(onLetterClick = onLetterClick)
//            if (isListView) {
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(filteredVideos, key = { it.title }) { videoItem ->
//                        TopicListItem(videoItem = videoItem, onClick = { onItemClick(videoItem) })
//                    }
//                }
//            } else {
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()
//                ) {
//                    items(filteredVideos, key = { it.title }) { videoItem ->
//                        TopicGridItem(videoItem = videoItem, onClick = { onItemClick(videoItem) })
//                    }
//                }
//            }
//        }
//    }
//}

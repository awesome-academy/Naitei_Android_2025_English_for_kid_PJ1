package com.example.englishappforkid.presentation.screens.gametopic

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishappforkid.data.model.Topic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val onItemClick: (Topic) -> Unit = { topic ->
        Toast.makeText(context, "Clicked on ${topic.name}", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("English Topics") },
                actions = {
                    IconButton(onClick = { homeViewModel.toggleView() }) {
                        Icon(
                            imageVector = if (uiState.isListView) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Switch View",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (uiState.isListView) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(uiState.topics) { topic ->
                    topicListItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(innerPadding),
            ) {
                items(uiState.topics) { topic ->
                    topicGridItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        }
    }
}

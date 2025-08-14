package com.example.englishappforkid.presentation.screens.home

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.englishappforkid.data.DataSource
import com.example.englishappforkid.data.model.Topic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentListScreen() {
    var isListView by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val onItemClick: (Topic) -> Unit = { topic ->
        Toast.makeText(context, "Clicked on ${topic.name}", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("English Topics") },
                actions = {
                    IconButton(onClick = { isListView = !isListView }) {
                        Icon(
                            imageVector = if (isListView) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Switch View"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isListView) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(DataSource.topics) { topic ->
                    TopicListItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(DataSource.topics) { topic ->
                    TopicGridItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        }
    }
}

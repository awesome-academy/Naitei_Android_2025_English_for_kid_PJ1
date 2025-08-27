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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.englishappforkid.data.DataSource
import com.example.englishappforkid.data.model.Topic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun contentListScreen(navController: NavHostController) {
    var isListView by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val onItemClick: (Topic) -> Unit = { topic ->
        Toast.makeText(context, "Clicked on ${topic.name}", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game") },
                actions = {
                    IconButton(onClick = { isListView = !isListView }) {
                        Icon(
                            imageVector = if (isListView) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Switch View",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (isListView) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(DataSource.topics) { topic ->
                    topicListItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(innerPadding),
            ) {
                items(DataSource.topics) { topic ->
                    topicGridItem(topic = topic, onClick = { onItemClick(topic) })
                }
            }
        }
    }
}

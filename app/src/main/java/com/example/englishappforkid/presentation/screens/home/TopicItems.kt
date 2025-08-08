package com.example.englishappforkid.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.englishappforkid.data.model.Topic

@Composable
fun TopicListItem(topic: Topic, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp).clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = getIconForTopic(topic.name), contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = topic.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun TopicGridItem(topic: Topic, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp).aspectRatio(1f).clickable(onClick = onClick)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = getIconForTopic(topic.name), contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = topic.name, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun getIconForTopic(topicName: String): ImageVector {
    return when (topicName) {
        "Animals" -> Icons.Default.Pets
        "Colors" -> Icons.Default.ColorLens
        "Numbers" -> Icons.Default.Numbers
        "Fruits" -> Icons.Default.Spa
        "Family" -> Icons.Default.FamilyRestroom
        "Jobs" -> Icons.Default.Work
        "Sports" -> Icons.Default.SportsBasketball
        "Kitchen" -> Icons.Default.Kitchen
        else -> Icons.Default.Star
    }
}


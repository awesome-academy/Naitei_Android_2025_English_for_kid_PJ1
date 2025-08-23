package com.example.englishappforkid.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.englishappforkid.data.model.Topic

@Composable
fun topicListItem(
    topic: Topic,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = getIconForTopic(topic.name),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = topic.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun topicGridItem(
    topic: Topic,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(8.dp)
                .aspectRatio(1f)
                .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = getIconForTopic(topic.name),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = topic.name, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun getIconForTopic(topicName: String): ImageVector =
    when (topicName) {
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

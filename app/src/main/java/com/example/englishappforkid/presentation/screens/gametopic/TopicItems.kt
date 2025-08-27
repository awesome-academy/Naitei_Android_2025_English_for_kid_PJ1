package com.example.englishappforkid.presentation.screens.gametopic

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.englishappforkid.R
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
            Image(
                painter = getImageForTopic(topic.name),
                contentDescription = topic.name,
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
            Image(
                painter = getImageForTopic(topic.name),
                contentDescription = topic.name,
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = topic.name, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun getImageForTopic(topicName: String): Painter =
    when (topicName) {
        "Animals" -> painterResource(id = R.drawable.animals)
        "Fruits" -> painterResource(id = R.drawable.fruits)
        "Colors" -> painterResource(id = R.drawable.colors)
        "School" -> painterResource(id = R.drawable.school)
        "Vehicles" -> painterResource(id = R.drawable.vehicles)
        "Sport" -> painterResource(id = R.drawable.sport)
        "Family" -> painterResource(id = R.drawable.family)
        "Number" -> painterResource(id = R.drawable.number)
        "Toys" -> painterResource(id = R.drawable.toys)
        "Nature" -> painterResource(id = R.drawable.nature)
        else -> painterResource(id = R.drawable.back_ground) // ảnh mặc định
    }

/**
 * Hiển thị danh sách theo dạng lưới 2 cột
 */
@Composable
fun topicGridScreen(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(8.dp),
    ) {
        items(topics) { topic ->
            topicGridItem(topic = topic, onClick = { onTopicClick(topic) })
        }
    }
}

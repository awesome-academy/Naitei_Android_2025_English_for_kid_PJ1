package com.example.englishappforkid.presentation.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.englishappforkid.data.model.VideoItem

@Composable
fun TopicListItem(videoItem: VideoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(videoItem.thumbnailUrl),
                contentDescription = null,
                //Ảnh tràn chiều ngang và có chiều cao cố định
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = videoItem.title, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun TopicGridItem(videoItem: VideoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(videoItem.thumbnailUrl),
                contentDescription = null,
                //Modifier để ảnh tràn hết không gian trong Card
                //Modifier để ảnh chiếm phần còn lại của Card, không che Text
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = videoItem.title, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun AlphabetFilter(onLetterClick: (String) -> Unit) {
    val alphabet = ('A'..'Z').toList()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(alphabet) { letter ->
            Button(
                onClick = { onLetterClick(letter.toString()) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Text(
                    text = letter.toString(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

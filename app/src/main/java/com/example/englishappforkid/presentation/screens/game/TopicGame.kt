package com.example.englishappforkid.presentation.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.TopicModel
import com.example.englishappforkid.ui.theme.boxBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topicGame(
    topics: List<TopicModel>,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.choose_the_topic),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            items(topics) { topic ->
                topicCard(topic) {
                    navController.navigate("game/${topic.name}")
                }
            }
        }
    }
}

@Composable
fun topicCard(
    topic: TopicModel,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Yellow),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(boxBackground)
                    .padding(12.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Image(
                    painter = getImageForTopic(topic.name),
                    contentDescription = topic.name,
                    modifier = Modifier.size(80.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = topic.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
            }
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
        else -> painterResource(id = R.drawable.back_ground)
    }

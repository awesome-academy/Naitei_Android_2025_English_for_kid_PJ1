package com.example.englishappforkid.presentation.screens.prehome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.LeaderboardEntry
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.boxFullname
import com.example.englishappforkid.ui.theme.colorButtonSelected
import com.example.englishappforkid.ui.theme.icRefresh

@Composable
fun preHomeScreen(
    navController: NavHostController,
    viewModel: LeaderboardViewModel = viewModel(),
) {
    val entries by viewModel.entries.collectAsState()
    preHomeScreenContent(navController = navController, entries = entries)
}

@Composable
fun preHomeScreenContent(
    navController: NavHostController,
    entries: List<LeaderboardEntry>,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        headerSection()
        Spacer(modifier = Modifier.height(36.dp))

        menuCard(
            title = stringResource(R.string.short_story),
            backgroundColor = boxBackground,
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_short_story),
                    contentDescription = "Short Story",
                    modifier = Modifier.size(36.dp),
                )
            },
            onClick = {
                navController.navigate(ScreenRoutes.VIDEO_LIST)
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuCard(
            title = stringResource(R.string.song),
            backgroundColor = boxBackground,
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_song),
                    contentDescription = "Song",
                    modifier = Modifier.size(36.dp),
                )
            },
            onClick = {
                // TODO: Thêm logic điều hướng cho mục "Song" sau này
            },
        )

        Spacer(modifier = Modifier.height(40.dp))
        leaderboardSection(entries)
    }
}

@Composable
fun headerSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.english_app),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = icRefresh,
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(colorButtonSelected, RoundedCornerShape(12.dp))
                    .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.hello))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = boxFullname),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(stringResource(R.string.full_name), color = Color.White)
            }
        }
    }
}

@Composable
fun menuCard(
    title: String,
    backgroundColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable(onClick = onClick),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp),
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

@Composable
fun leaderboardSection(entries: List<LeaderboardEntry>) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFCCF2F4), RoundedCornerShape(12.dp))
                .padding(8.dp),
    ) {
        Text(stringResource(R.string.leaderboard), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_crown),
                contentDescription = "Crown",
                modifier =
                    Modifier
                        .size(24.dp)
                        .align(Alignment.TopCenter),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("3rd", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "3rd",
                        modifier =
                            Modifier
                                .size(90.dp)
                                .padding(top = 24.dp),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.3f),
                ) {
                    Text("", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "1st",
                        modifier =
                            Modifier
                                .size(90.dp)
                                .padding(top = 8.dp),
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("2nd", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "2nd",
                        modifier =
                            Modifier
                                .size(90.dp)
                                .padding(top = 20.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        entries.forEach { entry ->

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${entry.rank}  ${entry.name}${if (entry.isYou) " (You)" else ""}",
                fontWeight = if (entry.isYou) FontWeight.Bold else FontWeight.Normal,
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

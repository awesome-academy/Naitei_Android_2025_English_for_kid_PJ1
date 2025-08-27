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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.rememberAsyncImagePainter
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.LeaderboardEntry
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.prehome.LeaderboardViewModel
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.colorButtonSelected
import com.example.englishappforkid.ui.theme.icRefresh
import com.google.firebase.auth.FirebaseAuth

@Composable
fun preHomeScreen(
    navController: NavHostController,
    viewModel: LeaderboardViewModel = viewModel(),
) {
    val entries by viewModel.entries.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    LaunchedEffect(Unit) {
        viewModel.loadLeaderboard()
    }

    preHomeScreenContent(
        navController = navController,
        entries = entries,
        email = user?.email,
        onRefresh = { viewModel.loadLeaderboard() },
    )
}

@Composable
fun preHomeScreenContent(
    navController: NavHostController,
    entries: List<LeaderboardEntry>,
    email: String?,
    onRefresh: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        headerSection(email, navController, onRefresh)
        Spacer(modifier = Modifier.height(20.dp))

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
            onClick = { navController.navigate(ScreenRoutes.VIDEO_LIST) },
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            onClick = { navController.navigate(ScreenRoutes.SONG_LIST) },
        )

        Spacer(modifier = Modifier.height(20.dp))
        leaderboardSection(entries)
    }
}

@Composable
fun headerSection(
    email: String?,
    navController: NavHostController,
    onRefresh: () -> Unit,
) {
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
                modifier =
                    Modifier.clickable {
                        onRefresh()
                    },
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(colorButtonSelected, RoundedCornerShape(12.dp))
                    .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.hello), color = Color.White)
            Text(text = email ?: stringResource(R.string.loading), color = Color.White)
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
    val top3 = entries.take(3)
    val you = entries.find { it.isYou }

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
                        .size(28.dp)
                        .align(Alignment.TopCenter),
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
            ) {
                // 3rd
                top3.getOrNull(2)?.let { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("3rd", fontWeight = FontWeight.Bold)
                        Image(
                            painter =
                                if (!player.avatar.isNullOrEmpty()) {
                                    rememberAsyncImagePainter(model = player.avatar)
                                } else {
                                    painterResource(id = R.drawable.person_1)
                                },
                            contentDescription = player.name,
                            modifier =
                                Modifier
                                    .size(70.dp)
                                    .padding(top = 24.dp),
                        )
                        Text(player.name, fontSize = 13.sp)
                    }
                }

                top3.getOrNull(0)?.let { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter =
                                if (!player.avatar.isNullOrEmpty()) {
                                    rememberAsyncImagePainter(model = player.avatar)
                                } else {
                                    painterResource(id = R.drawable.person_1)
                                },
                            contentDescription = player.name,
                            modifier =
                                Modifier
                                    .size(90.dp)
                                    .padding(top = 8.dp),
                        )
                        Text(player.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // 2nd
                top3.getOrNull(1)?.let { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("2nd", fontWeight = FontWeight.Bold)
                        Image(
                            painter =
                                if (!player.avatar.isNullOrEmpty()) {
                                    rememberAsyncImagePainter(model = player.avatar)
                                } else {
                                    painterResource(id = R.drawable.person_1)
                                },
                            contentDescription = player.name,
                            modifier =
                                Modifier
                                    .size(80.dp)
                                    .padding(top = 16.dp),
                        )
                        Text(player.name, fontSize = 13.sp)
                    }
                }
            }
        }

        if (entries.isNotEmpty()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(8.dp),
            ) {
                entries.getOrNull(0)?.let {
                    Text("Top 1: ${it.name} - ${it.score} pts", fontWeight = FontWeight.SemiBold)
                }
                entries.getOrNull(1)?.let {
                    Text("Top 2: ${it.name} - ${it.score} pts", fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)

                entries.getOrNull(2)?.let {
                    Text("Top 3: ${it.name} - ${it.score} pts", fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                you?.let {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Your Rank: ${it.rank}.${it.name} - ${it.score} pts",
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

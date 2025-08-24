package com.example.englishappforkid.presentation.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.ButtonYellow
import com.example.englishappforkid.ui.theme.DarkBlue

@Composable
fun welcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        // Stars
        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "Star 1",
            modifier =
                Modifier
                    .size(18.dp)
                    .offset(x = 325.dp, y = 315.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "Star 2",
            modifier =
                Modifier
                    .size(20.dp)
                    .offset(x = 330.dp, y = 230.dp),
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Text section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(top = 150.dp),
            ) {
                Text(
                    text = "Welcome to",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 30.dp),
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(250.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle),
                        contentDescription = "Circle around project name",
                        modifier = Modifier.fillMaxSize(),
                    )
                    Text(
                        text = "Project 01",
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkBlue,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }

            // Image section
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .offset(y = 20.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_astronaut_home),
                    contentDescription = "Astronaut on Rocket",
                    modifier =
                        Modifier
                            .size(300.dp)
                            .offset(y = (-70).dp),
                )

                // Rabbit
                Image(
                    painter = painterResource(id = R.drawable.ic_rabbits),
                    contentDescription = "Rabbits at the bottom",
                    modifier =
                        Modifier
                            .size(100.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-30).dp, y = (0).dp), // Adjusted offset for correct position
                )
            }

            // Button section
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { navController.navigate(ScreenRoutes.AUTH) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow),
                ) {
                    Text(
                        "Let's go",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    )
                }
            }
        }
    }
}

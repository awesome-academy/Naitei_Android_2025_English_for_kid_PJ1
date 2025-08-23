package com.example.englishappforkid.presentation.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes

@Composable
fun authScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_screen_login),
                contentDescription = "Astronaut Image",
                modifier =
                    Modifier
                        .size(300.dp)
                        .padding(top = 48.dp),
            )
            Text(
                text = stringResource(R.string.hello_everyone),
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(top = 64.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(ScreenRoutes.SIGN_IN) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    style = TextStyle(fontSize = 16.sp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(ScreenRoutes.SIGN_UP) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_up), style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}

package com.example.englishappforkid.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.R
import com.example.englishappforkid.ui.theme.Cowbell
import com.example.englishappforkid.ui.theme.Pink80
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.englishAppForKidTheme
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes

@Composable
fun profileScreen(navController: NavHostController) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        nameSection(
            onClick = {
                navController.navigate(ScreenRoutes.PROFILE_DETAIL)
            },
        )

        Spacer(modifier = Modifier.height(36.dp))

        menuCard(
            title = stringResource(R.string.edit_profile),
            backgroundColor = boxBackground,
            onClick = { navController.navigate(ScreenRoutes.EDIT_PROFILE) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuCard(
            title = stringResource(R.string.notification_setup),
            backgroundColor = boxBackground,
            onClick = { navController.navigate(ScreenRoutes.NOTIFICATION_SETUP) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuCard(
            title = stringResource(R.string.term_policy),
            backgroundColor = boxBackground,
            onClick = { navController.navigate(ScreenRoutes.TERM_POLICY) },
        )

        Spacer(modifier = Modifier.height(36.dp))

        logout(
            title = stringResource(R.string.log_out),
            backgroundColor = Cowbell,
            onClick = {
                navController.navigate(ScreenRoutes.LOGIN) {
                    popUpTo(ScreenRoutes.PROFILE) { inclusive = true }
                }
            },
        )
    }
}

@Composable
fun nameSection(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = boxBackground),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person_1),
                    contentDescription = "Profile Picture",
                    modifier =
                        Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(24.dp)),
                )
                Spacer(modifier = Modifier.width(24.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.full_name), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text(text = stringResource(R.string.view_my_profile), color = Color.Blue, fontSize = 18.sp)
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go",
                    tint = Pink80,
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color.Yellow,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Rating Go",
                    tint = Pink80,
                )
            }
        }
    }
}

@Composable
fun menuCard(
    title: String,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { onClick() },
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go",
                    tint = Pink80,
                )
            }
        }
    }
}

@Composable
fun logout(
    title: String,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previewProfileScreen() {
    englishAppForKidTheme {
        val navController = rememberNavController()
        profileScreen(navController = navController)
    }
}

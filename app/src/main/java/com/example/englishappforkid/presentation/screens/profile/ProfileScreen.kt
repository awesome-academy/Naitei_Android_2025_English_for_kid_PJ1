package com.example.englishappforkid.presentation.screens.profile

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.UserProfile
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.Cowbell
import com.example.englishappforkid.ui.theme.Pink80
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.englishAppForKidTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun profileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(),
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val auth = FirebaseAuth.getInstance()

    if (userProfile != null) {
        profileContent(userProfile = userProfile!!, navController = navController, auth = auth)
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun profileContent(
    userProfile: UserProfile,
    navController: NavHostController,
    auth: FirebaseAuth,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Pink80,
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(28.dp)
                        .clickable {
                            navController.popBackStack()
                        },
            )
            Text(
                text = stringResource(R.string.profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        nameSection(
            userProfile = userProfile,
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
                auth.signOut()
                navController.navigate(ScreenRoutes.WELCOME) {
                    popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                }
            },
        )
    }
}

@Composable
fun nameSection(
    userProfile: UserProfile,
    onClick: () -> Unit,
) {
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
                // Thay thế Image bằng AsyncImage để tải ảnh từ URL
                if (userProfile.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = userProfile.avatarUrl,
                        contentDescription = "Profile Picture",
                        modifier =
                            Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp)),
                    )
                } else {
                    // Hiển thị ảnh trắng nếu không có ảnh đại diện
                    Box(
                        modifier =
                            Modifier
                                .size(80.dp)
                                .background(Color.LightGray, RoundedCornerShape(24.dp)),
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = userProfile.fullName, fontWeight = FontWeight.Bold, fontSize = 24.sp)
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
        val fakeUser =
            UserProfile(
                uid = "fake_uid",
                fullName = "Nguyen Van A",
                email = "test@example.com",
                avatarUrl = "",
            )
        profileContent(userProfile = fakeUser, navController = navController, auth = FirebaseAuth.getInstance())
    }
}

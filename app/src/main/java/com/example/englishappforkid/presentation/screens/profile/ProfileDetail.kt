package com.example.englishappforkid.presentation.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.UserProfile

private val colorPink80 = Color(0xFFEFB8C8)
private val colorBoxBackground = Color(0xFFF0F0F0)
private val colorCowbell = Color(0xFFD0BCFF)

@Composable
fun profileDetailScreen(
    navController: NavHostController,
    userProfile: UserProfile,
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
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(28.dp)
                        .clickable { navController.popBackStack() },
                tint = colorPink80,
            )

            Text(
                text = stringResource(R.string.my_profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_crown),
                    contentDescription = "Crown",
                    modifier =
                        Modifier
                            .size(32.dp)
                            .offset(y = 12.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = userProfile.avatarUrl,
                    contentDescription = "Avatar",
                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(4.dp, Color.Yellow, RoundedCornerShape(16.dp)),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userProfile.fullName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorBoxBackground),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                profileInfoRow(label = "Email", value = userProfile.email)
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(label = "Address", value = userProfile.address)
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(label = "Nickname", value = userProfile.nickname)
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(label = "Age", value = userProfile.age)
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color.Yellow,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: xử lý thay đổi mật khẩu */ },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorCowbell),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = "Lock",
                tint = Color.Black,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.change_password),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun profileInfoRow(
    label: String,
    value: String,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = label, color = Color.Blue, fontSize = 14.sp)
                Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = Color.Red,
            )
        }
    }
}

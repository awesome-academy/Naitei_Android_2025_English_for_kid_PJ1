package com.example.englishappforkid.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishappforkid.R
import com.example.englishappforkid.ui.theme.*

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(36.dp))

        // Menu Cards
        MenuCard(
            title = "Short Story",
            backgroundColor = boxBackground,
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_short_story),
                    contentDescription = "Short Story",
                    modifier = Modifier.size(36.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuCard(
            title = "Song",
            backgroundColor = boxBackground,
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_song),
                    contentDescription = "Song",
                    modifier = Modifier.size(36.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Leaderboard (currently commented out)
        LeaderboardSection()

        Spacer(modifier = Modifier.height(20.dp))

        BottomNavBar()
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Để "English App" căn giữa mà vẫn có icon ở bên phải,
            // ta thêm Spacer vào bên trái để cân bằng với icon bên phải
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = "English App",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,

            )
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = icRefresh
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorButtonSelected, RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Hello")
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = boxFullname),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("full name.", color = Color.White)
            }
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    backgroundColor: Color,
    icon: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}


@Composable
fun LeaderboardSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFCCF2F4), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // Title section with crown
        Text("👑 Leaderboard", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Row for images
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Crown above the first place
            Image(
                painter = painterResource(id = R.drawable.ic_crown), // Add the crown image
                contentDescription = "Crown",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopCenter)

            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, // Space images evenly
                modifier = Modifier.fillMaxWidth()
            ) {
                // 3rd place
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("3rd", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "3rd",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(top = 24.dp)
                    )
                }

                // 1st place
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.3f)
                ) {
                    Text("", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "1st",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(top = 8.dp)// 1st place (largest)
                    )
                }

                // 2nd place
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("2nd", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "2nd",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(top = 20.dp)
                    )
                }
            }
        }



        // Add Dividers between the text rows
        Spacer(modifier = Modifier.height(36.dp))

        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        Text("100  Triệu Thị Huyền (You)")

        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        // Line separating the names
        Spacer(modifier = Modifier.height(8.dp))

        Text("101  Vũ Hà My")
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        // Line separating the names
        Spacer(modifier = Modifier.height(8.dp))

        Text("101  Vũ Hà My")
    }
}


@Composable
fun BottomNavBar() {
    NavigationBar(containerColor = colorButtonBarBackGround) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Stories"
                )
            },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_book),
                    contentDescription = "Stories"
                )
            },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "Download"
                )
            },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .background(
                            if (true) Color.Yellow else Color.White, // Nền vàng khi chọn
                        )

                )
                Image(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "User",
                )
            },
            selected = false,
            onClick = {}
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    EnglishAppForKidTheme {
        HomeScreen()
    }
}

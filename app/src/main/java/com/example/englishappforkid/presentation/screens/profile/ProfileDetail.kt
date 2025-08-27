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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.boxBackground

@Composable
fun profileDetailScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    val userProfile by viewModel.userProfile.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(boxBackground)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(28.dp)
                        .clickable { navController.popBackStack() },
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(R.string.my_profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar + Name
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
                if (!userProfile?.avatarUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(userProfile?.avatarUrl)
                                .crossfade(true)
                                .build(),
                        contentDescription = "Avatar",
                        modifier =
                            Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    4.dp,
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(16.dp),
                                ),
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.person_1),
                        contentDescription = "Default Avatar",
                        modifier =
                            Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    4.dp,
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(16.dp),
                                ),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                        if (!userProfile?.fullname.isNullOrEmpty()) {
                            userProfile!!.fullname
                        } else {
                            stringResource(
                                R.string.no_name,
                            )
                        },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Info Card
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                profileInfoRow(
                    label = stringResource(R.string.email),
                    value = userProfile?.email ?: stringResource(R.string.not_set),
                    showEdit = false,
                )
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(
                    label = stringResource(R.string.address),
                    value =
                        userProfile?.address?.ifEmpty { stringResource(R.string.not_set) } ?: stringResource(R.string.not_set),
                    showEdit = false,
                )
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(
                    label = stringResource(R.string.nickname),
                    value =
                        userProfile?.nickname?.ifEmpty { stringResource(R.string.not_set) } ?: stringResource(R.string.not_set),
                    showEdit = false,
                )
                Spacer(modifier = Modifier.height(12.dp))
                profileInfoRow(
                    label = stringResource(R.string.age),
                    value =
                        userProfile?.age?.ifEmpty { stringResource(R.string.not_set) } ?: stringResource(R.string.not_set),
                    showEdit = false,
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
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
            onClick = { navController.navigate(ScreenRoutes.EDIT_PROFILE) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.edit_profile),
                color = Color.White,
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
    showEdit: Boolean = true,
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
                Text(text = label, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            if (showEdit) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

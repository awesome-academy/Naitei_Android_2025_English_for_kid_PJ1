package com.example.englishappforkid.presentation.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    val userProfileState by viewModel.userProfile.collectAsState()
    val isUpdating by viewModel.isUpdating.collectAsState()

    var nickname by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    // Đồng bộ dữ liệu khi userProfile thay đổi
    LaunchedEffect(userProfileState) {
        userProfileState?.let {
            nickname = if (it.nickname.isNotBlank()) it.nickname else "No Name"
            fullname = if (it.fullname.isNotBlank()) it.fullname else "No Name"
            address = if (it.address.isNotBlank()) it.address else "No Address"
            age = if (it.age.isNotBlank()) it.age else "No Age"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { navController.popBackStack() },
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
    ) { innerPadding ->
        val profile = userProfileState // local val để tránh smart cast error

        if (profile == null) {
            // Hiển thị loading khi chưa có dữ liệu
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .clickable { pickImageLauncher.launch("image/*") },
                ) {
                    when {
                        selectedImageUri != null -> {
                            AsyncImage(
                                model =
                                    ImageRequest
                                        .Builder(context)
                                        .data(selectedImageUri)
                                        .crossfade(true)
                                        .build(),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        profile.avatarUrl.isNotBlank() -> {
                            AsyncImage(
                                model =
                                    ImageRequest
                                        .Builder(context)
                                        .data(profile.avatarUrl)
                                        .crossfade(true)
                                        .build(),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        else -> {
                            Text("Pick Image", color = Color.DarkGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Email (không cho chỉnh sửa)
                OutlinedTextField(
                    value = profile.email,
                    onValueChange = {},
                    label = { Text("Email") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = fullname,
                    onValueChange = { fullname = it },
                    label = { Text("Fullname") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(
                            address = if (address == "No Address") "" else address,
                            nickname = if (nickname == "No Name") "" else nickname,
                            fullname = if (fullname == "No Name") "" else fullname,
                            age = if (age == "No Age") "" else age,
                            newAvatarUri = selectedImageUri,
                        )
                        navController.popBackStack()
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isUpdating,
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.englishappforkid.R

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

    val noName = stringResource(R.string.no_name)
    val noAddress = stringResource(R.string.no_address)
    val noAge = stringResource(R.string.no_age)

    LaunchedEffect(userProfileState) {
        userProfileState?.let {
            nickname = if (it.nickname.isNotBlank()) it.nickname else noName
            fullname = if (it.fullname.isNotBlank()) it.fullname else noName
            address = if (it.address.isNotBlank()) it.address else noAddress
            age = if (it.age.isNotBlank()) it.age else noAge
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.edit_profile),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
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
        val profile = userProfileState

        if (profile == null) {
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
                                contentDescription = stringResource(R.string.pick_image),
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
                                contentDescription = stringResource(R.string.pick_image),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        else -> {
                            Text(stringResource(R.string.pick_image), color = Color.DarkGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = profile.email,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.email)) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text(stringResource(R.string.nickname)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = fullname,
                    onValueChange = { fullname = it },
                    label = { Text(stringResource(R.string.fullname)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.address)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text(stringResource(R.string.age)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(
                            address = if (address == noAddress) "" else address,
                            nickname = if (nickname == noName) "" else nickname,
                            fullname = if (fullname == noName) "" else fullname,
                            age = if (age == noAge) "" else age,
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
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Text(
                            stringResource(R.string.save_changes),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

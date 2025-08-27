package com.example.englishappforkid.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.data.DataManager
import com.example.englishappforkid.ui.theme.ButtonYellow
import com.example.englishappforkid.ui.theme.Orange

@Composable
fun forgotPasswordScreen(navController: NavHostController) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(DataManager(context)))
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        // Back Button
        Box(
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                    .background(Orange, shape = RoundedCornerShape(20.dp))
                    .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Color.Black,
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // Astronaut Image - Bạn có thể thay bằng ảnh phi hành gia với ngôi sao
            Image(
                painter = painterResource(id = R.drawable.ic_screen_login),
                contentDescription = "astronaut_image",
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Reset password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { authViewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Send Email button
            Button(
                onClick = {
                    // Gọi hàm trong ViewModel và xử lý khi thành công
                    authViewModel.sendPasswordResetEmail {
                        Toast.makeText(context, "Password reset link sent. Please check your email.", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    }
                },
                enabled = !uiState.isLoading,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Send Email", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

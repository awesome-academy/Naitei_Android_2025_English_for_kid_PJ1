@file:Suppress("DEPRECATION")

package com.example.englishappforkid.presentation.screens.auth

import AuthViewModel
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.data.DataManager
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.ButtonYellow
import com.example.englishappforkid.ui.theme.ForgotPasswordBlue
import com.example.englishappforkid.ui.theme.Orange
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun signInScreen(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(DataManager(context)))

    val uiState by authViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var passwordVisibility by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        authViewModel.loadSavedCredentials()
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Google Sign-In config
    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("395767946621-2bbq4gfbagb1h9mgaqm38r0n6ere15rp.apps.googleusercontent.com")
            .requestEmail()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    val googleLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authViewModel.signInWithGoogle(account) {
                    navController.navigate(ScreenRoutes.HOME) {
                        popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                    }
                }
            } catch (e: ApiException) {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.google_sign_in_failed, e.message),
                        Toast.LENGTH_SHORT,
                    ).show()
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
            Spacer(modifier = Modifier.height(90.dp))

            // Astronaut Image
            Image(
                painter = painterResource(id = R.drawable.ic_screen_login),
                contentDescription = "astronaut_image",
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(R.string.sign_in),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { authViewModel.onEmailChange(it) },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) },
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = uiState.pass,
                onValueChange = { authViewModel.onPasswordChange(it) },
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "toggle password visibility",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions =
                    KeyboardActions {
                        authViewModel.signIn(rememberMe = isChecked) {
                            navController.navigate(ScreenRoutes.HOME) {
                                popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                            }
                        }
                    },
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remember me & Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.remember_me), fontSize = 14.sp, color = Color.Black)
                }
                TextButton(onClick = { navController.navigate(ScreenRoutes.FORGOT_PASSWORD) }) {
                    Text(stringResource(R.string.forgot_password), fontSize = 14.sp, color = ForgotPasswordBlue)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In button
            Button(
                onClick = {
                    authViewModel.signIn(rememberMe = isChecked) {
                        navController.navigate(ScreenRoutes.HOME) {
                            popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                        }
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
                    Text(stringResource(R.string.sign_in), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign Up
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.don_t_have_an_account), fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { navController.navigate(ScreenRoutes.SIGN_UP) },
                ) {
                    Text(stringResource(R.string.sign_up), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForgotPasswordBlue)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Divider with OR
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Or", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Google Sign-In button
            Button(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    googleLauncher.launch(signInIntent)
                },
                enabled = !uiState.isLoading,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "google logo",
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.sign_in_by_google), fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

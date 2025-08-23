@file:Suppress("DEPRECATION")

package com.example.englishappforkid.presentation.screens.auth

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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.ButtonYellow
import com.example.englishappforkid.ui.theme.ForgotPasswordBlue
import com.example.englishappforkid.ui.theme.Orange
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun signInScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
) {
    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("395767946621-2bbq4gfbagb1h9mgaqm38r0n6ere15rp.apps.googleusercontent.com")
            .requestEmail()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authViewModel.signInWithGoogle(account) {
                    navController.navigate(ScreenRoutes.HOME) {
                        popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google Sign In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_ground),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        // Back Button
        Box(
            modifier =
                Modifier
                    .padding(25.dp)
                    .size(45.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(22.5.dp))
                    .background(Orange, shape = RoundedCornerShape(22.5.dp))
                    .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(25.dp),
                tint = Color.Black,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_screen_login),
            contentDescription = "Astronaut with a star",
            modifier =
                Modifier
                    .size(250.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (30.dp)),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(300.dp))

            Text(
                "Sign In",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { authViewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.pass,
                onValueChange = { authViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation =
                    if (passwordVisibility) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector =
                                if (passwordVisibility) {
                                    Icons.Filled.Visibility
                                } else {
                                    Icons.Filled.VisibilityOff
                                },
                            contentDescription = "Toggle Password visibility",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions =
                    KeyboardActions(onDone = {
                        authViewModel.signIn {
                            navController.navigate(ScreenRoutes.HOME) {
                                popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                            }
                        }
                    }),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var isChecked by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        modifier = Modifier.size(24.dp),
                    )
                    Text(" Remember me", fontSize = 12.sp, color = Color.Black)
                }
                TextButton(onClick = { navController.navigate(ScreenRoutes.FORGOT_PASSWORD) }) {
                    Text("Forgot Password?", fontSize = 12.sp, color = ForgotPasswordBlue)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.signIn {
                        navController.navigate(ScreenRoutes.HOME) {
                            popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
                        }
                    }
                },
                enabled = !uiState.isLoading,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonYellow),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign In", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account?", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = { navController.navigate(ScreenRoutes.SIGN_UP) },
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            "Sign up",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForgotPasswordBlue,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    HorizontalDivider(Modifier.weight(1f), color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Or", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    HorizontalDivider(Modifier.weight(1f), color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    googleLauncher.launch(signInIntent)
                },
                enabled = !uiState.isLoading,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in by Google", fontSize = 20.sp, color = Color.Red)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

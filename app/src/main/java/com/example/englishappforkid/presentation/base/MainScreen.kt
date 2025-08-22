package com.example.englishappforkid.presentation.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.UserProfile
import com.example.englishappforkid.presentation.base.components.bottomNavBar
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.presentation.screens.auth.AuthScreen
import com.example.englishappforkid.presentation.screens.auth.ForgotPasswordScreen
import com.example.englishappforkid.presentation.screens.auth.SignInScreen
import com.example.englishappforkid.presentation.screens.auth.SignUpScreen
import com.example.englishappforkid.presentation.screens.auth.WelcomeScreen
import com.example.englishappforkid.presentation.screens.home.contentListScreen
import com.example.englishappforkid.presentation.screens.notification.notiSetup
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen
import com.example.englishappforkid.presentation.screens.profile.profileDetailScreen
import com.example.englishappforkid.presentation.screens.profile.profileScreen
import com.example.englishappforkid.presentation.screens.songlist.songListScreen
import com.example.englishappforkid.presentation.screens.videolist.videoListScreen

@Composable
fun mainScreen() {
    val navController = rememberNavController()
    val fakeUser =
        UserProfile(
            fullName = "Nguyen Van A",
            address = "Thai Nguyen",
            nickname = "Fox",
            age = "16 years old",
            avatarResId = R.drawable.person_1,
        )
    Scaffold(
        bottomBar = {
            bottomNavBar(navController = navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(ScreenRoutes.WELCOME) { WelcomeScreen(navController) }
            composable(ScreenRoutes.AUTH) { AuthScreen(navController) }
            composable(ScreenRoutes.SIGN_IN) { SignInScreen(navController) }
            composable(ScreenRoutes.SIGN_UP) { SignUpScreen(navController) }
            composable(ScreenRoutes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController) }

            composable(ScreenRoutes.HOME) { preHomeScreen(navController) }
            composable(ScreenRoutes.STORY) { contentListScreen(navController) }
            composable(ScreenRoutes.DOWNLOAD) { /* downloadScreen(navController) */ }
            composable(ScreenRoutes.PROFILE) { profileScreen(navController) }
            composable(ScreenRoutes.EDIT_PROFILE) { /* Edit Profile Screen */ }
            composable(ScreenRoutes.NOTIFICATION_SETUP) { notiSetup(navController) }
            composable(ScreenRoutes.TERM_POLICY) { /* Policy Screen */ }
            composable(ScreenRoutes.LOGIN) { /* Login Screen */ }
            composable(ScreenRoutes.VIDEO_LIST) { videoListScreen(navController = navController) }
            composable(ScreenRoutes.SONG_LIST) { songListScreen(navController = navController) }
            composable(ScreenRoutes.PROFILE_DETAIL) { profileDetailScreen(navController, userProfile = fakeUser) }
        }
    }
}

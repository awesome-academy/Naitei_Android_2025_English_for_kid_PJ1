package com.example.englishappforkid.presentation.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishappforkid.presentation.base.components.bottomNavBar
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.presentation.playvideo.songScreen
import com.example.englishappforkid.presentation.playvideo.videoScreen
import com.example.englishappforkid.presentation.screens.auth.authScreen
import com.example.englishappforkid.presentation.screens.auth.forgotPasswordScreen
import com.example.englishappforkid.presentation.screens.auth.signInScreen
import com.example.englishappforkid.presentation.screens.auth.signUpScreen
import com.example.englishappforkid.presentation.screens.auth.welcomeScreen
import com.example.englishappforkid.presentation.screens.downloads.downloadedVideoPlayerScreen
import com.example.englishappforkid.presentation.screens.downloads.downloadsScreen
import com.example.englishappforkid.presentation.screens.notification.notiSetup
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen
import com.example.englishappforkid.presentation.screens.profile.ProfileViewModel
import com.example.englishappforkid.presentation.screens.profile.editProfileScreen
import com.example.englishappforkid.presentation.screens.profile.profileDetailScreen
import com.example.englishappforkid.presentation.screens.profile.profileScreen
import com.example.englishappforkid.presentation.screens.songlist.songListScreen
import com.example.englishappforkid.presentation.screens.videolist.videoListScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun mainScreen() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val profileViewModel: ProfileViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navController.navigate(ScreenRoutes.HOME) {
                popUpTo(ScreenRoutes.WELCOME) { inclusive = true }
            }
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar =
        when (currentBackStackEntry?.destination?.route) {
            ScreenRoutes.HOME, ScreenRoutes.SONG_LIST, ScreenRoutes.DOWNLOAD, ScreenRoutes.PROFILE -> true
            else -> false
        }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                bottomNavBar(navController = navController)
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.WELCOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(ScreenRoutes.MAIN_SCREEN) { mainScreen() }
            composable(ScreenRoutes.WELCOME) { welcomeScreen(navController) }
            composable(ScreenRoutes.AUTH) { authScreen(navController) }
            composable(ScreenRoutes.SIGN_IN) { signInScreen(navController) }
            composable(ScreenRoutes.SIGN_UP) { signUpScreen(navController) }
            composable(ScreenRoutes.FORGOT_PASSWORD) { forgotPasswordScreen(navController) }
            composable(ScreenRoutes.HOME) { preHomeScreen(navController) }
            composable(ScreenRoutes.DOWNLOAD) { downloadsScreen(navController) }
            composable(ScreenRoutes.PROFILE) { profileScreen(navController) }

            composable(ScreenRoutes.EDIT_PROFILE) {
                editProfileScreen(navController = navController, viewModel = profileViewModel)
            }

            composable(ScreenRoutes.NOTIFICATION_SETUP) { notiSetup(navController) }
            composable(ScreenRoutes.TERM_POLICY) { /* Policy Screen */ }
            composable(ScreenRoutes.LOGIN) { /* Login Screen */ }
            composable(ScreenRoutes.VIDEO_LIST) { videoListScreen(navController) }
            composable(ScreenRoutes.SONG_LIST) { songListScreen(navController) }

            composable(ScreenRoutes.PROFILE_DETAIL) {
                val userProfile by profileViewModel.userProfile.collectAsState()
                profileDetailScreen(navController, profileViewModel)
            }

            composable(
                route = "video_player/{videoId}",
                arguments = listOf(navArgument("videoId") { type = NavType.StringType }),
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("videoId")?.let { videoId ->
                    videoScreen(videoId = videoId, navController = navController)
                }
            }

            composable(
                route = "song_player/{songId}",
                arguments = listOf(navArgument("songId") { type = NavType.StringType }),
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("songId")?.let { songId ->
                    songScreen(videoId = songId, navController = navController)
                }
            }

            composable(
                route = "downloaded_player/{downloadId}",
                arguments = listOf(navArgument("downloadId") { type = NavType.StringType }),
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("downloadId")?.let { downloadId ->
                    downloadedVideoPlayerScreen(videoId = downloadId, navController = navController)
                }
            }
        }
    }
}

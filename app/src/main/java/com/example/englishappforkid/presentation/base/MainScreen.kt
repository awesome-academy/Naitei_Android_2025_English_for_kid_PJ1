package com.example.englishappforkid.presentation.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishappforkid.R
import com.example.englishappforkid.data.model.UserProfile
import com.example.englishappforkid.presentation.base.components.bottomNavBar
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.presentation.playvideo.songScreen
import com.example.englishappforkid.presentation.playvideo.videoScreen
import com.example.englishappforkid.presentation.screens.downloads.downloadedVideoPlayerScreen
import com.example.englishappforkid.presentation.screens.downloads.downloadsScreen
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
            composable(ScreenRoutes.HOME) { preHomeScreen(navController) }
            composable(ScreenRoutes.STORY) { contentListScreen(navController) }
            composable(ScreenRoutes.DOWNLOAD) { downloadsScreen(navController = navController) }
            composable(ScreenRoutes.PROFILE) { profileScreen(navController) }
            composable(ScreenRoutes.EDIT_PROFILE) { /* Edit Profile Screen */ }
            composable(ScreenRoutes.NOTIFICATION_SETUP) { notiSetup(navController) }
            composable(ScreenRoutes.TERM_POLICY) { /* Policy Screen */ }
            composable(ScreenRoutes.LOGIN) { /* Login Screen */ }
            composable(ScreenRoutes.VIDEO_LIST) { videoListScreen(navController = navController) }
            composable(ScreenRoutes.SONG_LIST) { songListScreen(navController = navController) }
            composable(ScreenRoutes.PROFILE_DETAIL) { profileDetailScreen(navController, userProfile = fakeUser) }

            composable(
                route = "video_player/{videoId}",
                arguments = listOf(navArgument("videoId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId")
                if (videoId != null) {
                    videoScreen(
                        videoId = videoId,
                        navController = navController,
                    )
                }
            }
            composable(
                route = "song_player/{songId}",
                arguments = listOf(navArgument("songId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val songId = backStackEntry.arguments?.getString("songId")
                if (songId != null) {
                    songScreen(
                        videoId = songId,
                        navController = navController,
                    )
                }
            }
            composable(
                route = "downloaded_player/{downloadId}",
                arguments = listOf(navArgument("downloadId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val downloadId = backStackEntry.arguments?.getString("downloadId")
                if (downloadId != null) {
                    downloadedVideoPlayerScreen(
                        videoId = downloadId,
                        navController = navController,
                    )
                }
            }
        }
    }
}

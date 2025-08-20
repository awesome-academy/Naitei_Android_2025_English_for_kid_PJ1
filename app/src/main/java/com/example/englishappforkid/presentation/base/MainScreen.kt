package com.example.englishappforkid.presentation.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.presentation.base.components.bottomNavBar
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.presentation.screens.home.contentListScreen
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen
import com.example.englishappforkid.presentation.screens.profile.profileScreen
import com.example.englishappforkid.presentation.screens.videolist.videoListScreen

@Composable
fun mainScreen() {
    val navController = rememberNavController()

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
            composable(ScreenRoutes.DOWNLOAD) { /* downloadScreen(navController) */ }
            composable(ScreenRoutes.PROFILE) { profileScreen(navController) }
            composable(ScreenRoutes.EDIT_PROFILE) { /* Edit Profile Screen */ }
            composable(ScreenRoutes.NOTIFICATION_SETUP) { /* Noti Setup Screen */ }
            composable(ScreenRoutes.TERM_POLICY) { /* Policy Screen */ }
            composable(ScreenRoutes.LOGIN) { /* Login Screen */ }
            composable(ScreenRoutes.PROFILE_DETAIL) { /* Profile Detail */ }
            composable(ScreenRoutes.VIDEO_LIST) { videoListScreen(navController = navController) }
        }
    }
}

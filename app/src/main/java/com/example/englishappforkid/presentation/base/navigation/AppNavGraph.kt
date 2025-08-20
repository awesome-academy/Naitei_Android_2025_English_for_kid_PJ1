package com.example.englishappforkid.presentation.base.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.englishappforkid.presentation.screens.playvideo.VideoScreen
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen
import com.example.englishappforkid.presentation.screens.videolist.videoListScreen

@Composable
fun appNavGraph(navController: NavHostController) {
    // startDestination là màn hình đầu tiên của ứng dụng
    NavHost(navController = navController, startDestination = "home") {

        // Màn hình chào mừng
        composable("home") { preHomeScreen(navController) }

        // Màn hình danh sách video. Khi điều hướng, hãy dùng route "story".
        composable("story") { videoListScreen(navController) }

        // Màn hình phát video
        composable(
            route = "video_player/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            if (videoId != null) {
                VideoScreen(
                    videoId = videoId,
                    navController = navController
                )
            }
        }
    }
}
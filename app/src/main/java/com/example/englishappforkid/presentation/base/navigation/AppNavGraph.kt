package com.example.englishappforkid.presentation.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.presentation.screens.home.contentListScreen
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen

@Composable
fun appNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { preHomeScreen(navController) }
        composable("story") { contentListScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun previewAppNavGraph() {
    val navController = rememberNavController()
    appNavGraph(navController = navController)
}

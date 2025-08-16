package com.example.englishappforkid.presentation.base
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.presentation.base.components.bottomNavBar
import com.example.englishappforkid.presentation.screens.home.contentListScreen
import com.example.englishappforkid.presentation.screens.prehome.preHomeScreen
import com.example.englishappforkid.presentation.screens.profile.profileScreen

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
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("home") { preHomeScreen(navController) }
            composable("story") { contentListScreen(navController) }
            composable("download") { /* downloadScreen(navController) */ }
            composable("profile") { profileScreen(navController) }
        }
    }
}

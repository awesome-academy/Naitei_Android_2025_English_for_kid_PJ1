package com.example.englishappforkid.presentation.base.components

import androidx.annotation.DrawableRes
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes

data class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
)

val bottomNavItems =
    listOf(
        BottomNavItem(ScreenRoutes.HOME, R.drawable.ic_home),
        BottomNavItem(ScreenRoutes.SONG_LIST, R.drawable.ic_book),
        BottomNavItem(ScreenRoutes.DOWNLOAD, R.drawable.ic_download),
        BottomNavItem(ScreenRoutes.PROFILE, R.drawable.ic_user),
    )

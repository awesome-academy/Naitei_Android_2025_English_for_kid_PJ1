package com.example.englishappforkid.presentation.base.components

import androidx.annotation.DrawableRes
import com.example.englishappforkid.R

data class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
)

val bottomNavItems =
    listOf(
        BottomNavItem("home", R.drawable.ic_home),
        BottomNavItem("story", R.drawable.ic_book),
        BottomNavItem("download", R.drawable.ic_download),
        BottomNavItem("user", R.drawable.ic_user),
    )

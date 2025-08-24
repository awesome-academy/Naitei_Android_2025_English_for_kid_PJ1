package com.example.englishappforkid.presentation.base.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.englishappforkid.R

@Composable
fun backgroundImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.back_ground),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
}

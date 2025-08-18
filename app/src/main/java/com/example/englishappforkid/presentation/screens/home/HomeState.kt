package com.example.englishappforkid.presentation.screens.home

import com.example.englishappforkid.data.model.VideoItem

data class HomeState(
    val videos: List<VideoItem> = emptyList(),
    val isListView: Boolean = true,
)

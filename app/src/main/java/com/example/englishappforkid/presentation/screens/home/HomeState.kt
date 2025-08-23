package com.example.englishappforkid.presentation.screens.home

import com.example.englishappforkid.data.model.Topic

data class HomeState(
    val topics: List<Topic> = emptyList(),
    val isListView: Boolean = true,
)

package com.example.englishappforkid.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val repository = VideoRepository()

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadVideos()
    }

    private fun loadVideos() {
        _uiState.update { it.copy(videos = repository.getVideos()) }
    }

    fun toggleView() {
        _uiState.update { it.copy(isListView = !it.isListView) }
    }
}

package com.example.englishappforkid.presentation.screens.gametopic

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.repository.TopicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val repository = TopicRepository()

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTopics()
    }

    private fun loadTopics() {
        _uiState.update { it.copy(topics = repository.getTopics()) }
    }

    fun toggleView() {
        _uiState.update { it.copy(isListView = !it.isListView) }
    }
}

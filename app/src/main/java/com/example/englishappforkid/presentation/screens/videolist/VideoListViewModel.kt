package com.example.englishappforkid.presentation.screens.videolist

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.VideoDataSource
import com.example.englishappforkid.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoListViewModel : ViewModel() {
    private val _videos = MutableStateFlow<List<VideoItem>>(emptyList())
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _isListView = MutableStateFlow(true)
    val isListView: StateFlow<Boolean> = _isListView.asStateFlow()

    init {
        _videos.value = VideoDataSource.videoStories
    }

    fun toggleView() {
        _isListView.update { !it }
    }
}

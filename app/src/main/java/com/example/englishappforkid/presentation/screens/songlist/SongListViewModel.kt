package com.example.englishappforkid.presentation.screens.songlist

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.SongDataSource
import com.example.englishappforkid.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SongListViewModel : ViewModel() {
    private val _songs = MutableStateFlow<List<VideoItem>>(emptyList())
    val songs: StateFlow<List<VideoItem>> = _songs.asStateFlow()

    private val _isListView = MutableStateFlow(true) // Mặc định là ListView
    val isListView: StateFlow<Boolean> = _isListView.asStateFlow()

    init {
        _songs.value = SongDataSource.songs
    }

    fun toggleView() {
        _isListView.update { !it }
    }
}

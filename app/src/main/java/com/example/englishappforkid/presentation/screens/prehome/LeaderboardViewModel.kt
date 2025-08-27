package com.example.englishappforkid.ui.prehome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishappforkid.data.model.LeaderboardEntry
import com.example.englishappforkid.data.repository.LeaderboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: LeaderboardRepository = LeaderboardRepository(),
) : ViewModel() {
    private val _entries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val entries: StateFlow<List<LeaderboardEntry>> = _entries

    private val _topThree = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val topThree: StateFlow<List<LeaderboardEntry>> = _topThree

    private val _yourRank = MutableStateFlow<LeaderboardEntry?>(null)
    val yourRank: StateFlow<LeaderboardEntry?> = _yourRank

    fun loadLeaderboard() {
        viewModelScope.launch {
            val (allEntries, top3, yourEntry) = repository.getLeaderboardData()

            _entries.value = allEntries
            _topThree.value = top3
            _yourRank.value = yourEntry
        }
    }
}

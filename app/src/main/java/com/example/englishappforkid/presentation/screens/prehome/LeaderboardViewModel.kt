package com.example.englishappforkid.presentation.screens.prehome

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.model.LeaderboardEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LeaderboardViewModel : ViewModel() {
    // Dữ liệu giả lập
    private val _entries =
        MutableStateFlow(
            listOf(
                LeaderboardEntry(rank = 1, name = "Trương Ngọc Mai", isYou = false),
                LeaderboardEntry(rank = 2, name = "Trương Ngọc Mai", isYou = false),
                LeaderboardEntry(rank = 3, name = "Bạn", isYou = true),
            ),
        )
    val entries: StateFlow<List<LeaderboardEntry>> = _entries
}

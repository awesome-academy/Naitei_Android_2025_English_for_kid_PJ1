package com.example.englishappforkid.data.model

data class LeaderboardEntry(
    val uid: String = "",
    val rank: Int,
    val name: String,
    val isYou: Boolean,
    val score: Int,
    val avatar: String,
)

package com.example.englishappforkid.presentation.screens.game

data class GameUiState(
    val theme: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val lastAddedPoints: Int = 0,
    val userGuess: String = "",
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val availableCharacters: List<Char> = emptyList(),
    val scrambledWord: String = "", // Added to store scrambled word
)

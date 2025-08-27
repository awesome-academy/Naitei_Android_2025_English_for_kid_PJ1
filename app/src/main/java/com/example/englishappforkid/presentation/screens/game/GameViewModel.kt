package com.example.englishappforkid.presentation.screens.game

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.WordData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val MAX_NO_OF_WORDS = 10
const val GRID_SIZE = 25

class GameViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val wordData = WordData()

    private val _uiState = MutableStateFlow(GameUiState(theme = "Animals"))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String
    private var currentTheme: String = "Animals"

    init {
        resetGame()
    }

    fun setTheme(theme: String) {
        currentTheme = theme
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        val newWord = pickNewWord()
        _uiState.value =
            GameUiState(
                currentWordCount = 1,
                theme = currentTheme,
                availableCharacters = generateAvailableCharacters(newWord),
                scrambledWord =
                    newWord
                        .uppercase()
                        .toList()
                        .shuffled()
                        .joinToString(""),
            )
    }

    fun updateUserGuess(guessedWord: String) {
        _uiState.update { it.copy(userGuess = guessedWord, isGuessedWordWrong = false) }
    }

    fun checkUserGuess() {
        val guess = _uiState.value.userGuess
        if (guess.equals(currentWord, ignoreCase = true)) {
            val points = calculatePoints(currentWord)
            val updatedScore = _uiState.value.score + points
            updateGameState(updatedScore, points)
        } else {
            _uiState.update { it.copy(isGuessedWordWrong = true) }
        }
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
    }

    private fun updateGameState(
        updatedScore: Int,
        lastPoints: Int = 0,
    ) {
        if (usedWords.size >= MAX_NO_OF_WORDS) {
            _uiState.update {
                it.copy(
                    score = updatedScore,
                    lastAddedPoints = lastPoints,
                    isGameOver = true,
                    isGuessedWordWrong = false,
                )
            }
            saveScoreToFirestore(updatedScore)
        } else {
            val newWord = pickNewWord()
            _uiState.update {
                it.copy(
                    currentWordCount = it.currentWordCount + 1,
                    score = updatedScore,
                    lastAddedPoints = lastPoints,
                    availableCharacters = generateAvailableCharacters(newWord),
                    scrambledWord =
                        newWord
                            .uppercase()
                            .toList()
                            .shuffled()
                            .joinToString(""),
                    userGuess = "",
                    isGuessedWordWrong = false,
                )
            }
        }
    }

    private fun calculatePoints(word: String) =
        when (word.length) {
            in 0..3 -> 1
            in 4..5 -> 2
            in 6..7 -> 3
            else -> 4
        }

    private fun generateAvailableCharacters(word: String): List<Char> {
        val wordChars = word.uppercase().toMutableList()
        val alphabet = ('A'..'Z').toList()
        val randomChars = mutableListOf<Char>()
        while (wordChars.size + randomChars.size < GRID_SIZE) {
            randomChars.add(alphabet.random())
        }
        return (wordChars + randomChars).shuffled()
    }

    private fun pickNewWord(): String {
        val themeWords = wordData.themedWords[currentTheme]?.filterNot { usedWords.contains(it) } ?: emptyList()
        if (themeWords.isEmpty()) return "DEFAULT"
        currentWord = themeWords.random()
        usedWords.add(currentWord)
        return currentWord
    }

    private fun saveScoreToFirestore(scoreToAdd: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val uid = currentUser.uid

        val docRef = db.collection("leaderboard").document(uid)
        docRef
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Lấy điểm cũ
                    val oldScore = document.getLong("score") ?: 0
                    // Cộng thêm điểm mới
                    val newScore = oldScore + scoreToAdd
                    docRef
                        .update("score", newScore)
                        .addOnSuccessListener { println("Score updated for UID: $uid, newScore=$newScore") }
                        .addOnFailureListener { e -> println("Error updating score: $e") }
                } else {
                    // Nếu chưa có document, tạo mới
                    val entry =
                        mapOf(
                            "score" to scoreToAdd,
                            "isYou" to true,
                        )
                    docRef
                        .set(entry)
                        .addOnSuccessListener { println("Score created for UID: $uid, score=$scoreToAdd") }
                        .addOnFailureListener { e -> println("Error creating score: $e") }
                }
            }.addOnFailureListener { e ->
                println("Error fetching current score: $e")
            }
    }
}

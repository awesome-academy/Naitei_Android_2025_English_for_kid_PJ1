package com.example.englishappforkid.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.englishappforkid.R
import com.example.englishappforkid.presentation.base.navigation.ScreenRoutes
import com.example.englishappforkid.ui.theme.Cowbell
import com.example.englishappforkid.ui.theme.Pink80
import com.example.englishappforkid.ui.theme.boxBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gameScreen(
    navController: NavHostController,
    gameViewModel: GameViewModel = viewModel(),
    onExit: () -> Unit = { navController.navigate(ScreenRoutes.GAME) },
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val userGuess = gameUiState.userGuess

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(gameUiState.theme, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            gameProgress(currentWordCount = gameUiState.currentWordCount, totalWords = MAX_NO_OF_WORDS)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Pink80), // màu vàng nhạt
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Score: ${gameUiState.score}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cowbell,
                    )
                }
            }
            scrambledLettersDisplay(scrambledWord = gameUiState.scrambledWord)
            Spacer(modifier = Modifier.height(16.dp))

            wordDisplay(word = userGuess, pronunciation = "/$userGuess/")
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { gameViewModel.checkUserGuess() }) { Text(stringResource(R.string.check)) }
                Button(onClick = { gameViewModel.skipWord() }) { Text(stringResource(R.string.skip)) }
                IconButton(onClick = {
                    if (userGuess.isNotEmpty()) gameViewModel.updateUserGuess(userGuess.dropLast(1))
                }) {
                    Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = stringResource(R.string.delete))
                }
            }

            characterGrid(
                characters = gameUiState.availableCharacters.map { it.toString() },
                onCharClicked = { char -> gameViewModel.updateUserGuess(userGuess + char) },
            )

            Spacer(modifier = Modifier.weight(1f))

            if (userGuess.isNotEmpty()) {
                val feedbackText =
                    if (gameUiState.isGuessedWordWrong) {
                        stringResource(R.string.wrong)
                    } else {
                        stringResource(R.string.great)
                    }
                val feedbackColor =
                    if (gameUiState.isGuessedWordWrong) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                Text(feedbackText, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = feedbackColor)
            }
        }
    }

    if (gameUiState.isGameOver) {
        finalScoreDialog(
            score = gameUiState.score,
            onPlayAgain = { gameViewModel.resetGame() },
            onExit = onExit,
        )
    }
}

@Composable
fun scrambledLettersDisplay(scrambledWord: String) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(70.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scrambledWord.forEach { char ->
                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Cowbell),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = char.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
fun gameProgress(
    currentWordCount: Int,
    totalWords: Int,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.easy), style = MaterialTheme.typography.bodyMedium)
            Text("$currentWordCount/$totalWords", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { currentWordCount.toFloat() / totalWords },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
            color = Cowbell,
            trackColor = Color.LightGray,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
fun wordDisplay(
    word: String,
    pronunciation: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = boxBackground),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(word.uppercase(), fontSize = 32.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
            Text(pronunciation, fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Composable
fun characterGrid(
    characters: List<String>,
    onCharClicked: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(characters) { char ->
            val isVowel = char.uppercase() in "AEIOU"
            val backgroundColor = if (isVowel) Color(0xFFB3E5FC) else Color(0xFFFFF59D)
            val textColor = if (isVowel) Color(0xFF01579B) else Color(0xFFF57F17)
            Box(
                modifier =
                    Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(backgroundColor)
                        .clickable { onCharClicked(char) },
                contentAlignment = Alignment.Center,
            ) {
                Text(char, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    }
}

@Composable
private fun finalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.congratulations)) },
        text = { Text(stringResource(R.string.you_scored, score)) },
        dismissButton = {
            TextButton(onClick = onExit) { Text(stringResource(R.string.exit)) }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) { Text(stringResource(R.string.play_again)) }
        },
    )
}

package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultsScreen(
    score: Int,
    skips: Int,
    correctWords: List<String>,
    skippedWords: List<String>,
    onPlayAgain: () -> Unit,
    onChangeCategory: () -> Unit
) {
    var scoreVisible by remember { mutableStateOf(false) }
    val scoreScale by animateFloatAsState(
        targetValue = if (scoreVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "scoreScale"
    )
    LaunchedEffect(Unit) { scoreVisible = true }

    val total = score + skips
    val accuracy = if (total > 0) (score * 100 / total) else 0

    val message = when {
        score == 0 -> "Pole sana! 😅 Try again!"
        score < 3  -> "Si mbaya! 👍 Keep going!"
        score < 6  -> "Poa sana! 🔥 Well played!"
        score < 10 -> "Unachapisha! 🚀 Impressive!"
        else       -> "Wewe ni bingwa! 🏆 GOAT!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Round Over!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Animated score
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scoreScale)
        ) {
            Text(
                text = "$score",
                fontSize = 80.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "words guessed", fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = message, fontSize = 20.sp,
            fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "Skipped", value = "$skips", emoji = "⏭️")
            StatCard(label = "Accuracy", value = "$accuracy%", emoji = "🎯")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Correct words
        if (correctWords.isNotEmpty()) {
            WordListSection(
                title = "✅ Got Right (${correctWords.size})",
                words = correctWords,
                chipColor = MaterialTheme.colorScheme.primaryContainer,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Skipped words
        if (skippedWords.isNotEmpty()) {
            WordListSection(
                title = "⏭️ Skipped (${skippedWords.size})",
                words = skippedWords,
                chipColor = MaterialTheme.colorScheme.errorContainer,
                textColor = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Buttons
        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Play Again 🔄", fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onChangeCategory,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Change Category 🏠", fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordListSection(
    title: String,
    words: List<String>,
    chipColor: Color,
    textColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp))

        // Wrap chips in a flowing row
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            words.forEach { word ->
                Surface(
                    color = chipColor,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = word,
                        color = textColor,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, emoji: String) {
    ElevatedCard(modifier = Modifier.width(140.dp)) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = label, fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
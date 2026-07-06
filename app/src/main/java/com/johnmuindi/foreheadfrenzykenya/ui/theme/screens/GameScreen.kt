package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johnmuindi.foreheadfrenzykenya.data.WordBank
import com.johnmuindi.foreheadfrenzykenya.sensor.SoundManager
import com.johnmuindi.foreheadfrenzykenya.sensor.TiltDetector
import com.johnmuindi.foreheadfrenzykenya.viewmodel.GameViewModel
import com.johnmuindi.foreheadfrenzykenya.viewmodel.RoundState

@Composable
fun GameScreen(
    categoryId: String,
    duration: Int,
    onRoundEnd: (score: Int, skips: Int) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val category = remember(categoryId) { WordBank.getCategoryById(categoryId) }
    val soundManager = remember { SoundManager(context) }

    // Force landscape and restore when leaving
    DisposableEffect(Unit) {
        val original = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onDispose {
            activity.requestedOrientation = original
        }
    }

    LaunchedEffect(categoryId) {
        category?.let { viewModel.startRound(it, duration) }
    }

    LaunchedEffect(viewModel.roundState) {
        if (viewModel.roundState == RoundState.ENDED) {
            soundManager.playRoundEnd()
            onRoundEnd(viewModel.score, viewModel.skips)
        }
    }

    DisposableEffect("sensor") {
        val tiltDetector = TiltDetector(
            context = context,
            onTiltForward = {
                soundManager.playCorrect()
                viewModel.markCorrect()
            },
            onTiltBackward = {
                soundManager.playSkip()
                viewModel.markSkipped()
            }
        )
        tiltDetector.start()
        onDispose {
            tiltDetector.stop()
            soundManager.release()
        }
    }

    val timerColor = if (viewModel.timeLeft <= 10) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.onSurface

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⏱ ${viewModel.timeLeft}s",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = timerColor
                )
                Text(
                    text = "✅ ${viewModel.score}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Word display — smaller font + padding so long words don't overflow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.currentWord,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 50.sp,
                    maxLines = 2
                )
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        soundManager.playSkip()
                        viewModel.markSkipped()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.width(140.dp).height(52.dp)
                ) {
                    Text("Skip ⏭️", fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        soundManager.playCorrect()
                        viewModel.markCorrect()
                    },
                    modifier = Modifier.width(140.dp).height(52.dp)
                ) {
                    Text("Got it! ✅", fontSize = 16.sp)
                }
            }
        }

        // Countdown overlay
        if (viewModel.roundState == RoundState.COUNTDOWN) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📱 Put phone on forehead!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                    AnimatedContent(
                        targetState = viewModel.countdownValue,
                        transitionSpec = {
                            scaleIn(animationSpec = tween(300)) togetherWith
                                    scaleOut(animationSpec = tween(200))
                        },
                        label = "countdown"
                    ) { value ->
                        Text(
                            text = if (value == 0) "GO! 🚀" else "$value",
                            fontSize = if (value == 0) 64.sp else 96.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (value == 0) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
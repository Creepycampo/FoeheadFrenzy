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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johnmuindi.foreheadfrenzykenya.data.WordBank
import com.johnmuindi.foreheadfrenzykenya.sensor.SoundManager
import com.johnmuindi.foreheadfrenzykenya.sensor.TiltDetector
import com.johnmuindi.foreheadfrenzykenya.ui.theme.ForeheadFrenzyKenyaTheme
import com.johnmuindi.foreheadfrenzykenya.viewmodel.GameViewModel
import com.johnmuindi.foreheadfrenzykenya.viewmodel.RoundState

private val rainbowColors = listOf(
    Color(0xFFFF0080),
    Color(0xFFFF4500),
    Color(0xFFFFD700),
    Color(0xFF00FF80),
    Color(0xFF00BFFF),
    Color(0xFF8000FF),
    Color(0xFFFF0080),
)

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

    // Force landscape, restore orientation on leaving screen
    DisposableEffect(Unit) {
        val original = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose { activity.requestedOrientation = original }
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

    // Calibration state — shown in countdown overlay
    var isCalibrated by remember { mutableStateOf(false) }

    DisposableEffect("sensor") {
        val tiltDetector = TiltDetector(
            context        = context,
            onTiltForward  = { soundManager.playCorrect(); viewModel.markCorrect() },
            onTiltBackward = { soundManager.playSkip(); viewModel.markSkipped() },
            onCalibrated   = { isCalibrated = true }
        )
        tiltDetector.start()
        onDispose {
            tiltDetector.stop()
            soundManager.release()
        }
    }

    val timerColor = if (viewModel.timeLeft <= 10) Color(0xFFEF4444) else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF050510)),
                    radius = 1800f
                )
            )
    ) {
        // ── Main game UI ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Top row: timer | flag | score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⏱  ${viewModel.timeLeft}s",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = timerColor
                    )
                }

                Text("🇰🇪", fontSize = 28.sp)

                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⭐  ${viewModel.score}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // ── Rainbow gradient border word card ──
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .fillMaxHeight(0.82f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Brush.linearGradient(rainbowColors))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color(0xFF0A0A1E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.currentWord,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            lineHeight = 54.sp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }

            // Bottom row: Skip | hint | Got It
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { soundManager.playSkip(); viewModel.markSkipped() },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier.width(130.dp).height(52.dp)
                ) {
                    Text(
                        text = "SKIP  ⏭",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Hold the phone on\nyour forehead so\nyour friend can see!",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Button(
                    onClick = { soundManager.playCorrect(); viewModel.markCorrect() },
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    modifier = Modifier.width(130.dp).height(52.dp)
                ) {
                    Text(
                        text = "GOT IT!  ✅",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        // ── Countdown overlay — sits on top until PLAYING ──
        if (viewModel.roundState == RoundState.COUNTDOWN) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050510).copy(alpha = 0.96f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "📱 Put phone on forehead!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    AnimatedContent(
                        targetState = viewModel.countdownValue,
                        transitionSpec = {
                            scaleIn(tween(300)) togetherWith scaleOut(tween(200))
                        },
                        label = "countdown"
                    ) { value ->
                        Text(
                            text = if (value == 0) "GO! 🚀" else "$value",
                            fontSize = if (value == 0) 64.sp else 96.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (value == 0) Color(0xFF22C55E) else Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sensor calibration status
                    Box(
                        modifier = Modifier
                            .background(
                                if (isCalibrated) Color(0xFF16A34A).copy(alpha = 0.25f)
                                else Color.White.copy(alpha = 0.08f),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isCalibrated) "✅ Sensors ready!"
                            else "📡 Calibrating sensors...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isCalibrated) Color(0xFF4ADE80)
                            else Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// ── Preview ──
@Preview(
    showBackground = true,
    widthDp = 851,
    heightDp = 393
)
@Composable
fun GameScreenPreview() {
    ForeheadFrenzyKenyaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF1A1A2E), Color(0xFF050510)),
                        radius = 1800f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("⏱  42s", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Text("🇰🇪", fontSize = 28.sp)

                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("⭐  5", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .fillMaxHeight(0.82f)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Brush.linearGradient(rainbowColors))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(25.dp))
                                .background(Color(0xFF0A0A1E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nyashinski",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        modifier = Modifier.width(130.dp).height(52.dp)
                    ) {
                        Text("SKIP  ⏭", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }

                    Text(
                        text = "Hold the phone on\nyour forehead so\nyour friend can see!",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        modifier = Modifier.width(130.dp).height(52.dp)
                    ) {
                        Text("GOT IT!  ✅", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}
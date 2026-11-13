package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johnmuindi.foreheadfrenzykenya.ui.theme.ForeheadFrenzyKenyaTheme
import kotlinx.coroutines.delay

// Each step in the how-to-play demo
private data class GameStep(
    val emoji: String,
    val title: String,
    val description: String,
    val demoWord: String?,       // word shown on the fake phone screen
    val demoColor: Color,        // background color of fake phone screen
    val actionHint: String?      // small hint shown below phone e.g. "Tilt forward ✅"
)

private val steps = listOf(
    GameStep(
        emoji = "🎯",
        title = "Pick a Category",
        description = "Choose from Kenyan Cities, Animals, Celebrities, Slang and more.",
        demoWord = null,
        demoColor = Color(0xFF1A1A2E),
        actionHint = null
    ),
    GameStep(
        emoji = "📱",
        title = "Phone on Your Forehead",
        description = "Hold the phone flat against your forehead, screen facing outward so friends can see the word — but you can't!",
        demoWord = "Nairobi",
        demoColor = Color(0xFF1A3A5C),
        actionHint = "Screen faces out 👥"
    ),
    GameStep(
        emoji = "🗣️",
        title = "Friends Give Clues",
        description = "Your friends describe, act out, or give hints about the word — without saying it directly!",
        demoWord = "Nyashinski",
        demoColor = Color(0xFF1A0533),
        actionHint = "\"He's a famous Kenyan rapper...\" 🎤"
    ),
    GameStep(
        emoji = "✅",
        title = "Tilt Forward = Got It!",
        description = "When you guess correctly, tilt the phone forward (chin down). Score goes up!",
        demoWord = "Gor Mahia",
        demoColor = Color(0xFF0D2818),
        actionHint = "Chin down ↓  = GOT IT ✅"
    ),
    GameStep(
        emoji = "⏭️",
        title = "Tilt Backward = Skip",
        description = "Stuck? Tilt the phone backward (chin up) to skip the word and move on.",
        demoWord = "Mursik",
        demoColor = Color(0xFF2A0A00),
        actionHint = "Chin up ↑  = SKIP ⏭️"
    ),
    GameStep(
        emoji = "⏱️",
        title = "Beat the Clock!",
        description = "Score as many words as you can before the timer hits zero. Most points wins!",
        demoWord = "Khaligraph",
        demoColor = Color(0xFF2C1810),
        actionHint = "Time's almost up! ⏱️"
    ),
)

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableIntStateOf(0) }

    // Auto-advance the demo every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000L)
            currentStep = (currentStep + 1) % steps.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF050510))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Top bar ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = com.johnmuindi.foreheadfrenzykenya.R.drawable.img),
                        contentDescription = "Back",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "How to Play",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Text(
                text = "Simple · Fun · Kenyan 🇰🇪",
                fontSize = 14.sp,
                color = Color(0xFFFBB308),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ── Animated demo card ──
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    (fadeIn(tween(400)) + scaleIn(tween(400), initialScale = 0.92f))
                        .togetherWith(fadeOut(tween(300)) + scaleOut(tween(300), targetScale = 1.05f))
                },
                label = "step_demo"
            ) { stepIndex ->
                val step = steps[stepIndex]
                DemoCard(step = step)
            }

            Spacer(Modifier.height(16.dp))

            // ── Progress dots ──
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, _ ->
                    val isActive = index == currentStep
                    Box(
                        modifier = Modifier
                            .size(if (isActive) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) Color(0xFFFBB308)
                                else Color.White.copy(alpha = 0.25f)
                            )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Quick rules list ──
            Text(
                text = "Quick Rules",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            steps.forEachIndexed { index, step ->
                QuickRuleRow(
                    number = index + 1,
                    emoji = step.emoji,
                    title = step.title,
                    description = step.description
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(28.dp))

            // ── Pro tips ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFBB308).copy(alpha = 0.10f))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "💡 Pro Tips",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFBB308),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    listOf(
                        "Hold the phone flat not tilted — when calibrating",
                        "Wait for 'Sensors Ready ✅' before the game starts",
                        "30s mode is great for loud party rounds",
                        "Word Mixer adds extra chaos — try it with Slang + Cities!",
                        "Speak Swahili clues for extra difficulty 😄"
                    ).forEach { tip ->
                        Row(
                            modifier = Modifier.padding(bottom = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                color = Color(0xFFFBB308),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 8.dp, top = 1.dp)
                            )
                            Text(
                                text = tip,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.75f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))


            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF16A34A)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "♥L♥E♥T♥'S  P♥L♥A♥Y \uD83C\uDFAE ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

// ── The fake "game screen" demo card for each step ──
@Composable
private fun DemoCard(step: GameStep) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = step.emoji, fontSize = 40.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            text = step.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = step.description,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )

        // Only render mock phone if this step has a demo word
        if (step.demoWord != null) {
            Spacer(Modifier.height(20.dp))

            // Fake phone screen showing the word
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFFF0080), Color(0xFFFF4500),
                                Color(0xFFFFD700), Color(0xFF00FF80),
                                Color(0xFF00BFFF), Color(0xFF8000FF),
                                Color(0xFFFF0080)
                            )
                        )
                    )
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(step.demoColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = step.demoWord,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            step.actionHint?.let { hint ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = hint,
                    fontSize = 12.sp,
                    color = Color(0xFFFBB308),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ── Single rule row in the quick rules list ──
@Composable
private fun QuickRuleRow(
    number: Int,
    emoji: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Step number badge
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFFBB308).copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$number",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFBB308)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 16.sp)
                Spacer(Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f),
                lineHeight = 17.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
fun HowToPlayPreview() {
    ForeheadFrenzyKenyaTheme {
        HowToPlayScreen(onBack = {})
    }
}
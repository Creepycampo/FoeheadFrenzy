package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johnmuindi.foreheadfrenzykenya.data.WordBank
import com.johnmuindi.foreheadfrenzykenya.viewmodel.GameViewModel

private val categoryGradients = mapOf(
    "cities"      to listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)),
    "animals"     to listOf(Color(0xFF2C1810), Color(0xFF6B3410), Color(0xFFD4571A)),
    "celebrities" to listOf(Color(0xFF1A0533), Color(0xFF6B0F7A), Color(0xFFEC4899)),
    "slang"       to listOf(Color(0xFF0D2818), Color(0xFF1A5C2A), Color(0xFF22C55E)),
    "football"    to listOf(Color(0xFF0A1628), Color(0xFF1A3A5C), Color(0xFF3B82F6)),
    "food"        to listOf(Color(0xFF2A0A00), Color(0xFF7B2D00), Color(0xFFEA580C)),
)

@Composable
fun CategoryDetailScreen(
    categoryId: String,
    duration: Int,
    gameViewModel: GameViewModel,
    onPlay: (categoryId: String, duration: Int) -> Unit,
    onBack: () -> Unit
) {
    val category = remember(categoryId) { WordBank.getCategoryById(categoryId) } ?: return
    val otherCategories = remember(categoryId) {
        WordBank.categories.filter { it.id != categoryId }
    }
    val categoryGradient = categoryGradients[categoryId]
        ?: listOf(Color(0xFF1A1A2E), Color(0xFF16213E))

    var showComingSoon by remember { mutableStateOf(false) }
    var mixExpanded by remember { mutableStateOf(false) }
    val selectedMixIds = remember { mutableStateListOf<String>() }

    // Live total word count across selected mix categories
    val totalMixWords = remember(selectedMixIds.toList()) {
        category.words.size + selectedMixIds.sumOf { id ->
            WordBank.getCategoryById(id)?.words?.size ?: 0
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
                .padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(bottom = 20.dp)
            ) {
                Text("←", fontSize = 22.sp, color = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Categories",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.65f),
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(categoryGradient)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = category.emoji, fontSize = 52.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = category.name.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "${category.words.size} words",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // ── Option 1: Play Normal ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("▶", fontSize = 22.sp, color = Color(0xFF22C55E))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Play ${category.name}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Standard · ${category.words.size} words · ${duration}s",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.45f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onPlay(categoryId, duration) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF16A34A)
                    )
                ) {
                    Text(
                        text = "▶   START GAME",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.07f),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { showComingSoon = true }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🖼️", fontSize = 22.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Picture Mode",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White.copy(alpha = 0.4f)
                            )
                            Text(
                                text = "Guess from images",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.25f)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF374151),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🔒  Soon",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.45f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Option 3: Word Mixer ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(
                        1.5.dp,
                        Color(0xFFFBB308).copy(alpha = if (mixExpanded) 0.5f else 0.2f),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                // Header — tap to expand/collapse
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { mixExpanded = !mixExpanded }
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎲", fontSize = 22.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Word Mixer",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFBB308)
                            )
                            Text(
                                text = "Mix words from other categories",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.45f)
                            )
                        }
                    }
                    Text(
                        text = if (mixExpanded) "▲" else "▼",
                        fontSize = 14.sp,
                        color = Color(0xFFFBB308)
                    )
                }

                // Expandable content
                AnimatedVisibility(
                    visible = mixExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 20.dp, end = 20.dp, bottom = 20.dp
                        )
                    ) {
                        Text(
                            text = "Scroll and tap categories to add:",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.55f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Horizontal scrollable category chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(otherCategories) { cat ->
                                val isSelected = selectedMixIds.contains(cat.id)
                                val chipGradient = categoryGradients[cat.id]
                                    ?: listOf(Color(0xFF1A1A2E), Color(0xFF16213E))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (isSelected)
                                                Brush.linearGradient(chipGradient)
                                            else
                                                Brush.linearGradient(
                                                    listOf(
                                                        Color.White.copy(0.08f),
                                                        Color.White.copy(0.08f)
                                                    )
                                                )
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFFBB308)
                                            else Color.White.copy(0.12f),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            if (isSelected) selectedMixIds.remove(cat.id)
                                            else selectedMixIds.add(cat.id)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 12.dp)
                                        .width(80.dp)
                                ) {
                                    Text(cat.emoji, fontSize = 28.sp)
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = cat.name.split(" ").first(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) Color.White
                                        else Color.White.copy(0.5f),
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                    if (isSelected) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = "✓",
                                            fontSize = 11.sp,
                                            color = Color(0xFFFBB308),
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }

                        // Live word count
                        if (selectedMixIds.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📊", fontSize = 14.sp)
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "$totalMixWords total words in mix",
                                    fontSize = 13.sp,
                                    color = Color(0xFFFBB308),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Start Mix button
                        Button(
                            onClick = {
                                val mixedWords = buildList {
                                    addAll(category.words)
                                    selectedMixIds.forEach { id ->
                                        WordBank.getCategoryById(id)?.words?.let { addAll(it) }
                                    }
                                }
                                gameViewModel.prepareMixedRound(mixedWords)
                                onPlay(categoryId, duration)
                            },
                            enabled = selectedMixIds.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFBB308),
                                disabledContainerColor = Color(0xFFFBB308).copy(alpha = 0.25f)
                            )
                        ) {
                            Text(
                                text = if (selectedMixIds.isEmpty()) "Select categories above first"
                                else "🎲   START MIX · $totalMixWords words",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedMixIds.isEmpty()) Color.White.copy(0.35f)
                                else Color(0xFF1A1A2E)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        // ── Coming Soon dialog ──
        if (showComingSoon) {
            AlertDialog(
                onDismissRequest = { showComingSoon = false },
                containerColor = Color(0xFF1A1A2E),
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "🔒 Coming Soon!",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Text(
                        text = "Picture Mode is currently in production.\n\nWe'll update you the moment it's ready! 🎨🚀",
                        color = Color.White.copy(alpha = 0.7f),
                        lineHeight = 22.sp,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showComingSoon = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Got it! 👍",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

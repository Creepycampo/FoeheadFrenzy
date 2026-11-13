package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.johnmuindi.foreheadfrenzykenya.data.Category
import com.johnmuindi.foreheadfrenzykenya.data.WordBank

private data class TimerOption(val seconds: Int, val label: String, val color: Color)

private val timerOptions = listOf(
    TimerOption(30, "30s 🔥\nQuick",  Color(0xFFEC4899)),
    TimerOption(60, "60s ⚡\nNormal", Color(0xFF16A34A)),
    TimerOption(90, "90s 😎\nChill",  Color(0xFF7C3AED)),
)

private val categoryGradients = mapOf(
    "cities"      to listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)),
    "animals"     to listOf(Color(0xFF2C1810), Color(0xFF6B3410), Color(0xFFD4571A)),
    "celebrities" to listOf(Color(0xFF1A0533), Color(0xFF6B0F7A), Color(0xFFEC4899)),
    "slang"       to listOf(Color(0xFF0D2818), Color(0xFF1A5C2A), Color(0xFF22C55E)),
    "football"    to listOf(Color(0xFF0A1628), Color(0xFF1A3A5C), Color(0xFF3B82F6)),
    "food"        to listOf(Color(0xFF2A0A00), Color(0xFF7B2D00), Color(0xFFEA580C)),
)

@Composable
fun CategoryPickerScreen(onCategorySelected: (Category, Int) -> Unit) {
    var selectedDuration by remember { mutableStateOf(60) }

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
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "⚡  CHOOSE TOPIC  ⚡",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            // Round duration label
            Text(
                text = "ROUND DURATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Duration chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                timerOptions.forEach { option ->
                    val isSelected = selectedDuration == option.seconds
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) option.color
                                else Color.White.copy(alpha = 0.08f)
                            )
                            .clickable { selectedDuration = option.seconds },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) Color.White
                            else Color.White.copy(alpha = 0.45f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Category grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(WordBank.categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategorySelected(category, selectedDuration) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    val gradientColors = categoryGradients[category.id]
        ?: listOf(Color(0xFF1A1A2E), Color(0xFF16213E))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(gradientColors))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = category.emoji, fontSize = 38.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                lineHeight = 17.sp
            )
        }
    }
}
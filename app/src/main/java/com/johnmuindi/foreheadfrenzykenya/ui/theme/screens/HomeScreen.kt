package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johnmuindi.foreheadfrenzykenya.R
import com.johnmuindi.foreheadfrenzykenya.ui.theme.ForeheadFrenzyKenyaTheme

@Composable
fun HomeScreen(onPlayClick: () -> Unit,
               onHowToPlayClick: () -> Unit ) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Your photo fills the entire screen ──
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ── Dark gradient at the bottom so buttons are readable ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF0D0D2B).copy(alpha = 0.6f),
                            Color(0xFF0D0D2B).copy(alpha = 0.92f),
                            Color(0xFF0D0D2B)
                        )
                    )
                )
        )

        // ── Buttons and feature icons pinned to the bottom ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            // PLAY NOW — big yellow pill
            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBB308)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                Text(
                    text = "▶  PLAY NOW",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A2E),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // HOW TO PLAY — dark outlined pill
            OutlinedButton(
                onClick = onHowToPlayClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.35f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFF1A1A3E).copy(alpha = 0.75f),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "👥   HOW TO PLAY",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Three feature icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeatureItem(
                    emoji = "",
                    label = "FUN WITH\nFRIENDS",
                    accentColor = Color(0xFFEC4899)
                )
                FeatureItem(
                    emoji = "🎯",
                    label = "TONS OF\nCATEGORIES",
                    accentColor = Color(0xFF3B82F6)
                )
                FeatureItem(
                    emoji = "🏆",
                    label = "KEEP SCORE\n& COMPETE",
                    accentColor = Color(0xFFFBB308)
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(
    emoji: String,
    label: String,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(accentColor, CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 26.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 851)
@Composable
fun HomeScreenPreview() {
    ForeheadFrenzyKenyaTheme {
        HomeScreen(
            onPlayClick = {},
            onHowToPlayClick = TODO()
        )
    }
}
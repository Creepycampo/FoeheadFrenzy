package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.johnmuindi.foreheadfrenzykenya.data.WordBank
import com.johnmuindi.foreheadfrenzykenya.sensor.TiltDetector
import com.johnmuindi.foreheadfrenzykenya.viewmodel.GameViewModel
import com.johnmuindi.foreheadfrenzykenya.viewmodel.RoundState

@Composable
fun  GameScreen (
    categoryId: String,
    onRoundEnd: ()-> Unit,
    viewModel: GameViewModel = viewModel()
){
    val category = remember (categoryId) {WordBank.getCategoryById(categoryId)  }

    LaunchedEffect(categoryId) {
        category?.let { viewModel.startRound(it) }
    }
    LaunchedEffect(viewModel.roundState) {
        if (viewModel.roundState == RoundState.ENDED) {
            onRoundEnd()
        }
    }
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val tiltDetector = TiltDetector(
            context = context,
            onTiltForward = { viewModel.markCorrect() },
            onTiltBackward = { viewModel.markSkipped() }
        )
        tiltDetector.start()
        onDispose { tiltDetector.stop() }
    }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "⏱ ${viewModel.timeLeft}s", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "✅ ${viewModel.score}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = viewModel.currentWord,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(onClick = {viewModel.markSkipped() }) {
                Text("Skip ⏭\uFE0F")
            }
            Button(onClick = { viewModel.markCorrect() }) {
                Text("Got it! ✅")
            }
        }
    }
}
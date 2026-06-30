package com.johnmuindi.foreheadfrenzykenya.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen (onPlayClick: () -> Unit){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "ke FOREHEAD FRENZY",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "KENYAN EDITION",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onPlayClick,
            modifier = Modifier
                .fillMaxSize()
                .height(56.dp)
        ) {
            Text(
                text = "PLAY",
                fontSize = 20.sp
            )
        }

    }
}
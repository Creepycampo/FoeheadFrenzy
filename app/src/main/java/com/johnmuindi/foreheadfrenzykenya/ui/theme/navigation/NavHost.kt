package com.johnmuindi.foreheadfrenzykenya.ui.theme.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme{
                Surface(modifier = Modifier.fillMaxSize()){
                    val navController = rememberNavController()
                    NavHost(navController = navController,startDestination = Screen.Home.route){
                        composable(Screen.Home.route){
                            HomeScreen(onPlayClick = {
                                navController.navigate(Screen.CategoryPicker.route)
                            }
                            )
                        }
                    }
                }
            }
        }
    }
}
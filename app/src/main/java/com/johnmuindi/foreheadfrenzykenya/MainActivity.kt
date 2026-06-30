package com.johnmuindi.foreheadfrenzykenya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.johnmuindi.foreheadfrenzykenya.ui.theme.ForeheadFrenzyKenyaTheme
import com.johnmuindi.foreheadfrenzykenya.ui.theme.navigation.Screen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.CategoryPickerScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.GameScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForeheadFrenzyKenyaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onPlayClick = {
                                    navController.navigate(Screen.CategoryPicker.route)
                                }
                            )
                        }
                        composable (Screen.CategoryPicker.route){
                            CategoryPickerScreen (onCategorySelected = {category ->
                                navController.navigate(Screen.Game.createRoute(category.id))
                            })
                        }
                        composable(
                            route =Screen.Game.route,
                            arguments = listOf(navArgument("categoryId"){type = NavType.StringType})
                        ) {
                            backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")?: return@composable
                            GameScreen(
                                categoryId = categoryId,
                                onRoundEnd = {
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ForeheadFrenzyKenyaTheme {
        Greeting("Android")
    }
}
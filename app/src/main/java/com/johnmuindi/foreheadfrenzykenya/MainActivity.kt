package com.johnmuindi.foreheadfrenzykenya

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.HowToPlayScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.ResultsScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.HomeScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.GameScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.CategoryPickerScreen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.ForeheadFrenzyKenyaTheme
import com.johnmuindi.foreheadfrenzykenya.ui.theme.navigation.Screen
import com.johnmuindi.foreheadfrenzykenya.ui.theme.screens.CategoryDetailScreen
import com.johnmuindi.foreheadfrenzykenya.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
            ForeheadFrenzyKenyaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    // Activity-scoped — survives navigation between Game and Results
                    val gameViewModel: GameViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {

                        composable(Screen.Home.route) {
                            HomeScreen(
                                onPlayClick = { navController.navigate(Screen.CategoryPicker.route) },
                                onHowToPlayClick = { navController.navigate(Screen.HowToPlay.route) }
                            )
                        }
                        composable(Screen.CategoryPicker.route) {
                            CategoryPickerScreen(
                                onCategorySelected = { category, duration ->
                                    navController.navigate(
                                        Screen.CategoryDetail.createRoute(category.id, duration)
                                    )
                                }
                            )
                        }
                        composable(
                            route = Screen.CategoryDetail.route,
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("duration") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
                            val duration = backStackEntry.arguments?.getInt("duration") ?: 60
                            CategoryDetailScreen(
                                categoryId = categoryId,
                                duration = duration,
                                gameViewModel = gameViewModel,
                                onPlay = { catId, dur ->
                                    navController.navigate(Screen.Game.createRoute(catId, dur))
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Game.route,
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("duration") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
                            val duration = backStackEntry.arguments?.getInt("duration") ?: 60
                            GameScreen(
                                categoryId = categoryId,
                                duration = duration,
                                onRoundEnd = { score, skips ->
                                    navController.navigate(
                                        Screen.Results.createRoute(score, skips, categoryId, duration)
                                    ) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
                                },
                                viewModel = gameViewModel
                            )
                        }
                        composable(Screen.HowToPlay.route) {
                            HowToPlayScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.Results.route,
                            arguments = listOf(
                                navArgument("score") { type = NavType.IntType },
                                navArgument("skips") { type = NavType.IntType },
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("duration") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val score = backStackEntry.arguments?.getInt("score") ?: 0
                            val skips = backStackEntry.arguments?.getInt("skips") ?: 0
                            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                            val duration = backStackEntry.arguments?.getInt("duration") ?: 60

                            ResultsScreen(
                                score = score,
                                skips = skips,
                                correctWords = gameViewModel.correctWords,
                                skippedWords = gameViewModel.skippedWords,
                                onPlayAgain = {
                                    // Same category, same duration, fresh round
                                    navController.navigate(
                                        Screen.Game.createRoute(categoryId, duration)
                                    ) {
                                        popUpTo(Screen.Results.route) { inclusive = true }
                                    }
                                },
                                onChangeCategory = {
                                    // Go to category picker, keep Home in stack
                                    navController.navigate(Screen.CategoryPicker.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
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
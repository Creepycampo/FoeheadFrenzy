package com.johnmuindi.foreheadfrenzykenya.ui.theme.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CategoryPicker : Screen("category_picker")
    object Game : Screen("game/{categoryId}/{duration}") {
        fun createRoute(categoryId: String, duration: Int) = "game/$categoryId/$duration"
    }
    object Results : Screen("results/{score}/{skips}/{categoryId}/{duration}") {
        fun createRoute(score: Int, skips: Int, categoryId: String, duration: Int) =
            "results/$score/$skips/$categoryId/$duration"
    }
}
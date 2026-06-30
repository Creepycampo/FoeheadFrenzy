package com.johnmuindi.foreheadfrenzykenya.ui.theme.navigation

sealed class Screen (val route: String){
    object Home : Screen("Home")
    object CategoryPicker : Screen ("category_picker")
    object Game : Screen ("game/{categoryId}")
    fun createRoute(categoryId : String) = "game/$categoryId"
}
package com.example.flashcardapp.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcardapp.ui.view.DeckScreen
import com.example.flashcardapp.ui.view.DecksScreen
import com.example.flashcardapp.ui.view.LoginScreen

@Composable
fun Navigation(windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = LoginDestination.route) {
        composable(DecksDestination.route) { DecksScreen(navController) }
        composable(DeckDestination.route) { DeckScreen(navController, windowSize) }
        composable(LoginDestination.route) { LoginScreen(navController) }
    }

}
package com.example.flashcardapp.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcardapp.common.slideInFadeInFromLeft
import com.example.flashcardapp.common.slideInFadeInFromRight
import com.example.flashcardapp.common.slideOutFadeOutFromLeft
import com.example.flashcardapp.common.slideOutFadeOutFromRight
import com.example.flashcardapp.ui.view.DeckScreen
import com.example.flashcardapp.ui.view.DecksScreen
import com.example.flashcardapp.ui.view.auth.LoginScreen
import com.example.flashcardapp.ui.view.auth.RegisterScreen

@Composable
fun Navigation(windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = LoginDestination.route) {
        composable(LoginDestination.route, enterTransition = {
            slideInFadeInFromRight()
        }, exitTransition = {
            slideOutFadeOutFromLeft()
        }) { LoginScreen(navController) }
        composable(RegisterDestination.route, enterTransition = {
            slideInFadeInFromLeft()
        }, exitTransition = {
            slideOutFadeOutFromRight()
        }) { RegisterScreen(navController) }
        composable(DecksDestination.route) { DecksScreen(navController) }
        composable(DeckDestination.route) { DeckScreen(navController, windowSize) }

    }
}
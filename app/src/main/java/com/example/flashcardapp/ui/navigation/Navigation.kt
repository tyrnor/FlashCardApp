package com.example.flashcardapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flashcardapp.common.NavigationDirection
import com.example.flashcardapp.common.slideInFadeInFromLeft
import com.example.flashcardapp.common.slideInFadeInFromRight
import com.example.flashcardapp.common.slideOutFadeOutFromLeft
import com.example.flashcardapp.common.slideOutFadeOutFromRight
import com.example.flashcardapp.presentation.NavigationViewModel
import com.example.flashcardapp.ui.theme.AppTheme
import com.example.flashcardapp.ui.view.DeckScreen
import com.example.flashcardapp.ui.view.DecksScreen
import com.example.flashcardapp.ui.view.EditDeckScreen
import com.example.flashcardapp.ui.view.auth.LoginScreen
import com.example.flashcardapp.ui.view.auth.RegisterScreen

@Composable
fun Navigation(windowSize: WindowSizeClass) {
    val navController = rememberNavController()
    val navigationViewModel: NavigationViewModel = hiltViewModel()

    NavHost(
        modifier = Modifier
            .background(AppTheme.colorScheme.background)
            .fillMaxSize(),
        navController = navController,
        startDestination = LoginDestination.route
    ) {
        composable(
            LoginDestination.route,
            enterTransition = {
                slideInFadeInFromRight()
            },
            exitTransition = {
                slideOutFadeOutFromLeft()
            }) { LoginScreen(navController, navigationViewModel) }
        composable(
            RegisterDestination.route,
            enterTransition = {
                slideInFadeInFromLeft()
            },
            exitTransition = {
                slideOutFadeOutFromRight()
            }) { RegisterScreen(navController) }
        composable(
            DecksDestination.route,
            enterTransition = {
                if (navigationViewModel.navigationDirection.value == NavigationDirection.LEFT_TO_RIGHT) {
                    slideInFadeInFromLeft()
                } else {
                    slideInFadeInFromRight()
                }
            },
            exitTransition = {
                slideOutFadeOutFromLeft()
            }) { DecksScreen(navController, navigationViewModel) }
        composable(
            DeckDestination.route + "/{deckId}",
            arguments = listOf(navArgument("deckId") { type = NavType.StringType }),
            enterTransition = {
                slideInFadeInFromLeft()
            },
            exitTransition = {
                slideOutFadeOutFromRight()
            }) { DeckScreen(deckId = it.arguments?.getString("deckId"), navController, windowSize) }
        composable(
            route = EditDeckDestination.route + "/{deckId}",
            arguments = listOf(navArgument("deckId") { type = NavType.StringType }),
            enterTransition = {
                slideInFadeInFromLeft()
            },
            exitTransition = {
                slideOutFadeOutFromRight()
            }
        ) { EditDeckScreen(deckId = it.arguments?.getString("deckId"), navController) }

    }
}
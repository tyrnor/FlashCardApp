package com.example.flashcardapp.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.domain.LoginState
import com.example.flashcardapp.presentation.AuthViewModel
import com.example.flashcardapp.ui.navigation.DecksDestination

@Composable
fun LoginScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(true) {
        authViewModel.login("test@test.com", "test123")
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(DecksDestination.route)
            }
            is LoginState.Error -> {
                // Handle error
            }
            else -> {

            }
        }
    }
}
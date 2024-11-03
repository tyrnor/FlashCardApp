package com.example.flashcardapp.ui.view.auth

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.NavigationDirection
import com.example.flashcardapp.common.rememberImeState
import com.example.flashcardapp.domain.LoginState
import com.example.flashcardapp.presentation.AuthViewModel
import com.example.flashcardapp.presentation.NavigationViewModel
import com.example.flashcardapp.ui.composable.Footer
import com.example.flashcardapp.ui.composable.TopBar
import com.example.flashcardapp.ui.composable.button.LoginButton
import com.example.flashcardapp.ui.composable.textfield.EmailTextField
import com.example.flashcardapp.ui.composable.textfield.PasswordTextField
import com.example.flashcardapp.ui.navigation.DecksDestination
import com.example.flashcardapp.ui.navigation.LoginDestination
import com.example.flashcardapp.ui.navigation.RegisterDestination
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun LoginScreen(navController: NavController, navigationViewModel: NavigationViewModel) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    val loginState by authViewModel.loginState.collectAsState()
    val loginFailed by authViewModel.loginFailed.collectAsState()

    var loginErrorMessage by remember {
        mutableStateOf("")
    }
    val activity = LocalContext.current as Activity

    LaunchedEffect(true) {
        navigationViewModel.setNavigationDirection(NavigationDirection.LEFT_TO_RIGHT)
        authViewModel.login("test@test.com", "test123")
    }

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue / 2)
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(DecksDestination.route) {
                    popUpTo(LoginDestination.route) {
                        inclusive = true
                    }
                }
            }

            is LoginState.Error -> {
                loginErrorMessage = (loginState as LoginState.Error).message
                authViewModel.loginFailed()
            }

            else -> {

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.background)
            .padding(AppTheme.size.small)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(icon = Icons.Filled.Close, iconDescription = "Close App") {
            activity.finish()
        }
        BodyLogin(authViewModel, navigationViewModel, loginFailed, loginErrorMessage, loginState)
        Footer(text1 = "Don't have an account?", text2 = "Sign Up") {
            navController.navigate(RegisterDestination.route)
        }
    }
}

@Composable
fun BodyLogin(
    authViewModel: AuthViewModel,
    navigationViewModel: NavigationViewModel,
    loginFailed: Boolean,
    loginErrorMessage: String,
    loginState: LoginState
) {
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    val controller = LocalSoftwareKeyboardController.current
    Column {
        EmailTextField(email = email, emailError = emailError) {
            email = it
        }
        Spacer(modifier = Modifier.size(AppTheme.size.small))
        PasswordTextField(password = password, passwordError = passwordError) {
            password = it
        }
        Spacer(modifier = Modifier.size(AppTheme.size.small))
        ForgotPassword(modifier = Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.size(AppTheme.size.medium))
        if (loginFailed) LoginFailedMessage(loginErrorMessage)
        LoginButton(
            email = email,
            password = password,
            text = "Sign In",
            authViewModel = authViewModel,
            loginState = loginState,
            setEmailError = { emailError = it },
            setPasswordError = { passwordError = it }
        ) {
            controller?.hide()
            navigationViewModel.setNavigationDirection(NavigationDirection.LEFT_TO_RIGHT)
            authViewModel.login(email, password)
        }
    }
}

@Composable
fun LoginFailedMessage(message: String) {
    Row(
        modifier = Modifier.padding(
            horizontal = AppTheme.size.small,
            vertical = AppTheme.size.medium
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = "error icon",
            tint = AppTheme.colorScheme.error
        )
        Text(
            text = message,
            style = AppTheme.typography.labelNormal,
            modifier = Modifier.padding(horizontal = AppTheme.size.small)
        )
    }

}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Forgot password?",
        style = AppTheme.typography.labelNormal.copy(fontWeight = FontWeight.Bold),
        color = AppTheme.colorScheme.primary,
        modifier = modifier.padding(end = AppTheme.size.small)
    )

}
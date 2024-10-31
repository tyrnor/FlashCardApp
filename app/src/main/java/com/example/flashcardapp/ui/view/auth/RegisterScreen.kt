package com.example.flashcardapp.ui.view.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.rememberImeState
import com.example.flashcardapp.domain.LoginState
import com.example.flashcardapp.presentation.AuthViewModel
import com.example.flashcardapp.ui.composable.Footer
import com.example.flashcardapp.ui.composable.TopBar
import com.example.flashcardapp.ui.composable.button.RegisterButton
import com.example.flashcardapp.ui.composable.textfield.EmailTextField
import com.example.flashcardapp.ui.composable.textfield.PasswordTextField
import com.example.flashcardapp.ui.composable.textfield.UsernameTextField
import com.example.flashcardapp.ui.navigation.DecksDestination
import com.example.flashcardapp.ui.navigation.LoginDestination
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun RegisterScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    val loginState by authViewModel.loginState.collectAsState()
    val loginFailed by authViewModel.loginFailed.collectAsState()

    var loginErrorMessage by remember {
        mutableStateOf("")
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
            .background(AppTheme.colorScheme.background)
            .fillMaxSize()
            .padding(AppTheme.size.normal)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(icon = Icons.Filled.ArrowBackIosNew, iconDescription = "Back") {
            navController.popBackStack()
        }
        BodyRegister(
            authViewModel = authViewModel,
            loginFailed,
            loginErrorMessage
        )
        Footer(text1 = "Already have an account?", text2 = "Sign In") {
            navController.navigate(LoginDestination.route)
        }
    }
}


@Composable
fun BodyRegister(
    authViewModel: AuthViewModel,
    loginFailed: Boolean,
    loginErrorMessage: String,
) {

    var email by rememberSaveable { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var username by rememberSaveable { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    val controller = LocalSoftwareKeyboardController.current

    Column {
        EmailTextField(email = email, emailError = emailError) {
            email = it
        }
        Spacer(modifier = Modifier.size(AppTheme.size.small))
        UsernameTextField(username = username, usernameError = usernameError) {
            username = it
        }
        Spacer(modifier = Modifier.size(AppTheme.size.small))
        PasswordTextField(password = password, passwordError = passwordError) {
            password = it
        }
        Spacer(modifier = Modifier.size(AppTheme.size.medium))
        if (loginFailed) LoginFailedMessage(loginErrorMessage)
        RegisterButton(
            email = email,
            username = username,
            password = password,
            text = "Sign Up",
            authViewModel = authViewModel,
            setEmailError = { emailError = it },
            setUsernameError = { usernameError = it },
            setPasswordError = { passwordError = it }
        ) {
            if (!emailError && !usernameError && !passwordError) {
                controller?.hide()
                //authViewModel.register(email, password, username)
            }
        }
    }

}
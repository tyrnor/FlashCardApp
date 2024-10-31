package com.example.flashcardapp.ui.composable.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.domain.LoginState
import com.example.flashcardapp.presentation.AuthViewModel
import com.example.flashcardapp.ui.theme.AppTheme


@Composable
fun LoginButton(
    email: String,
    password: String,
    authViewModel: AuthViewModel,
    text : String,
    loginState: LoginState,
    setEmailError: (Boolean) -> Unit,
    setPasswordError: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    Button(
        onClick = {
            var hasError = false
            if (email.isBlank()) {
                setEmailError(true)
                authViewModel.resetLoginFailed()
                hasError = true
            } else {
                setEmailError(false)
            }

            if (password.isBlank()) {
                setPasswordError(true)
                authViewModel.resetLoginFailed()
                hasError = true
            } else {
                setPasswordError(false)
            }

            if (!hasError) {
                onClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.size.small),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colorScheme.primary,
            contentColor = AppTheme.colorScheme.onPrimary,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = AppTheme.size.small,
        ),
        shape = AppTheme.shape.button
    ) {
        if (loginState is LoginState.Loading) {
            CircularProgressIndicator(
                color = AppTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(AppTheme.size.medium)
            )
        } else {
            Text(text = text, style = AppTheme.typography.labelLarge)
        }
    }
}
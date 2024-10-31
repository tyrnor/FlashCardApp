package com.example.flashcardapp.ui.composable.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun PasswordTextField(
    password: String,
    passwordError: Boolean,
    onPasswordChange: (String) -> Unit,
) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.size.small)
            .border(
                1.dp,
                color = if (passwordError) AppTheme.colorScheme.error else AppTheme.colorScheme.primary,
                shape = AppTheme.shape.container
            ),
        placeholder = { Text(text = "Password", style = AppTheme.typography.body) },
        textStyle = AppTheme.typography.body,
        maxLines = 1,
        isError = passwordError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors().copy(
            unfocusedTextColor = AppTheme.colorScheme.onSecondary,
            focusedTextColor = AppTheme.colorScheme.onSecondary,
            unfocusedContainerColor = AppTheme.colorScheme.secondary,
            focusedContainerColor = AppTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            if (passwordError) {
                Icon(imageVector = Icons.Filled.Error, contentDescription = "error icon")
            } else {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = "show pass")
                }
            }

        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        shape = AppTheme.shape.container
    )
    if (passwordError) {
        Text(
            text = "Password cannot be empty",
            color = AppTheme.colorScheme.error,
            style = AppTheme.typography.labelNormal,
            modifier = Modifier.padding(start = AppTheme.size.small, top = AppTheme.size.small)
        )
    }
}
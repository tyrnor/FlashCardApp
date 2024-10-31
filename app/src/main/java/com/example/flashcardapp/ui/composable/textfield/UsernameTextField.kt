package com.example.flashcardapp.ui.composable.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun UsernameTextField(
    username: String,
    usernameError: Boolean,
    onTextChanged: (String) -> Unit,
) {
    TextField(
        value = username,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.size.small)
            .border(
                1.dp,
                color = if (usernameError) AppTheme.colorScheme.error else AppTheme.colorScheme.primary,
                shape = AppTheme.shape.container
            ),
        placeholder = { Text(text = "Username", style = AppTheme.typography.body) },
        textStyle = AppTheme.typography.body,
        isError = usernameError,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        colors = TextFieldDefaults.colors().copy(
            unfocusedTextColor = AppTheme.colorScheme.onSecondary,
            focusedTextColor = AppTheme.colorScheme.onSecondary,
            unfocusedContainerColor = AppTheme.colorScheme.secondary,
            focusedContainerColor = AppTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        shape = AppTheme.shape.container,
        trailingIcon = {
            if (usernameError) {
                Icon(imageVector = Icons.Filled.Error, contentDescription = "error icon")
            }
        }
    )
    if (usernameError) {
        Text(
            text = "Username cannot be empty",
            color = AppTheme.colorScheme.error,
            style = AppTheme.typography.labelNormal,
            modifier = Modifier.padding(start = AppTheme.size.small, top = AppTheme.size.small)
        )
    }
}
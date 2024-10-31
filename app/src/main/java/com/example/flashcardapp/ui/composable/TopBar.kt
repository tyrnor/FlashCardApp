package com.example.flashcardapp.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.ui.theme.AppTheme.size
import com.example.flashcardapp.ui.theme.AppTheme.colorScheme


@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconDescription: String,
    onClick: () -> Unit,
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = size.small, vertical = size.large)
        .height(48.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier
                .clickable { onClick() }
                .size(48.dp)
                .padding(size.small),
            tint = colorScheme.icons
        )
    }

}
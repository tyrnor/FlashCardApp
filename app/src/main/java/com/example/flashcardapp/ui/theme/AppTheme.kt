package com.example.flashcardapp.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val darkColorScheme = AppColorScheme(
    background = DarkPurple,
    onBackground = White,
    primary = Purple,
    onPrimary = White,
    secondary = LightGrey,
    onSecondary = Black,
    icons = White,
    error = Red,
    chatBackground = Black,
    chatText = White,
    chatContainer = Grey
)

private val lightColorScheme = AppColorScheme(
    background = White,
    onBackground = Black,
    primary = Purple,
    onPrimary = White,
    secondary = LightGrey,
    onSecondary = Black,
    icons = Black,
    error = Red,
    chatBackground = LightGrey,
    chatText = Black,
    chatContainer = White
)

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleNormal = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    body = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelNormal = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(25)
)

private val size = AppSize (
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()
    CompositionLocalProvider (
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current
    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
    val shape: AppShape
        @Composable get() = LocalAppShape.current
    val size : AppSize
        @Composable get() = LocalAppSize.current
}
package com.example.flashcardapp.common

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun slideOutFadeOutFromLeft(): ExitTransition = slideOutHorizontally(
    targetOffsetX = { -1000 }, animationSpec = tween(700)
) + fadeOut(animationSpec = tween(700))

fun slideOutFadeOutFromRight(): ExitTransition = slideOutHorizontally(
    targetOffsetX = { 1000 }, animationSpec = tween(700)
) + fadeOut(animationSpec = tween(700))

fun slideInFadeInFromLeft(): EnterTransition = slideInHorizontally(
    initialOffsetX = { 1000 }, animationSpec = tween(700)
) + fadeIn(animationSpec = tween(700))

fun slideInFadeInFromRight(): EnterTransition = slideInHorizontally(
    initialOffsetX = { -1000 }, animationSpec = tween(700)
) + fadeIn(animationSpec = tween(700))
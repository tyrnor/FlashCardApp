package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import com.example.flashcardapp.common.NavigationDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationViewModel : ViewModel() {

    private val _navigationDirection = MutableStateFlow<NavigationDirection?>(null)
    val navigationDirection: StateFlow<NavigationDirection?> = _navigationDirection

    fun setNavigationDirection(direction: NavigationDirection) {
        _navigationDirection.value = direction
    }

    fun resetNavigationDirection() {
        _navigationDirection.value = null
    }
}
package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.usecase.GetDecksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val getDecksUseCase: GetDecksUseCase
) : ViewModel() {
    private val _decksState = MutableStateFlow<Result<List<Deck>>?>(null)
    val decksState: StateFlow<Result<List<Deck>>?> = _decksState

    fun getDecks() {
        viewModelScope.launch {
            getDecksUseCase().collect { result ->
                _decksState.value = result
            }
        }
    }
}
package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.usecase.firestore.AddDeckUseCase
import com.example.flashcardapp.domain.usecase.firestore.DeleteDeckUseCase
import com.example.flashcardapp.domain.usecase.firestore.GetDecksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val getDecksUseCase: GetDecksUseCase,
    private val addDeckUseCase: AddDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase
) : ViewModel() {
    private val _decksState = MutableStateFlow<Result<List<Deck>>?>(null)
    val decksState: StateFlow<Result<List<Deck>>?> = _decksState

    fun getUserDecks() {
        viewModelScope.launch {
            getDecksUseCase().collect { result ->
                _decksState.value = result
            }
        }
    }

    fun addDeck(deck: Deck) {
        viewModelScope.launch {
            addDeckUseCase(deck)
        }
    }

    fun deleteDeck(deckId: String) {
        viewModelScope.launch {
            deleteDeckUseCase(deckId)
        }
    }
}
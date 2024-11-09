package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.usecase.firestore.AddCardUseCase
import com.example.flashcardapp.domain.usecase.firestore.AddDeckUseCase
import com.example.flashcardapp.domain.usecase.firestore.DeleteDeckUseCase
import com.example.flashcardapp.domain.usecase.firestore.EditCardUseCase
import com.example.flashcardapp.domain.usecase.firestore.GetCurrentDeckUseCase
import com.example.flashcardapp.domain.usecase.firestore.GetDeckCardsUseCase
import com.example.flashcardapp.domain.usecase.firestore.GetDecksUseCase
import com.example.flashcardapp.domain.usecase.firestore.UpdateLastCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val getDecksUseCase: GetDecksUseCase,
    private val addDeckUseCase: AddDeckUseCase,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val getDeckCardsUseCase: GetDeckCardsUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val editCardUseCase: EditCardUseCase,
    private val getCurrentDeckUseCase: GetCurrentDeckUseCase,
    private val updateLastCardUseCase: UpdateLastCardUseCase
) : ViewModel() {
    private val _decksState = MutableStateFlow<Result<List<Deck>>?>(null)
    val decksState: StateFlow<Result<List<Deck>>?> = _decksState

    private val _cardsState = MutableStateFlow<Result<List<Card>>?>(null)
    val cardsState: StateFlow<Result<List<Card>>?> = _cardsState

    private val _currentDeck = MutableStateFlow<Result<Deck>?>(null)
    val currentDeck: StateFlow<Result<Deck>?> = _currentDeck

    fun getUserDecks(uid: String) {
        viewModelScope.launch {
            getDecksUseCase(uid).collect { result ->
                _decksState.value = result
            }
        }
    }

    fun getDeckCards(uid: String, deckId: String) {
        viewModelScope.launch {
            getDeckCardsUseCase(uid, deckId).collect { result ->
                _cardsState.value = result
            }
        }
    }

    fun getCurrentDeck(uid: String, deckId: String) {
        viewModelScope.launch {
            getCurrentDeckUseCase(uid, deckId).collect { result ->
                _currentDeck.value = result
            }
        }
    }

    fun addDeck(uid: String, deck: Deck) {
        viewModelScope.launch {
            val addResult = addDeckUseCase(uid, deck)
            if (addResult.isSuccess) {
                refreshDecks(uid)
            }

        }
    }

    fun deleteDeck(uid: String, deckId: String) {
        viewModelScope.launch {
            val deleteResult = deleteDeckUseCase(uid, deckId)
            if (deleteResult.isSuccess) {
                refreshDecks(uid)
            }
        }
    }

    fun addCard(uid: String, deckId: String, card: Card) {
        viewModelScope.launch {
            val addResult = addCardUseCase(uid, deckId, card)
            if (addResult.isSuccess) {
                refreshCards(uid, deckId)
            }
        }
    }

    fun editCard(uid: String, deckId: String, cardId: String, card: Card) {
        viewModelScope.launch {
            editCardUseCase(uid, deckId, cardId, card)
        }
    }

    fun updateLastCard(uid: String, deckId: String, currentPage: Int) {
        viewModelScope.launch {
            updateLastCardUseCase(uid, deckId, currentPage)
        }
    }

    private suspend fun refreshDecks(uid: String) {
        getDecksUseCase(uid).collect { result ->
            _decksState.value = result
        }
    }

    private suspend fun refreshCards(uid: String, deckId: String) {
        getDeckCardsUseCase(uid, deckId).collect { result ->
            _cardsState.value = result
        }
    }

}
package com.example.flashcardapp.domain.repository

import com.example.flashcardapp.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun getUserDecks() : Flow<Result<List<Deck>>>
    suspend fun addDeck(deck: Deck) : Result<Unit>
    suspend fun deleteDeck(deckId: String) : Result<Unit>

}
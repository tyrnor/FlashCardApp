package com.example.flashcardapp.domain.repository

import com.example.flashcardapp.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun getUserDecks(uid: String) : Flow<Result<List<Deck>>>
    suspend fun addDeck(uid: String, deck: Deck) : Result<Unit>
    suspend fun deleteDeck(uid: String, deckId: String) : Result<Unit>

}
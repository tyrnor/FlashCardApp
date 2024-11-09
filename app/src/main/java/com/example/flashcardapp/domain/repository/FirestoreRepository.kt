package com.example.flashcardapp.domain.repository

import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun getUserDecks(uid: String) : Flow<Result<List<Deck>>>
    suspend fun addDeck(uid: String, deck: Deck) : Result<Unit>
    suspend fun deleteDeck(uid: String, deckId: String) : Result<Unit>
    suspend fun getDeckCards(uid: String, deckId: String) : Flow<Result<List<Card>>>
    suspend fun addCard(uid: String, deckId: String, card: Card) : Result<Unit>
    suspend fun editCard(uid: String, deckId: String, cardId: String, card: Card) : Result<Unit>
    suspend fun getCurrentDeck(uid: String, deckId: String) : Flow<Result<Deck>>
}
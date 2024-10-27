package com.example.flashcardapp.domain.repository

import com.example.flashcardapp.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun getDecks() : Flow<Result<List<Deck>>>
}
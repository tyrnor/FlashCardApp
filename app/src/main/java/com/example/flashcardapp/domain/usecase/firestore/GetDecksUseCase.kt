package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDecksUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String) : Flow<Result<List<Deck>>> {
        return firestoreRepository.getUserDecks(uid)
    }
}
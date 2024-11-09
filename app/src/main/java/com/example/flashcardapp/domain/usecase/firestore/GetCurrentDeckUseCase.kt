package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetCurrentDeckUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String, deckId: String) =
        firestoreRepository.getCurrentDeck(uid, deckId)
}
package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddDeckUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String, deck: Deck): Result<Unit> {
        return firestoreRepository.addDeck(uid, deck)
    }
}
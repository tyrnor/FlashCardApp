package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetDecksUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String) =
        firestoreRepository.getUserDecks(uid)
}
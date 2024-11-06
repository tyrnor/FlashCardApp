package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class EditCardUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String, deckId: String, cardId: String, card: Card) =
        firestoreRepository.editCard(uid, deckId, cardId, card)
}
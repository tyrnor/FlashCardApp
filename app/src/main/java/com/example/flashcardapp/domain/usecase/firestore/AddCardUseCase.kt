package com.example.flashcardapp.domain.usecase.firestore

import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddCardUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend operator fun invoke(uid: String, deckId: String, card: Card) =
        firestoreRepository.addCard(uid, deckId, card)
}
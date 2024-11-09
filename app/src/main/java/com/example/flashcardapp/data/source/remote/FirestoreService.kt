package com.example.flashcardapp.data.source.remote

import com.example.flashcardapp.data.model.CardDto
import com.example.flashcardapp.data.model.DeckDto
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val firestore = Firebase.firestore


    suspend fun getUserDecks(uid: String): Flow<Result<List<DeckDto>>> =
        callbackFlow {
            val collectionRef = firestore.collection("Users").document(uid).collection("Decks")
                .orderBy("timestamp")

            val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(Exception(error.message ?: "An error occurred")))
                    return@addSnapshotListener
                }
                val decks = snapshot?.documents?.map { document ->
                    document.toObject(DeckDto::class.java)!!.copy(id = document.id)
                } ?: emptyList()

                trySend(Result.success(decks))
            }

            awaitClose { listenerRegistration.remove() }
        }

    suspend fun getCurrentDeck(uid: String, deckId: String): Flow<Result<DeckDto>> = callbackFlow {
        val collectionRef = firestore.collection("Users").document(uid).collection("Decks")
            .document(deckId)

        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(Exception(error.message ?: "An error occurred")))
                return@addSnapshotListener
            } else {
                val deck = snapshot?.toObject(DeckDto::class.java)!!.copy(id = snapshot.id)
                trySend(Result.success(deck))
            }

        }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun getDeckCards(uid: String, deckId: String): Flow<Result<List<CardDto>>> =
        callbackFlow {
            val collectionRef = firestore.collection("Users").document(uid).collection("Decks")
                .document(deckId).collection("Cards").orderBy("timestamp")

            val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(Exception(error.message ?: "An error occurred")))
                    return@addSnapshotListener
                }
                val cards = snapshot?.documents?.map { document ->
                    document.toObject(CardDto::class.java)!!.copy(id = document.id)
                } ?: emptyList()

                trySend(Result.success(cards))
            }
            awaitClose { listenerRegistration.remove() }
        }

    suspend fun addDeck(uid: String, deck: DeckDto): Result<Unit> {
        return try {
            val deckData = hashMapOf(
                "name" to deck.name,
                "timestamp" to Timestamp.now(),
                "size" to deck.size,
                "lastCard" to deck.lastCard
            )
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .add(deckData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDeck(uid: String, deckId: String): Result<Unit> {
        return try {
            val deckRef = firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)

            val cardsRef = deckRef.collection("Cards")
            val cardsSnapshot = cardsRef.get().await()
            for (card in cardsSnapshot.documents) {
                card.reference.delete()
            }

            deckRef.delete().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCard(uid: String, deckId: String, card: CardDto): Result<Unit> {
        return try {
            val cardData = hashMapOf(
                "question" to card.question,
                "answer" to card.answer,
                "timestamp" to Timestamp.now()
            )
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)
                .collection("Cards")
                .add(cardData)
                .await()
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)
                .update("size", FieldValue.increment(1))
                .await()
            return Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editCard(uid: String, deckId: String, cardId: String, card: CardDto): Result<Unit> {
        return try {
            val cardData = mapOf(
                "question" to card.question,
                "answer" to card.answer,
            )
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)
                .collection("Cards")
                .document(cardId)
                .update(cardData)
                .await()
            return Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLastCard(uid: String, deckId: String, lastCard: Int): Result<Unit> {
        return try {
            val deckData = mapOf(
                "lastCard" to lastCard
            )
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)
                .update(deckData)
                .await()
            return Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
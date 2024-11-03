package com.example.flashcardapp.data.source.remote

import com.example.flashcardapp.data.model.DeckDto
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val firestore = Firebase.firestore
    private val uid = Firebase.auth.currentUser!!.uid

    suspend fun getUserDecks(): Flow<Result<List<DeckDto>>> = callbackFlow {
        val collectionRef = firestore.collection("Users").document(uid).collection("Decks")
            .orderBy("timestamp", Query.Direction.DESCENDING)

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

    suspend fun addDeck(deck: DeckDto): Result<Unit> {
        return try {
            val deckData = hashMapOf(
                "name" to deck.name,
                "timestamp" to deck.timestamp
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

    suspend fun deleteDeck(deckId: String): Result<Unit> {
        return try {
            firestore
                .collection("Users")
                .document(uid)
                .collection("Decks")
                .document(deckId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
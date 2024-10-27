package com.example.flashcardapp.data.source.remote

import com.example.flashcardapp.data.model.DeckDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreService {

    private val firestore = Firebase.firestore

    suspend fun getUserDecks(uid: String): Flow<Result<List<DeckDto>>> = callbackFlow {
        val collectionRef =
            firestore.collection("Users").document(uid).collection("Decks")
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
}
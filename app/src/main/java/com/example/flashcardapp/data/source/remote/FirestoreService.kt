package com.example.flashcardapp.data.source.remote

import com.example.flashcardapp.data.model.DeckDto
import com.example.flashcardapp.domain.Resource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreService {

    private val firestore = Firebase.firestore

    suspend fun getDecks(): Flow<Resource<List<DeckDto>>> = callbackFlow {
        trySend(Resource.Loading)

        val collectionRef = firestore.collection("Decks")

        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "An error occurred"))
                return@addSnapshotListener
            }
            val decks = snapshot?.documents?.map { document ->
                document.toObject(DeckDto::class.java)!!.copy(id = document.id)
            } ?: emptyList()

            trySend(Resource.Success(decks))
        }

        awaitClose { listenerRegistration.remove() }
    }
}
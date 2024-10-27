package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.source.remote.FirestoreService
import com.example.flashcardapp.domain.repository.FirestoreRepository
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firestoreService: FirestoreService) :
    FirestoreRepository {

}
package com.example.flashcardapp.di

import com.example.flashcardapp.data.repository.FirestoreRepositoryImpl
import com.example.flashcardapp.data.source.remote.FirestoreService
import com.example.flashcardapp.domain.mapper.DeckMapper
import com.example.flashcardapp.domain.repository.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestoreService(): FirestoreService = FirestoreService()

    @Provides
    @Singleton
    fun provideDeckMapper(): DeckMapper = DeckMapper()

    @Provides
    @Singleton
    fun provideFirestoreRepository(firestoreService: FirestoreService, deckMapper: DeckMapper): FirestoreRepository =
        FirestoreRepositoryImpl(firestoreService, deckMapper)

}
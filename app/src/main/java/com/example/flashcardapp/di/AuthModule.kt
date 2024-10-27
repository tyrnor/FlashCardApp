package com.example.flashcardapp.di

import com.example.flashcardapp.data.repository.AuthRepositoryImpl
import com.example.flashcardapp.data.source.remote.AuthService
import com.example.flashcardapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = AuthService()

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)
}
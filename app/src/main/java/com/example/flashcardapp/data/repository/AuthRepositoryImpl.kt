package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.source.remote.AuthService
import com.example.flashcardapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository{

    override suspend fun register(email: String, password: String): Result<Unit> {
        return authService.register(email, password)
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return authService.login(email, password)
    }

}
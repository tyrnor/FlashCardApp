package com.example.flashcardapp.domain.usecase.auth

import com.example.flashcardapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) : Result<Unit> {
        return authRepository.login(email, password)
    }
 }
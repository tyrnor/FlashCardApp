package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.domain.LoginState
import com.example.flashcardapp.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _loginFailed = MutableStateFlow(false)
    val loginFailed : StateFlow<Boolean> = _loginFailed

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = loginUseCase(email, password)
                result.fold(
                    onSuccess = {
                        _loginState.value = LoginState.Success
                    },
                    onFailure = {
                        _loginState.value = LoginState.Error(it.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loginFailed(){
        _loginFailed.value = true
    }
    fun resetLoginFailed() {
        _loginFailed.value = false
    }
}
package com.example.flashcardapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.domain.Resource
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.usecase.GetDecksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val getDecksUseCase: GetDecksUseCase
) : ViewModel() {
    private val _decksState = MutableStateFlow<Resource<List<Deck>>>(Resource.Loading)
    val decksState: StateFlow<Resource<List<Deck>>> = _decksState

    fun getDecks() {
        viewModelScope.launch {
            getDecksUseCase().collect { resource ->
                _decksState.value = resource
            }
        }
    }
}
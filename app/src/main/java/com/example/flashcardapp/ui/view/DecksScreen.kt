package com.example.flashcardapp.ui.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.domain.Resource
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.presentation.DecksViewModel

@Composable
fun DecksScreen() {
    val decksViewModel: DecksViewModel = viewModel()
    val decksState by decksViewModel.decksState.collectAsState()

    LaunchedEffect(Unit) {
        decksViewModel.getDecks()
    }

    when(decksState) {
        is Resource.Loading -> Text(text = "Loading")
        is Resource.Success -> {
            val decks = (decksState as Resource.Success<List<Deck>>).data
            LazyColumn {
                items(decks) {
                    Text(text = it.name)
                }
            }
        }
        is Resource.Error -> Text((decksState as Resource.Error).message)
    }
}
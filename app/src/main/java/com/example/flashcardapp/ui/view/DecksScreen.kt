package com.example.flashcardapp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.presentation.DecksViewModel
import com.example.flashcardapp.ui.navigation.DeckDestination
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun DecksScreen(navController: NavController) {
    val decksViewModel: DecksViewModel = hiltViewModel()
    val decksState by decksViewModel.decksState.collectAsState()
    val uid = Firebase.auth.currentUser!!.uid

    LaunchedEffect(Unit) {
        decksViewModel.getUserDecks(uid)
    }

    decksState?.let { result ->
        result.fold(
            onSuccess = { decks ->
                DeckList(decks) {
                    navController.navigate(DeckDestination.route)
                }
            },
            onFailure =  { error ->
                Text(text = "Error: ${error.message}")
            }
        )
    } ?: CircularProgressIndicator()
}

@Composable
fun DeckList(decks: List<Deck>, onDeckClick: (Deck) -> Unit) {
    LazyColumn {
        items(decks) { deck ->
            DeckItem(deck, onDeckClick)
        }
    }
}

@Composable
fun DeckItem(deck: Deck, onDeckClick: (Deck) -> Unit) {
    Text(
        text = deck.name,
        modifier = Modifier.clickable { onDeckClick(deck) }
    )
}

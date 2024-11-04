package com.example.flashcardapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.AuthUtils
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.presentation.DecksViewModel
import com.example.flashcardapp.ui.theme.AppTheme

@Composable
fun EditDeckScreen(deckId: String?, navController: NavController) {
    val decksViewModel: DecksViewModel = hiltViewModel()
    val cardsState by decksViewModel.cardsState.collectAsState()

    val uid = AuthUtils.currentId

    if (uid != null) {
        LaunchedEffect(Unit) {
            if (deckId != null) {
                decksViewModel.getDeckCards(uid, deckId)
            }
        }
        cardsState?.let { result ->
            result.fold(
                onSuccess = { cards ->
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                }
                            ) {
                                Icon(Icons.Default.Add, "")
                            }
                        },
                        containerColor = AppTheme.colorScheme.background
                    ) { paddingValues ->
                        if (cards.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No cards found")
                            }
                        } else {
                            CardList(
                                paddingValues = paddingValues,
                                cards = cards
                            )
                        }
                    }

                },
                onFailure = { error ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error: ${error.message}")
                    }
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Can't load user information")
        }
    }
}

@Composable
fun CardList(
    paddingValues: PaddingValues,
    cards: List<Card>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(AppTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(cards) { card ->
            Column {
                Text(card.question)
                Text(card.answer)
            }
        }
    }
}

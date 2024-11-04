package com.example.flashcardapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.AuthUtils
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.presentation.DecksViewModel
import com.example.flashcardapp.ui.theme.AppTheme
import com.google.firebase.Timestamp

@Composable
fun EditDeckScreen(deckId: String?, navController: NavController) {
    val decksViewModel: DecksViewModel = hiltViewModel()
    val cardsState by decksViewModel.cardsState.collectAsState()

    val uid = AuthUtils.currentId

    var openDialog by remember { mutableStateOf(false) }
    var cardQuestion by rememberSaveable { mutableStateOf("") }
    var cardAnswer by rememberSaveable { mutableStateOf("") }
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
                                    if (deckId != null) {
                                        openDialog = true
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, "")
                            }
                        },
                        containerColor = AppTheme.colorScheme.background
                    ) { paddingValues ->
                        if (openDialog) {
                            CreateCardAlertDialog(
                                cardQuestion = cardQuestion,
                                cardAnswer = cardAnswer,
                                onCardQuestionChange = { value ->
                                    cardQuestion = value
                                },
                                onCardAnswerChange = { value ->
                                    cardAnswer = value
                                },
                                onConfirmation = {
                                    decksViewModel.addCard(
                                        uid = uid,
                                        deckId = deckId!!,
                                        card = Card(question = cardQuestion, answer = cardAnswer)
                                    )
                                    openDialog = false
                                    cardQuestion = ""
                                    cardAnswer = ""
                                },
                                onDismiss = { openDialog = false }
                            )
                        }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardAlertDialog(
    cardQuestion: String,
    cardAnswer: String,
    onCardQuestionChange: (String) -> Unit,
    onCardAnswerChange: (String) -> Unit,
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
    ) {
        Card(shape = AppTheme.shape.container) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.size.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create new Card",
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.titleNormal
                )
                Spacer(modifier = Modifier.height(AppTheme.size.medium))
                TextField(
                    value = cardQuestion,
                    onValueChange = { onCardQuestionChange(it) },
                    placeholder = { Text("Question") }
                )
                Spacer(modifier = Modifier.height(AppTheme.size.medium))
                TextField(
                    value = cardAnswer,
                    onValueChange = { onCardAnswerChange(it) },
                    placeholder = { Text("Answer") }
                )
                Spacer(modifier = Modifier.height(AppTheme.size.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        onDismiss()
                    }) { Text("Cancel") }
                    Button(
                        enabled = cardQuestion.isNotEmpty() && cardAnswer.isNotEmpty(),
                        onClick = {
                            if (cardQuestion.isNotEmpty() && cardAnswer.isNotEmpty()) {
                                onConfirmation()
                            }
                        }) {
                        Text("Create")
                    }
                }
            }
        }
    }
}
package com.example.flashcardapp.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
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

    var openCreateDialog by remember { mutableStateOf(false) }
    var openEditDialog by remember { mutableStateOf(false) }

    var cardQuestion by rememberSaveable { mutableStateOf("") }
    var cardAnswer by rememberSaveable { mutableStateOf("") }
    var selectedCardId by remember { mutableStateOf<String?>(null) }
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
                                        openCreateDialog = true
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, "")
                            }
                        },
                        containerColor = AppTheme.colorScheme.background
                    ) { paddingValues ->
                        if (openCreateDialog) {
                            CreateEditCardAlertDialog(
                                dialogTitle = "Create New Card",
                                cardQuestion = cardQuestion,
                                cardAnswer = cardAnswer,
                                buttonText = "Create",
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
                                    openCreateDialog = false
                                    cardQuestion = ""
                                    cardAnswer = ""
                                },
                                onDismiss = {
                                    openCreateDialog = false
                                    cardQuestion = ""
                                    cardAnswer = ""
                                }
                            )
                        }
                        if (openEditDialog) {
                            CreateEditCardAlertDialog(
                                dialogTitle = "Edit Card",
                                cardQuestion = cardQuestion,
                                cardAnswer = cardAnswer,
                                buttonText = "Edit",
                                onCardQuestionChange = { value ->
                                    cardQuestion = value
                                },
                                onCardAnswerChange = { value ->
                                    cardAnswer = value
                                },
                                onConfirmation = {
                                    decksViewModel.editCard(
                                        uid = uid,
                                        deckId = deckId!!,
                                        cardId = selectedCardId!!,
                                        card = Card(question = cardQuestion, answer = cardAnswer)
                                    )
                                    openEditDialog = false
                                    cardQuestion = ""
                                    cardAnswer = ""
                                },
                                onDismiss = {
                                    openEditDialog = false
                                    cardQuestion = ""
                                    cardAnswer = ""
                                }
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
                                cards = cards,
                                onCardClick = { card ->
                                    selectedCardId = card.id
                                    cardQuestion = card.question
                                    cardAnswer = card.answer
                                    openEditDialog = true
                                }
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
    cards: List<Card>,
    onCardClick: (Card) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(AppTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(cards, key = { it.id }) { card ->
            Box(modifier = Modifier.animateItem()) {
                CardItem(
                    cardQuestion = card.question,
                    cardAnswer = card.answer,
                    onClick = { onCardClick(card) }
                )
            }
        }
    }
}

@Composable
fun CardItem(
    cardQuestion: String,
    cardAnswer: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
        ,
        shape = RectangleShape,
        colors = CardDefaults.cardColors().copy(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Text("Question")
            Text(cardQuestion)
            Text("Answer")
            Text(cardAnswer)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditCardAlertDialog(
    dialogTitle: String,
    cardQuestion: String,
    cardAnswer: String,
    buttonText: String,
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
                    text = dialogTitle,
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
                        Text(buttonText)
                    }
                }
            }
        }
    }
}
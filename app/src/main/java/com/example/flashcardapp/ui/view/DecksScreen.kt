package com.example.flashcardapp.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flashcardapp.common.AuthUtils
import com.example.flashcardapp.common.NavigationDirection
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.presentation.DecksViewModel
import com.example.flashcardapp.presentation.NavigationViewModel
import com.example.flashcardapp.ui.composable.CantLoadUserInformation
import com.example.flashcardapp.ui.composable.OnFailure
import com.example.flashcardapp.ui.composable.ProgressIndicator
import com.example.flashcardapp.ui.navigation.DeckDestination
import com.example.flashcardapp.ui.navigation.EditDeckDestination
import com.example.flashcardapp.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DecksScreen(navController: NavController, navigationViewModel: NavigationViewModel) {
    val decksViewModel: DecksViewModel = hiltViewModel()
    val decksState by decksViewModel.decksState.collectAsState()

    val context = LocalContext.current

    val uid = AuthUtils.currentId

    var openDialog by remember { mutableStateOf(false) }
    var deckName by rememberSaveable { mutableStateOf("") }


    if (uid != null) {
        LaunchedEffect(Unit) {
            decksViewModel.getUserDecks(uid)
        }
        decksState?.let { result ->
            result.fold(
                onSuccess = { decks ->
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    openDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Add, "")
                            }
                        },
                        containerColor = AppTheme.colorScheme.background
                    ) { paddingValues ->
                        if (openDialog) {
                            CreateDeckAlertDialog(
                                deckName,
                                onDeckNameChange = { value ->
                                    deckName = value
                                },
                                onConfirmation = {
                                    decksViewModel.addDeck(
                                        uid = uid,
                                        deck = Deck(name = deckName)
                                    )
                                    openDialog = false
                                    deckName = ""
                                },
                                onDismiss = {
                                    openDialog = false
                                    deckName = ""
                                }
                            )
                        }
                        if (decks.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No decks found")
                            }
                        } else {
                            DeckList(
                                paddingValues = paddingValues,
                                decks = decks,
                                editDeck = { deckId ->
                                    navigationViewModel.setNavigationDirection(NavigationDirection.RIGHT_TO_LEFT)
                                    navController.navigate(EditDeckDestination.route + "/$deckId")
                                },
                                deleteDeck = { deckId -> decksViewModel.deleteDeck(uid, deckId) }
                            ) { deck ->
                                if (deck.size == 0) {
                                    Toast.makeText(context, "Deck is empty", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    navigationViewModel.setNavigationDirection(NavigationDirection.RIGHT_TO_LEFT)
                                    navController.navigate(DeckDestination.route + "/${deck.id}")
                                }
                            }
                        }
                    }
                },
                onFailure = { error ->
                    OnFailure(error)
                }
            )
        } ?: ProgressIndicator()
    } else {
        CantLoadUserInformation()
    }


}


@Composable
fun DeckList(
    paddingValues: PaddingValues,
    decks: List<Deck>,
    editDeck: (String) -> Unit,
    deleteDeck: (String) -> Unit,
    onDeckClick: (Deck) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(AppTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(decks, key = { it.id }) { deck ->
            Box(modifier = Modifier.animateItem()) {
                DeckItem(
                    deck = deck,
                    editDeck = {
                        editDeck(deck.id)
                    },
                    deleteDeck = {
                        deleteDeck(deck.id)
                    },
                    onDeckClick = {
                        onDeckClick(deck)
                    }
                )
            }
        }

    }
}


@Composable
fun DeckItem(
    deck: Deck,
    editDeck: () -> Unit,
    deleteDeck: () -> Unit,
    onDeckClick: () -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    val formattedDate = remember(deck.timestamp) {
        val date = deck.timestamp.toDate()
        SimpleDateFormat("d MMM", Locale.getDefault()).format(date)
    }
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors().copy(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppTheme.size.medium),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = AppTheme.size.medium,
                        top = AppTheme.size.medium
                    )
                ) {
                    Text(
                        text = deck.name,
                    )
                    Text(
                        text = "Last Update"
                    )
                    Text(
                        text = formattedDate
                    )
                }
                Column(modifier = Modifier.padding(end = AppTheme.size.small)) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, onClick = {
                            editDeck()
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = {
                            deleteDeck()
                            Toast.makeText(context, "Deck deleted", Toast.LENGTH_SHORT).show()
                            expanded = false
                        })
                    }
                }

            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "0",
                    color = Color.Black
                )
                Text(
                    text = "0",
                    color = Color.Black
                )
                Text(
                    text = deck.size.toString(),
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.size.medium))
            Button(
                onClick = { onDeckClick() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(AppTheme.shape.button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                ),
                shape = AppTheme.shape.container
            ) {
                Text(
                    text = "Study",
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.size.medium))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckAlertDialog(
    deckName: String,
    onDeckNameChange: (String) -> Unit,
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
                    text = "Create Deck",
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.titleNormal
                )
                Spacer(modifier = Modifier.height(AppTheme.size.medium))
                TextField(
                    value = deckName,
                    onValueChange = { onDeckNameChange(it) },
                    placeholder = { Text("Deck Name") }
                )
                Spacer(modifier = Modifier.height(AppTheme.size.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        onDismiss()
                    }) { Text("Cancel") }
                    Button(enabled = deckName.isNotEmpty(), onClick = {
                        if (deckName.isNotEmpty()) {
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

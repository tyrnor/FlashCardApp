package com.example.flashcardapp.ui.navigation

interface Destinations {
    val route: String
}

object DecksDestination : Destinations {
    override val route: String = "decks"
}

object DeckDestination : Destinations {
    override val route: String = "deck"
}
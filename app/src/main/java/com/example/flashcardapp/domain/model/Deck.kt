package com.example.flashcardapp.domain.model

import com.google.firebase.Timestamp

data class Deck(
    val id: String = "",
    val name: String,
    val timestamp: Timestamp = Timestamp.now(),
    val size: Int = 0,
    val lastCard : Int = 0
)

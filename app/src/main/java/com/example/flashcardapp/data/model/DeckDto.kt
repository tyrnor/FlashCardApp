package com.example.flashcardapp.data.model

import com.google.firebase.Timestamp

data class DeckDto(
    val id : String = "",
    val name : String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val size : Int = 0,
    val lastCard : Int = 0,
)

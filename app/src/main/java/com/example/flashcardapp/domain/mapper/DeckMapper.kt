package com.example.flashcardapp.domain.mapper

import com.example.flashcardapp.data.model.DeckDto
import com.example.flashcardapp.domain.model.Deck

class DeckMapper {

    fun toDomain(deckDto: DeckDto): Deck {
        return Deck(
            id = deckDto.id,
            name = deckDto.name,
            timestamp = deckDto.timestamp,
            size = deckDto.size,
            lastCard = deckDto.lastCard,
            currentSession = deckDto.currentSession
        )
    }

    fun toDto(deck: Deck): DeckDto {
        return DeckDto(
            id = deck.id,
            name = deck.name,
            timestamp = deck.timestamp,
            size = deck.size,
            lastCard = deck.lastCard,
            currentSession = deck.currentSession
        )
    }
}
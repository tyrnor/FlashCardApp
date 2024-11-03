package com.example.flashcardapp.domain.mapper

import com.example.flashcardapp.data.model.CardDto
import com.example.flashcardapp.domain.model.Card

class CardMapper {

    fun toDomain(cardDto: CardDto): Card {
        return Card(
            id = cardDto.id,
            question = cardDto.question,
            answer = cardDto.answer
        )
    }

    fun toDto(card: Card): CardDto {
        return CardDto(
            id = card.id,
            question = card.question,
            answer = card.answer
        )
    }
}
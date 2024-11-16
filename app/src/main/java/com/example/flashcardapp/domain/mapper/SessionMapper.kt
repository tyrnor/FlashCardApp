package com.example.flashcardapp.domain.mapper

import com.example.flashcardapp.data.model.CardDto
import com.example.flashcardapp.data.model.SessionDto
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.model.Session

class SessionMapper {
    fun toDomain(sessionDto: SessionDto): Session {
        return Session(
            cards = sessionDto.cards.map { cardDto ->
                Card(
                    id = cardDto.id,
                    question = cardDto.question,
                    answer = cardDto.answer
                )
            }
        )
    }

    fun toDto(session: Session): SessionDto {
        return SessionDto(
            cards = session.cards.map { card ->
                CardDto(
                    id = card.id,
                    question = card.question,
                    answer = card.answer
                )
            }
        )
    }
}
package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.source.remote.FirestoreService
import com.example.flashcardapp.domain.mapper.CardMapper
import com.example.flashcardapp.domain.mapper.DeckMapper
import com.example.flashcardapp.domain.model.Card
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService,
    private val deckMapper: DeckMapper,
    private val cardMapper: CardMapper
) :
    FirestoreRepository {
    override suspend fun getUserDecks(uid: String): Flow<Result<List<Deck>>> {
        return firestoreService.getUserDecks(uid).map { result ->
            result.mapCatching { deckDtos ->
                deckDtos.map { deckMapper.toDomain(it) }
            }
        }
    }

    override suspend fun addDeck(uid: String, deck: Deck): Result<Unit> {
        val deckDto = deckMapper.toDto(deck)
        return firestoreService.addDeck(uid, deckDto)
    }

    override suspend fun deleteDeck(uid: String, deckId: String): Result<Unit> {
        return firestoreService.deleteDeck(uid, deckId)
    }

    override suspend fun getDeckCards(uid: String, deckId: String): Flow<Result<List<Card>>> {
        return firestoreService.getDeckCards(uid, deckId).map { result ->
            result.mapCatching { cardDtos ->
                cardDtos.map { cardMapper.toDomain(it) }
            }
        }
    }

    override suspend fun addCard(uid: String, deckId: String, card: Card): Result<Unit> {
        val cardDto = cardMapper.toDto(card)
        return firestoreService.addCard(uid, deckId, cardDto)
    }

    override suspend fun editCard(
        uid: String,
        deckId: String,
        cardId: String,
        card: Card
    ): Result<Unit> {
        val cardDto = cardMapper.toDto(card)
        return firestoreService.editCard(uid, deckId, cardId, cardDto)

    }

    override suspend fun getCurrentDeck(uid: String, deckId: String): Flow<Result<Deck>> {
        return firestoreService.getCurrentDeck(uid, deckId).map { result ->
            result.mapCatching { deckMapper.toDomain(it) }
        }
    }
}
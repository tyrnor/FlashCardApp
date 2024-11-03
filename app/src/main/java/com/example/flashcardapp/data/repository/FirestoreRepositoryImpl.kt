package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.source.remote.FirestoreService
import com.example.flashcardapp.domain.mapper.DeckMapper
import com.example.flashcardapp.domain.model.Deck
import com.example.flashcardapp.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService,
    private val deckMapper: DeckMapper
) :
    FirestoreRepository {
    override suspend fun getUserDecks(): Flow<Result<List<Deck>>> {
        return firestoreService.getUserDecks().map { result ->
            result.mapCatching { deckDtos ->
                deckDtos.map { deckMapper.toDomain(it) }
            }
        }
    }

    override suspend fun addDeck(deck: Deck): Result<Unit> {
        val deckDto = deckMapper.toDto(deck)
        return firestoreService.addDeck(deckDto)
    }

    override suspend fun deleteDeck(deckId: String): Result<Unit> {
        return firestoreService.deleteDeck(deckId)
    }
}
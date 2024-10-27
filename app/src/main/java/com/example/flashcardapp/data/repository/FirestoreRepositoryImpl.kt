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
    override suspend fun getDecks(): Flow<Result<List<Deck>>> {
        return firestoreService.getDecks().map { result ->
            result.mapCatching { deckDtos ->
                // Transform each DeckDto to a Deck using the mapper
                deckDtos.map { deckMapper.toDomain(it) }
            }
        }
    }
}
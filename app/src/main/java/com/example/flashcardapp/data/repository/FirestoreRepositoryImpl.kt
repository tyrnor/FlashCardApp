package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.source.remote.FirestoreService
import com.example.flashcardapp.domain.Resource
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
    override suspend fun getDecks(): Flow<Resource<List<Deck>>> {
        return firestoreService.getDecks().map { resource ->
            when (resource) {
                is Resource.Loading -> Resource.Loading
                is Resource.Success -> {
                    val decks = resource.data.map { deckDto ->
                        deckMapper.toDomain(deckDto)
                    }
                    Resource.Success(decks)
                }
                is Resource.Error -> Resource.Error(resource.message)
            }
        }
    }

}
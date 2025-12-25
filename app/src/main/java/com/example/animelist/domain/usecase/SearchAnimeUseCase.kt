package com.example.animelist.domain.usecase

import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    //флоу
    operator fun invoke(query: String): Flow<List<Anime>> {
        require(query.length >= 2) { "..." }
        return repository.searchAnimeFlow(query)
    }

    //пагинация
    suspend operator fun invoke(query: String, limit: Int, offset: Int): List<Anime> {
        require(query.length >= 2) { "..." }
        return repository.searchAnime(query, limit, offset)
    }
}
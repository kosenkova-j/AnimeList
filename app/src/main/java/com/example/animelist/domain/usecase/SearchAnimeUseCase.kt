package com.example.animelist.domain.usecase

import com.example.animelist.domain.model.Anime
import com.example.animelist.data.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    // Для suspend вызовов
//    suspend operator fun invoke(query: String): List<Anime> {
//        require(query.length >= 2) { "..." }
//        return repository.searchAnime(query)
//    }

    // Для Flow/observable вызовов
    operator fun invoke(query: String): Flow<List<Anime>> {
        require(query.length >= 2) { "..." }
        return repository.searchAnimeFlow(query)
    }

    // С пагинацией
    suspend operator fun invoke(query: String, limit: Int, offset: Int): List<Anime> {
        require(query.length >= 2) { "..." }
        return repository.searchAnime(query, limit, offset)
    }
}
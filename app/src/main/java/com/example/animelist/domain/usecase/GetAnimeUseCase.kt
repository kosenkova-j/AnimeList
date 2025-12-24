package com.example.animelist.domain.usecase

import com.example.animelist.domain.model.Anime
import com.example.animelist.data.repository.AnimeRepository
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): List<Anime> {
        return repository.getAnime(limit, offset)
    }

    suspend operator fun invoke(animeId: Int): Anime? {
        return repository.getAnimeById(animeId)
    }

    suspend operator fun invoke(status: AnimeStatus?): List<Anime> {
        return if (status == null) {
            repository.getAllAnime()
        } else {
            repository.getAnimeByStatus(status)
        }
    }

    operator fun invoke(): Flow<List<Anime>> {
        return  repository.getAllAnimeFlow()
    }
}
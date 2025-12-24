package com.example.animelist.domain.usecase

import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.data.repository.AnimeRepository
import javax.inject.Inject

class UpdateAnimeStatusUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(animeId: Int, status: AnimeStatus?) {
        repository.updateStatus(animeId, status)
    }

    suspend fun markAsWatched(animeId: Int) {
        repository.updateStatus(animeId, AnimeStatus.COMPLETED)
    }

    suspend fun markAsWatching(animeId: Int) {
        repository.updateStatus(animeId, AnimeStatus.WATCHING)
    }

    suspend fun addToPlanned(animeId: Int) {
        repository.updateStatus(animeId, AnimeStatus.PLANNED)
    }

    suspend fun markAsDropped(animeId: Int) {
        repository.updateStatus(animeId, AnimeStatus.DROPPED)
    }
}
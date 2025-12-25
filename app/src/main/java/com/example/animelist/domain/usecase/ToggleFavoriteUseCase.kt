package com.example.animelist.domain.usecase

import com.example.animelist.domain.repository.AnimeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: AnimeRepository
) {

    suspend operator fun invoke(animeId: Int): Boolean {
        return repository.toggleFavorite(animeId)
    }

    suspend operator fun invoke(animeId: Int, isFavorite: Boolean) {
        repository.setFavorite(animeId, isFavorite)
    }

    suspend fun addToFavorites(animeId: Int) {
        repository.setFavorite(animeId, true)
    }

    suspend fun removeFromFavorites(animeId: Int) {
        repository.setFavorite(animeId, false)
    }
}
package com.example.animelist.domain.usecase

import com.example.animelist.domain.repository.AnimeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * Переключает статус "Избранное" для аниме
     * @param animeId ID аниме
     * @return Новый статус isFavorite (true/false)
     */
    suspend operator fun invoke(animeId: Int): Boolean {
        return repository.toggleFavorite(animeId)
    }

    /**
     * Устанавливает конкретный статус "Избранное"
     * @param animeId ID аниме
     * @param isFavorite Новое значение
     */
    suspend operator fun invoke(animeId: Int, isFavorite: Boolean) {
        repository.setFavorite(animeId, isFavorite)
    }

    /**
     * Добавляет аниме в избранное
     */
    suspend fun addToFavorites(animeId: Int) {
        repository.setFavorite(animeId, true)
    }

    /**
     * Удаляет аниме из избранного
     */
    suspend fun removeFromFavorites(animeId: Int) {
        repository.setFavorite(animeId, false)
    }
}
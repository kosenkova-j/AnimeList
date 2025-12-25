package com.example.animelist.domain.usecase

import com.example.animelist.domain.repository.AnimeRepository
import javax.inject.Inject

class RateAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    /**
     * Обновляет оценку и комментарий пользователя
     * @param animeId ID аниме
     * @param rating Оценка от 1 до 5 (null чтобы удалить)
     * @param comment Комментарий пользователя (null чтобы удалить)
     */
    suspend operator fun invoke(
        animeId: Int,
        rating: Int?,
        comment: String? = null
    ) {
        require(rating == null || rating in 1..10) {
            "Рейтинг должен быть от 1 до 10"
        }

        repository.updateRating(animeId, rating, comment)
    }

    /**
     * Только оценка
     */
    suspend operator fun invoke(animeId: Int, rating: Int) {
        require(rating in 1..5) { "Рейтинг должен быть от 1 до 5" }
        repository.updateRating(animeId, rating, null)
    }

    /**
     * Только комментарий
     */
    suspend operator fun invoke(animeId: Int, comment: String) {
        repository.updateRating(animeId, null, comment)
    }
}
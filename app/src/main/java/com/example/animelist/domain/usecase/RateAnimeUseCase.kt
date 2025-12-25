package com.example.animelist.domain.usecase

import com.example.animelist.domain.repository.AnimeRepository
import javax.inject.Inject

class RateAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(
        animeId: Int,
        rating: Int?,
        comment: String? = null
    ) {
        require(rating == null || rating in 1..10) {
        }
        repository.updateRating(animeId, rating, comment)
    }

    suspend operator fun invoke(animeId: Int, comment: String) {
        repository.updateRating(animeId, null, comment)
    }
}
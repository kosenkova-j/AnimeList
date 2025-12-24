package com.example.animelist.domain.usecase

import com.example.animelist.domain.model.Anime
import javax.inject.Inject

// функция загрузки списка аниме
class GetAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(): List<Anime> {
        return emptyList() // Позже заменим на реальную логику
    }
}
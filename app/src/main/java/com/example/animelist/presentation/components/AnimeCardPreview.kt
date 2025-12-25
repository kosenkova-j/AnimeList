// presentation/components/AnimeCardPreview.kt
package com.example.animelist.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.presentation.theme.AnimeListTheme

// Тестовые данные
private val sampleAnimeNoStatus = Anime(
    id = 1,
    title = "Наруто",
    posterUrl = null,
    description = "История о мальчике-ниндзя, который мечтает стать Хокаге...",
    rating = 8.5,
    episodes = 220,
    season = 1,
    userStatus = null,
    userRating = null,
    userComment = null,
    isFavorite = false
)

private val sampleAnimeWatching = Anime(
    id = 2,
    title = "Атака титанов: Финальный сезон",
    posterUrl = null,
    description = "Эрен и его друзья сражаются за свободу...",
    rating = 9.1,
    episodes = 16,
    season = 4,
    userStatus = AnimeStatus.WATCHING,
    userRating = 9,
    userComment = null,
    isFavorite = true
)

private val sampleAnimeCompleted = Anime(
    id = 3,
    title = "Стальной алхимик: Братство",
    posterUrl = null,
    description = "Братья Элрики ищут философский камень...",
    rating = 9.5,
    episodes = 64,
    season = 1,
    userStatus = AnimeStatus.COMPLETED,
    userRating = 10,
    userComment = null,
    isFavorite = true
)

private val sampleAnimeDropped = Anime(
    id = 4,
    title = "Какое-то аниме",
    posterUrl = null,
    description = "Описание...",
    rating = 6.0,
    episodes = 12,
    season = 1,
    userStatus = AnimeStatus.DROPPED,
    userRating = 4,
    userComment = null,
    isFavorite = false
)

// Preview функции
@Preview(showBackground = true, name = "Не просмотрено")
@Composable
private fun AnimeCardNoStatusPreview() {
    AnimeListTheme {
        AnimeCard(
            anime = sampleAnimeNoStatus,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Смотрю")
@Composable
private fun AnimeCardWatchingPreview() {
    AnimeListTheme {
        AnimeCard(
            anime = sampleAnimeWatching,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Просмотрено")
@Composable
private fun AnimeCardCompletedPreview() {
    AnimeListTheme {
        AnimeCard(
            anime = sampleAnimeCompleted,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Брошено")
@Composable
private fun AnimeCardDroppedPreview() {
    AnimeListTheme {
        AnimeCard(
            anime = sampleAnimeDropped,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}
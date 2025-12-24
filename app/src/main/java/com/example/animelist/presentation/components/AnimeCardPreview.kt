// presentation/components/AnimeCardPreview.kt
package com.example.animelist.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.presentation.theme.AnimeListTheme

@Preview(showBackground = true)
@Composable
fun AnimeCardPreview() {
    AnimeListTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Аниме с полной информацией
                AnimeCard(
                    anime = Anime(
                        id = 1,
                        title = "Атака титанов: Финальный сезон",
                        posterUrl = "https://example.com/poster.jpg",
                        description = "Эрен Йегер и его друзья сражаются против титанов, чтобы защитить человечество и раскрыть тайны своего мира.",
                        rating = 9.1,
                        episodes = 75,
                        userStatus = AnimeStatus.COMPLETED,
                        userRating = 5,
                        userComment = "Шедевр!",
                        isFavorite = true
                    ),
                    onCardClick = {},
                    onFavoriteClick = {}
                )

                // Аниме без статуса пользователя
                AnimeCard(
                    anime = Anime(
                        id = 2,
                        title = "Моя геройская академия",
                        posterUrl = null,
                        description = "В мире, где у большинства людей есть сверхспособности, мальчик без способностей мечтает стать величайшим героем.",
                        rating = 8.3,
                        episodes = 138,
                        userStatus = null,
                        userRating = null,
                        userComment = null,
                        isFavorite = false
                    ),
                    onCardClick = {},
                    onFavoriteClick = {}
                )

                // Аниме в статусе "Смотрю"
                AnimeCard(
                    anime = Anime(
                        id = 3,
                        title = "Спуск в бездну",
                        posterUrl = "https://example.com/made-in-abyss.jpg",
                        description = "Дети исследуют таинственную бездну в поисках сокровищ и разгадки её тайн.",
                        rating = 8.8,
                        episodes = 13,
                        userStatus = AnimeStatus.WATCHING,
                        userRating = 4,
                        userComment = null,
                        isFavorite = false
                    ),
                    onCardClick = {},
                    onFavoriteClick = {}
                )
            }
        }
    }
}
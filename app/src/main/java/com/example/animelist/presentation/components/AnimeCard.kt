package com.example.animelist.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus

@Composable
fun AnimeCard(
    anime: Anime,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    showStatus: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Постер аниме
            PosterImage(
                posterUrl = anime.posterUrl,
                modifier = Modifier
                    .width(80.dp)
                    .height(112.dp)
            )

            // Информация об аниме
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Заголовок и кнопка избранного
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Название аниме
                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Кнопка избранного
                    FavoriteButton(
                        isFavorite = anime.isFavorite,
                        onClick = onFavoriteClick
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Описание (если есть)
                anime.description?.let { description ->
                    if (description.isNotEmpty()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                // Рейтинг аниме (от API)
                AnimeRating(
                    rating = anime.rating,
                    modifier = Modifier.fillMaxWidth()
                )

                // Статус пользователя
                if (showStatus) {
                    Spacer(modifier = Modifier.height(8.dp))
                    UserStatusSection(
                        userStatus = anime.userStatus,
                        userRating = anime.userRating,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// Компонент постера
@Composable
private fun PosterImage(
    posterUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (posterUrl != null) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Постер аниме",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Заглушка, если нет постера
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет изображения",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Кнопка избранного
@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(24.dp)
    ) {
        Icon(
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            },
            contentDescription = if (isFavorite) {
                "Удалить из избранного"
            } else {
                "Добавить в избранное"
            },
            tint = if (isFavorite) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    }
}

// Рейтинг аниме (от API)
@Composable
private fun AnimeRating(
    rating: Double?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = if (rating != null) {
                String.format("%.1f", rating)
            } else {
                "Н/Д"
            },
            style = MaterialTheme.typography.labelMedium,
            color = if (rating != null) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Text(
            text = "/10",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

// Звёздочки рейтинга пользователя
@Composable
private fun UserRatingStars(
    rating: Int,
    maxRating: Int = 10,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// presentation/components/AnimeCard.kt

@Composable
private fun UserRatingDisplay(
    rating: Int,  // 1-10
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Одна звезда как иконка
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(16.dp)
        )

        // Оценка числом
        Text(
            text = "$rating/10",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

// Используем в UserStatusSection:
@Composable
private fun UserStatusSection(
    userStatus: AnimeStatus?,
    userRating: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusChip(
            label = getStatusDisplayName(userStatus),
            isSelected = userStatus != null,
            onClick = {},
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        userRating?.let { rating ->
            UserRatingDisplay(rating = rating)  // ← Новый компонент
        }
    }
}

// Вспомогательная функция для отображения статусов
private fun getStatusDisplayName(status: AnimeStatus?): String {
    return when (status) {
        AnimeStatus.WATCHING -> "Смотрю"
        AnimeStatus.COMPLETED -> "Просмотрено"
        AnimeStatus.PLANNED -> "В планах"
        AnimeStatus.DROPPED -> "Брошено"
        AnimeStatus.FAVORITE -> "Любимое"
        null -> "Не просмотрено"
    }
}

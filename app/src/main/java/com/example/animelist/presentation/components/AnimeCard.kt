// presentation/components/AnimeCard.kt
package com.example.animelist.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.presentation.theme.CompletedAnime
import com.example.animelist.presentation.theme.CoralAccent
import com.example.animelist.presentation.theme.DroppedAnime
import com.example.animelist.presentation.theme.FavoriteAnime
import com.example.animelist.presentation.theme.PlannedAnime

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
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Постер с градиентом и бейджем рейтинга
            Box {
                PosterImage(
                    posterUrl = anime.posterUrl,
                    modifier = Modifier
                        .width(100.dp)
                        .height(140.dp)
                )

                // Бейдж рейтинга на постере
                anime.rating?.let { rating ->
                    RatingBadge(
                        rating = rating,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    )
                }

                // Кнопка избранного
                FavoriteButton(
                    isFavorite = anime.isFavorite,
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }

            // Информация
            Column(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 140.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Название
                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Описание в баббле
                    anime.description?.takeIf { it.isNotEmpty() }?.let { description ->
                        DescriptionBubble(
                            text = description,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Эпизоды + Статус пользователя
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Количество эпизодов
                    InfoBadgesRow(
                        episodes = anime.episodes,
                        season = anime.season
                    )
                    // Статус и оценка пользователя
                    if (showStatus) {
                        UserStatusRow(
                            userStatus = anime.userStatus,
                            userRating = anime.userRating
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoBadgesRow(
    episodes: Int?,
    season: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Сезон
        season?.let { s ->
            InfoBadge(
                icon = Icons.Filled.DateRange,
                text = "$s сезон"
            )
        }

        // Эпизоды
        episodes?.let { ep ->
            InfoBadge(
                icon = Icons.Filled.PlayArrow,
                text = "$ep эп."
            )
        }
    }
}

@Composable
private fun InfoBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun PosterImage(
    posterUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (posterUrl != null) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Постер",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Градиент снизу для лучшей читаемости
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет\nфото",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(28.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.9f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Убрать из избранного" else "В избранное",
                tint = if (isFavorite) Color(0xFFE91E63) else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun DescriptionBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
private fun EpisodesBadge(
    episodes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "$episodes эп.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun UserStatusRow(
    userStatus: AnimeStatus?,
    userRating: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = getStatusColor(userStatus)
        ) {
            Text(
                text = getStatusDisplayName(userStatus),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = if (userStatus != null) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Оценка пользователя
        userRating?.let { rating ->
            UserRatingBadge(rating = rating)
        }
    }
}

@Composable
private fun UserRatingBadge(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "$rating",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun getStatusColor(status: AnimeStatus?): Color {
    return when (status) {
        AnimeStatus.COMPLETED -> CompletedAnime    // Зелёный
        AnimeStatus.FAVORITE -> FavoriteAnime      // Оранжевый
        AnimeStatus.DROPPED -> DroppedAnime        // Красный
        AnimeStatus.PLANNED -> PlannedAnime        // Синий
        AnimeStatus.WATCHING -> CoralAccent  // Фиолетовый (или свой цвет)
        null -> MaterialTheme.colorScheme.primaryContainer  // Дефолтный персиковый
    }
}

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
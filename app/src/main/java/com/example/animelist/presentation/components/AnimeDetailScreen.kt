// presentation/screen/detail/AnimeDetailPreview.kt
package com.example.animelist.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.presentation.theme.AnimeListTheme

// ============ ТЕСТОВЫЕ ДАННЫЕ ============

private val sampleAnimeNoStatus = Anime(
    id = 1,
    title = "Наруто: Ураганные хроники",
    posterUrl = null,
    description = "Спустя два с половиной года тренировок с одним из легендарных саннинов, Наруто возвращается в Коноху. Вместе с Сакурой они заново формируют Команду Какаши. Однако им предстоит столкнуться с новыми врагами — организацией Акацуки, которая охотится за джинчурики.",
    rating = 8.5,
    episodes = 500,
    season = 2,
    userStatus = null,
    userRating = null,
    userComment = null,
    isFavorite = false
)

private val sampleAnimeWatching = Anime(
    id = 2,
    title = "Атака титанов: Финальный сезон",
    posterUrl = null,
    description = "Несколько лет назад человечество было почти полностью уничтожено титанами. Титаны — огромные создания, которые, казалось бы, не имеют другой цели, кроме как пожирать людей. Оставшиеся в живых укрылись за тремя концентрическими стенами.",
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
    description = "Братья Эдвард и Альфонс Элрики в детстве потеряли мать и решили воскресить её с помощью алхимии. Однако что-то пошло не так, и Эдвард потерял ногу, а Альфонс — всё тело. Чтобы спасти душу брата, Эд пожертвовал правой рукой и прикрепил душу Альфонса к доспехам.",
    rating = 9.5,
    episodes = 64,
    season = 1,
    userStatus = AnimeStatus.COMPLETED,
    userRating = 10,
    userComment = null,
    isFavorite = true
)

private val sampleAnimePlanned = Anime(
    id = 4,
    title = "Магическая битва",
    posterUrl = null,
    description = "Итадори Юдзи — обычный школьник с невероятной физической силой. Однажды он проглатывает проклятый палец демона Рёмена Сукуны, чтобы спасти друзей. Теперь в его теле живёт сильнейшее проклятие, а сам Юдзи становится учеником школы магов.",
    rating = 8.8,
    episodes = 24,
    season = 1,
    userStatus = AnimeStatus.PLANNED,
    userRating = null,
    userComment = null,
    isFavorite = false
)

private val sampleAnimeDropped = Anime(
    id = 5,
    title = "Боруто: Новое поколение",
    posterUrl = null,
    description = "Сын Наруто, Боруто, начинает свой путь ниндзя. История нового поколения героев Конохи.",
    rating = 6.5,
    episodes = 293,
    season = 1,
    userStatus = AnimeStatus.DROPPED,
    userRating = 4,
    userComment = null,
    isFavorite = false
)

private val sampleAnimeFavorite = Anime(
    id = 6,
    title = "Ванпанчмен",
    posterUrl = null,
    description = "Сайтама — герой, который может победить любого противника одним ударом. Но это делает его жизнь невыносимо скучной. Он ищет достойного соперника, но никак не может найти.",
    rating = 9.0,
    episodes = 24,
    season = 2,
    userStatus = AnimeStatus.FAVORITE,
    userRating = 10,
    userComment = null,
    isFavorite = true
)

// ============ PREVIEW КОНТЕНТА ============

@Composable
private fun PreviewDetailContent(anime: Anime) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Шапка с постером
        PreviewHeaderSection(anime = anime)

        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-24).dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Название
            Text(
                text = anime.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Инфо чипсы
            PreviewInfoChipsRow(anime = anime)

            // Кнопки действий
            PreviewActionButtonsSection(
                userStatus = anime.userStatus,
                userRating = anime.userRating
            )

            // Описание
            PreviewDescriptionSection(description = anime.description)
        }
    }
}

@Composable
private fun PreviewHeaderSection(anime: Anime) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Заглушка постера
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ПОСТЕР",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Градиент сверху
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Градиент снизу
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // Кнопка назад
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .size(40.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        }

        // Кнопка избранного
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .size(40.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (anime.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Избранное",
                    tint = if (anime.isFavorite) Color(0xFFFF6B6B) else Color.White
                )
            }
        }

        // Рейтинг
        anime.rating?.let { rating ->
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = String.format("%.1f", rating),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewInfoChipsRow(anime: Anime) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        anime.season?.let { season ->
            PreviewInfoChip(
                icon = Icons.Filled.DateRange,
                text = "$season сезон"
            )
        }

        anime.episodes?.let { episodes ->
            PreviewInfoChip(
                icon = Icons.Filled.PlayArrow,
                text = "$episodes эп."
            )
        }
    }
}

@Composable
private fun PreviewInfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PreviewActionButtonsSection(
    userStatus: AnimeStatus?,
    userRating: Int?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Мой список",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PreviewActionButton(
                icon = Icons.Filled.List,
                title = "Статус",
                subtitle = getStatusDisplayName(userStatus),
                isActive = userStatus != null,
                modifier = Modifier.weight(1f)
            )

            PreviewActionButton(
                icon = Icons.Filled.Star,
                title = "Оценка",
                subtitle = userRating?.let { "$it / 10" } ?: "Не оценено",
                isActive = userRating != null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PreviewActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = if (isActive) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PreviewDescriptionSection(description: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Описание",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ) {
            Text(
                text = description ?: "Описание отсутствует",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Justify,
                lineHeight = 22.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private fun getStatusDisplayName(status: AnimeStatus?): String {
    return when (status) {
        AnimeStatus.WATCHING -> "Смотрю"
        AnimeStatus.COMPLETED -> "Просмотрено"
        AnimeStatus.PLANNED -> "В планах"
        AnimeStatus.DROPPED -> "Брошено"
        AnimeStatus.FAVORITE -> "Любимое"
        null -> "Не выбрано"
    }
}

// ============ PREVIEW ФУНКЦИИ ============

@Preview(showBackground = true, showSystemUi = true, name = "Без статуса")
@Composable
private fun DetailScreenNoStatusPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeNoStatus)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Смотрю")
@Composable
private fun DetailScreenWatchingPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeWatching)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Просмотрено")
@Composable
private fun DetailScreenCompletedPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeCompleted)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "В планах")
@Composable
private fun DetailScreenPlannedPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimePlanned)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Брошено")
@Composable
private fun DetailScreenDroppedPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeDropped)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Любимое")
@Composable
private fun DetailScreenFavoritePreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeFavorite)
    }
}

// Тёмная тема
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Тёмная тема",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DetailScreenDarkPreview() {
    AnimeListTheme {
        PreviewDetailContent(anime = sampleAnimeCompleted)
    }
}
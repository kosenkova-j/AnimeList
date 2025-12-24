package com.example.animelist.presentation.screen.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animelist.domain.model.Anime
import com.example.animelist.presentation.components.AnimeCard

// элемент списка
@Composable
fun AnimeList(
    animeList: List<Anime>,
    onAnimeClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(animeList, key = { it.id }) { anime ->
            AnimeCard(
                anime = anime,
                onCardClick = { onAnimeClick(anime.id) },
                onFavoriteClick = { onToggleFavorite(anime.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
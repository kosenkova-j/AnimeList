package com.example.animelist.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animelist.domain.model.Anime
import com.example.animelist.presentation.components.AnimeCard

// элемент списка
// presentation/screen/home/components/AnimeList.kt

@Composable
fun AnimeList(
    animeList: List<Anime>,
    onAnimeClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onLoadMore: () -> Unit,           // ← Новый параметр
    isLoadingMore: Boolean = false,   // ← Новый параметр
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Детектим когда доскроллили до конца
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
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

        // Индикатор загрузки внизу
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
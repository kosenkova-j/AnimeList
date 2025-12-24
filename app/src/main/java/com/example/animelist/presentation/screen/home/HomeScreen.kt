package com.example.animelist.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animelist.domain.model.Anime
import com.example.animelist.presentation.components.AnimeCard
import com.example.animelist.presentation.screen.home.components.AnimeList
import com.example.animelist.presentation.screen.home.components.SearchSection

// В HomeScreen.kt замените AnimeList на:
//@Composable
//fun AnimeList(
//    animeList: List<Anime>,
//    onAnimeClick: (Int) -> Unit,
//    onToggleFavorite: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier,
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(animeList.size, key = { it.id }) { anime ->
//            AnimeCard(
//                anime = anime,
//                onCardClick = { onAnimeClick(anime.id) },
//                onFavoriteClick = { onToggleFavorite(anime.id) },
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}

// главный экран
@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // поиск и фильтры
            SearchSection(
                searchQuery = uiState.searchQuery,
                selectedStatus = uiState.selectedStatus,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onStatusSelected = viewModel::onStatusSelected,
                onSearchClick = onSearchClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // список
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ошибка загрузки: ${uiState.error}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                // Перезагрузить через обновление запроса
                                viewModel.onSearchQueryChange("")
                            }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                uiState.animeList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.searchQuery.isNotEmpty()) {
                                "Ничего не найдено"
                            } else {
                                "Список аниме пуст"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    AnimeList(
                        animeList = uiState.animeList,
                        onAnimeClick = onAnimeClick,
                        onToggleFavorite = viewModel::onToggleFavorite,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
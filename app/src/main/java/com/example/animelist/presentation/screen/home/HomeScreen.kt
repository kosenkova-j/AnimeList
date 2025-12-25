package com.example.animelist.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animelist.presentation.screen.home.components.SearchSection

// главный экран
@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        println("=== HOME SCREEN UPDATE ===")
        println("isLoading: ${uiState.isLoading}")
        println("error: ${uiState.error}")
        println("animeList size: ${uiState.animeList.size}")
        if (uiState.animeList.isNotEmpty()) {
            println("First anime: ${uiState.animeList.first().title}")
        }
    }

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
                        onLoadMore = viewModel::loadMore,           // ← Новое
                        isLoadingMore = uiState.isLoadingMore,      // ← Новое
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
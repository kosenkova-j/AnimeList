package com.example.animelist.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animelist.R
import com.example.animelist.presentation.screen.home.components.AnimeList
import com.example.animelist.presentation.screen.home.components.HomeTopBar
import com.example.animelist.presentation.screen.home.components.StatusFilterChips


// главный экран
@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    // Загружаем аниме при первом открытии
    LaunchedEffect(Unit) {
        viewModel.loadAnime()
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchClick = onSearchClick
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Фильтры по статусу
                StatusFilterChips(
                    selectedStatus = uiState.selectedStatus,
                    onStatusSelected = viewModel::onStatusSelected
                )

                // Список аниме
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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Ошибка загрузки",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { viewModel.loadAnime() }) {
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
                                text = "Список аниме пуст",
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
    )
}
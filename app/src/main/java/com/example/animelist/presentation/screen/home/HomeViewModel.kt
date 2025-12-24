package com.example.animelist.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.domain.usecase.GetAnimeUseCase
import com.example.animelist.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// логика хоумскрина
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimeUseCase: GetAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val anime = getAnimeUseCase()
                _uiState.update {
                    it.copy(
                        animeList = anime,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Неизвестная ошибка",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onStatusSelected(status: AnimeStatus?) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedStatus = status) }
            // Здесь будет фильтрация по статусу
            loadAnime() // Временное решение
        }
    }

    fun onToggleFavorite(animeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(animeId)
            // Обновляем список
            _uiState.update { state ->
                state.copy(
                    animeList = state.animeList.map { anime ->
                        if (anime.id == animeId) {
                            anime.copy(isFavorite = !anime.isFavorite)
                        } else anime
                    }
                )
            }
        }
    }
}

data class HomeUiState(
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedStatus: AnimeStatus? = null,
    val searchQuery: String = ""
)
// presentation/screen/detail/AnimeDetailViewModel.kt
package com.example.animelist.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.domain.repository.AnimeRepository
import com.example.animelist.domain.usecase.RateAnimeUseCase
import com.example.animelist.domain.usecase.ToggleFavoriteUseCase
import com.example.animelist.domain.usecase.UpdateAnimeStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimeDetailUiState(
    val anime: Anime? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showStatusDialog: Boolean = false,
    val showRatingDialog: Boolean = false
)

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AnimeRepository,
    private val updateStatusUseCase: UpdateAnimeStatusUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val rateAnimeUseCase: RateAnimeUseCase
) : ViewModel() {

    private val animeId: Int = savedStateHandle.get<Int>("animeId") ?: -1

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    init {
        loadAnime()
    }

    private fun loadAnime() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            println("AnimeDetailViewModel: loading anime $animeId")

            try {
                val anime = repository.getAnimeById(animeId)
                println("AnimeDetailViewModel: result = $anime")

                if (anime != null) {
                    _uiState.update { it.copy(anime = anime, isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(error = "Аниме не найдено (id=$animeId)", isLoading = false)
                    }
                }
            } catch (e: Exception) {
                println("AnimeDetailViewModel: exception ${e.message}")
                e.printStackTrace()
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onStatusClick() {
        _uiState.update { it.copy(showStatusDialog = true) }
    }

    fun onDismissStatusDialog() {
        _uiState.update { it.copy(showStatusDialog = false) }
    }

    fun onStatusSelected(status: AnimeStatus?) {
        viewModelScope.launch {
            updateStatusUseCase(animeId, status)
            _uiState.update { state ->
                state.copy(
                    anime = state.anime?.copy(userStatus = status),
                    showStatusDialog = false
                )
            }
        }
    }

    fun onRatingClick() {
        _uiState.update { it.copy(showRatingDialog = true) }
    }

    fun onDismissRatingDialog() {
        _uiState.update { it.copy(showRatingDialog = false) }
    }

    fun onRatingSelected(rating: Int) {
        viewModelScope.launch {
            rateAnimeUseCase(animeId, rating, null)
            _uiState.update { state ->
                state.copy(
                    anime = state.anime?.copy(userRating = rating),
                    showRatingDialog = false
                )
            }
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            val newFavorite = toggleFavoriteUseCase(animeId)
            _uiState.update { state ->
                state.copy(anime = state.anime?.copy(isFavorite = newFavorite))
            }
        }
    }

    fun refresh() {
        loadAnime()
    }
}
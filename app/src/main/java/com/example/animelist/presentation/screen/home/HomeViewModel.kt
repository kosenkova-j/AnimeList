package com.example.animelist.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animelist.data.local.dao.AnimeDao
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.domain.usecase.GetAnimeUseCase
import com.example.animelist.domain.usecase.InitializeCacheUseCase
import com.example.animelist.domain.usecase.SearchAnimeUseCase
import com.example.animelist.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// логика хоумскрина
// presentation/screen/home/HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimeUseCase: GetAnimeUseCase,      // Flow<List<Anime>>
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val initializeCacheUseCase: InitializeCacheUseCase
) : ViewModel() {

    // Состояния UI
    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<AnimeStatus?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    init {
        // Инициализация кэша
        viewModelScope.launch {
            initializeCacheUseCase()
            _isLoading.value = false  // Загрузка завершена
        }
    }

    // Flow данных из UseCase
    private val animeListFlow = getAnimeUseCase()

    // StateFlow для UI
    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedStatus,
        animeListFlow,      // ← Поток данных БЕЗ промежуточного _animeList
        _isLoading,
        _error
    ) { query, status, animeList, isLoading, error ->
        HomeUiState(
            searchQuery = query,
            selectedStatus = status,
            animeList = filterAndSearchAnime(animeList, query, status),
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    // Фильтрация и поиск
    private fun filterAndSearchAnime(
        animeList: List<Anime>,
        query: String,
        status: AnimeStatus?
    ): List<Anime> {
        return animeList.filter { anime ->
            val matchesSearch = query.isEmpty() ||
                    anime.title.contains(query, ignoreCase = true)

            val matchesStatus = when (status) {
                null -> true
                AnimeStatus.FAVORITE -> anime.isFavorite
                else -> anime.userStatus == status
            }

            matchesSearch && matchesStatus
        }
    }

    // === ПУБЛИЧНЫЕ МЕТОДЫ ===

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onStatusSelected(status: AnimeStatus?) {
        _selectedStatus.value = status
    }

    fun onToggleFavorite(animeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(animeId)
            // Автоматически обновится через animeListFlow
        }
    }

    fun retryLoad() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            initializeCacheUseCase()
            _isLoading.value = false
        }
    }
}

data class HomeUiState(
    val searchQuery: String = "",
    val selectedStatus: AnimeStatus? = null,
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
package com.example.animelist.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animelist.data.local.dao.AnimeDao
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.domain.repository.AnimeRepository
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
    private val getAnimeUseCase: GetAnimeUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val initializeCacheUseCase: InitializeCacheUseCase,
    private val repository: AnimeRepository  // ← Добавляем для пагинации
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<AnimeStatus?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _isLoadingMore = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _canLoadMore = MutableStateFlow(true)

    private var currentOffset = 0
    private val pageSize = 30

    init {
        viewModelScope.launch {
            initializeCacheUseCase()
            _isLoading.value = false
        }
    }

    private val animeListFlow = getAnimeUseCase()

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedStatus,
        animeListFlow,
        _isLoading,
        _isLoadingMore,
        _error,
        _canLoadMore
    ) { values ->
        val query = values[0] as String
        val status = values[1] as AnimeStatus?
        val animeList = values[2] as List<Anime>
        val isLoading = values[3] as Boolean
        val isLoadingMore = values[4] as Boolean
        val error = values[5] as String?
        val canLoadMore = values[6] as Boolean

        HomeUiState(
            searchQuery = query,
            selectedStatus = status,
            animeList = filterAndSearchAnime(animeList, query, status),
            isLoading = isLoading,
            isLoadingMore = isLoadingMore,
            error = error,
            canLoadMore = canLoadMore
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

    fun loadMore() {
        if (_isLoadingMore.value || !_canLoadMore.value || _searchQuery.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            currentOffset += pageSize

            println("Loading more: offset = $currentOffset")

            val newAnime = repository.getAnime(limit = pageSize, offset = currentOffset)

            if (newAnime.isEmpty()) {
                _canLoadMore.value = false
                println("No more anime to load")
            } else {
                println("Loaded ${newAnime.size} more anime")
            }

            _isLoadingMore.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Если поиск — ищем через API
        if (query.length >= 2) {
            viewModelScope.launch {
                _isLoading.value = true
                repository.searchAnime(query)
                _isLoading.value = false
            }
        }
    }

    // === ПУБЛИЧНЫЕ МЕТОДЫ ===


    fun onStatusSelected(status: AnimeStatus?) {
        _selectedStatus.value = status
        _searchQuery.value = ""
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
    val isLoadingMore: Boolean = false,  // ← Новое
    val error: String? = null,
    val canLoadMore: Boolean = true       // ← Новое
)
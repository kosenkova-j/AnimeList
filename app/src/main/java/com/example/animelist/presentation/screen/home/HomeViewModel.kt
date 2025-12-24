package com.example.animelist.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.domain.usecase.GetAnimeUseCase
import com.example.animelist.domain.usecase.SearchAnimeUseCase
import com.example.animelist.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// логика хоумскрина
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimeUseCase: GetAnimeUseCase,
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<AnimeStatus?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedStatus,
        _isLoading,
        _error,
        _animeList
    ) { searchQuery, selectedStatus, isLoading, error, animeList ->
        HomeUiState(
            searchQuery = searchQuery,
            selectedStatus = selectedStatus,
            isLoading = isLoading,
            error = error,
            animeList = filterAnime(animeList, searchQuery, selectedStatus)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    private var searchJob: Job? = null

    init {
        // автопоиск при изменении запроса
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Ждём 500мс после последнего ввода
                .distinctUntilChanged()
                .collect { query ->
                    if (query.length >= 2 || query.isEmpty()) {
                        loadAnime()
                    }
                }
        }
    }

    fun loadAnime() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val query = _searchQuery.value
                val anime = if (query.length >= 2) {
                    searchAnimeUseCase(query)
                } else {
                    getAnimeUseCase()
                }
                _animeList.value = anime
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onStatusSelected(status: AnimeStatus?) {
        _selectedStatus.value = status
    }

    fun onToggleFavorite(animeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(animeId)
            // обновляем список
            _animeList.update { list ->
                list.map { anime ->
                    if (anime.id == animeId) {
                        anime.copy(isFavorite = !anime.isFavorite)
                    } else anime
                }
            }
        }
    }

    private fun filterAnime(
        animeList: List<Anime>,
        searchQuery: String,
        status: AnimeStatus?
    ): List<Anime> {
        return animeList.filter { anime ->
            val matchesSearch = searchQuery.isEmpty() ||
                    anime.title.contains(searchQuery, ignoreCase = true) ||
                    anime.description?.contains(searchQuery, ignoreCase = true) == true

            val matchesStatus = when (status) {
                null -> true
                AnimeStatus.FAVORITE -> anime.isFavorite
                else -> anime.userStatus == status
            }

            matchesSearch && matchesStatus
        }
    }
}

data class HomeUiState(
    val searchQuery: String = "",
    val selectedStatus: AnimeStatus? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val animeList: List<Anime> = emptyList()
)
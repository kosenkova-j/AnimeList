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

    private val _animeList =  MutableStateFlow<List<Anime>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<AnimeStatus?>(null)

    init {
        // Загружаем данные один раз при создании ViewModel
        viewModelScope.launch {
            getAnimeUseCase().collect { list ->
                _animeList.value = list
            }
        }
    }

    // StateFlow для UI
    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedStatus,
        getAnimeListFlow() // ← Flow из UseCase
    ) { query, status, animeList ->
        HomeUiState(
            searchQuery = query,
            selectedStatus = status,
            animeList = filterAnime(animeList, query, status),
            isLoading = false,
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    // Flow из UseCase с переключением между поиском и всеми аниме
    private fun getAnimeListFlow(): Flow<List<Anime>> {
        return _searchQuery.flatMapLatest { query ->
            if (query.length >= 2) {
                searchAnimeUseCase(query)
            } else {
                getAnimeUseCase()
            }
        }
    }

    // Остальные методы без изменений
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
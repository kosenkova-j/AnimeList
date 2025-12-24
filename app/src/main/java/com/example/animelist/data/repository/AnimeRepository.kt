// domain/repository/AnimeRepository.kt
package com.example.animelist.data.repository

import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    // Получение аниме
    suspend fun getAllAnime(): List<Anime>
    suspend fun getAnime(limit: Int, offset: Int): List<Anime>
    suspend fun getAnimeById(id: Int): Anime?
    suspend fun getAnimeByStatus(status: AnimeStatus): List<Anime>
    fun getAllAnimeFlow(): Flow<List<Anime>>

    // Поиск
    suspend fun searchAnime(query: String): List<Anime>
    suspend fun searchAnime(query: String, limit: Int, offset: Int): List<Anime>
    fun searchAnimeFlow(query: String): Flow<List<Anime>>

    // Управление избранным
    suspend fun toggleFavorite(animeId: Int): Boolean
    suspend fun setFavorite(animeId: Int, isFavorite: Boolean)

    // Управление статусами (для следующих use cases)
    suspend fun updateStatus(animeId: Int, status: AnimeStatus?)
    suspend fun updateRating(animeId: Int, rating: Int?, comment: String?)

    // Синхронизация с API
    suspend fun syncWithApi()
    suspend fun syncFavorites()
}
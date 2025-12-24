// data/repository/AnimeRepository.kt
package com.example.animelist.data.repository

import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    // === ОСНОВНОЕ ПОЛУЧЕНИЕ ДАННЫХ ===
    suspend fun getAllAnime(): List<Anime>
    suspend fun getAnime(limit: Int, offset: Int): List<Anime>
    suspend fun getAnimeById(id: Int): Anime?
    suspend fun getAnimeByStatus(status: AnimeStatus): List<Anime>

    // === FLOW ВЕРСИИ (для Compose) ===
    fun getAllAnimeFlow(): Flow<List<Anime>>

    // === ПОИСК ===
    suspend fun searchAnime(query: String): List<Anime>
    suspend fun searchAnime(query: String, limit: Int, offset: Int): List<Anime>
    fun searchAnimeFlow(query: String): Flow<List<Anime>>

    // === УПРАВЛЕНИЕ ИЗБРАННЫМ ===
    suspend fun toggleFavorite(animeId: Int): Boolean
    suspend fun setFavorite(animeId: Int, isFavorite: Boolean)

    // === УПРАВЛЕНИЕ СТАТУСАМИ И ОЦЕНКАМИ ===
    suspend fun updateStatus(animeId: Int, status: AnimeStatus?)
    suspend fun updateRating(animeId: Int, rating: Int?, comment: String?)

    // === КЭШИРОВАНИЕ И СИНХРОНИЗАЦИЯ ===
    suspend fun initializeCache()
    suspend fun syncWithApi()
    suspend fun syncFavorites()

    // === ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ===
    suspend fun clearCache()
    suspend fun getCachedAnimeCount(): Int
}
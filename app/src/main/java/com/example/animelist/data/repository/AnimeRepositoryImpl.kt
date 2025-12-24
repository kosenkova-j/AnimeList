// data/repository/AnimeRepositoryImpl.kt
package com.example.animelist.data.repository

import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.data.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor() : AnimeRepository {

    override suspend fun getAllAnime(): List<Anime> {
        // Временная заглушка
        return emptyList()
    }

    override suspend fun getAnime(limit: Int, offset: Int): List<Anime> {
        return emptyList()
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        return null
    }

    override suspend fun getAnimeByStatus(status: AnimeStatus): List<Anime> {
        return emptyList()
    }

    override fun getAllAnimeFlow(): Flow<List<Anime>> {
        return flowOf(emptyList())
    }

    override suspend fun searchAnime(query: String): List<Anime> {
        return emptyList()
    }

    override suspend fun searchAnime(query: String, limit: Int, offset: Int): List<Anime> {
        return emptyList()
    }

    override fun searchAnimeFlow(query: String): Flow<List<Anime>> {
        return flowOf(emptyList())
    }

    override suspend fun toggleFavorite(animeId: Int): Boolean {
        return false
    }

    override suspend fun setFavorite(animeId: Int, isFavorite: Boolean) {
        // Заглушка
    }

    override suspend fun updateStatus(animeId: Int, status: AnimeStatus?) {
        // Заглушка
    }

    override suspend fun updateRating(animeId: Int, rating: Int?, comment: String?) {
        // Заглушка
    }

    override suspend fun syncWithApi() {
        // Заглушка
    }

    override suspend fun syncFavorites() {
        // Заглушка
    }
}
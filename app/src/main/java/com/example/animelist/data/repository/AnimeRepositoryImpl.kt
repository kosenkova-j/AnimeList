// data/repository/AnimeRepositoryImpl.kt (обновлённая)
package com.example.animelist.data.repository

import com.example.animelist.data.remote.YummyAnimeDataSource
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: YummyAnimeDataSource
) : AnimeRepository {

    override suspend fun getAllAnime(): List<Anime> {
        return remoteDataSource.getAnime()
            .fold(
                onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
                onFailure = { emptyList() }
            )
    }

    override suspend fun getAnime(limit: Int, offset: Int): List<Anime> {
        return remoteDataSource.getAnime(limit, offset)
            .fold(
                onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
                onFailure = { emptyList() }
            )
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        // Позже реализуем
        return null
    }

    override suspend fun getAnimeByStatus(status: AnimeStatus): List<Anime> {
        // Позже реализуем с Room
        return emptyList()
    }

    override fun getAllAnimeFlow(): Flow<List<Anime>> = flow {
        val anime = getAllAnime()
        emit(anime)
    }

    override suspend fun searchAnime(query: String): List<Anime> {
        return remoteDataSource.searchAnime(query)
            .fold(
                onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
                onFailure = { emptyList() }
            )
    }

    override suspend fun searchAnime(query: String, limit: Int, offset: Int): List<Anime> {
        return remoteDataSource.searchAnime(query, limit, offset)
            .fold(
                onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
                onFailure = { emptyList() }
            )
    }

    override fun searchAnimeFlow(query: String): Flow<List<Anime>> = flow {
        val anime = searchAnime(query)
        emit(anime)
    }

    override suspend fun toggleFavorite(animeId: Int): Boolean {
        // Позже реализуем
        return false
    }

    override suspend fun setFavorite(animeId: Int, isFavorite: Boolean) {
        // Позже реализуем
    }

    override suspend fun updateStatus(animeId: Int, status: AnimeStatus?) {
        // Позже реализуем
    }

    override suspend fun updateRating(animeId: Int, rating: Int?, comment: String?) {
        // Позже реализуем
    }

    override suspend fun syncWithApi() {
        // Позже реализуем
    }

    override suspend fun syncFavorites() {
        // Позже реализуем
    }

    // Вспомогательная функция преобразования DTO → Domain
    private fun AnimeDto.toDomain(): Anime {
        return Anime(
            id = id,
            title = titleRu ?: title,
            posterUrl = poster,
            description = description,
            rating = rating,
            episodes = episodes,
            userStatus = null, // Позже из Room
            userRating = null,
            userComment = null,
            isFavorite = false
        )
    }
}
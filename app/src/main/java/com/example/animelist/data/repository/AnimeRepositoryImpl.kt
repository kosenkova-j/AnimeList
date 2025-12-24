// data/repository/AnimeRepositoryImpl.kt
package com.example.animelist.data.repository

import com.example.animelist.data.local.dao.AnimeDao
import com.example.animelist.data.local.dao.UserAnimeDao
import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.data.remote.YummyAnimeDataSource
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: YummyAnimeDataSource,
    private val animeDao: AnimeDao,
    private val userAnimeDao: UserAnimeDao
) : AnimeRepository {

    // === ПРЕОБРАЗОВАНИЯ ===
    private fun AnimeDto.toEntity(): AnimeEntity {
        return AnimeEntity(
            id = id,
            title = title,
            titleRu = titleRu,
            posterUrl = poster,
            description = description,
            rating = rating,
            episodes = episodes,
            type = type,
            status = status,
            genres = genres,
            studios = studios,
            year = year
        )
    }

    private fun AnimeEntity.toDomain(userData: UserAnimeEntity? = null): Anime {
        return Anime(
            id = id,
            title = titleRu ?: title,
            posterUrl = posterUrl,
            description = description,
            rating = rating,
            episodes = episodes,
            userStatus = userData?.userStatus,
            userRating = userData?.userRating,
            userComment = userData?.userComment,
            isFavorite = userData?.isFavorite ?: false
        )
    }

    private fun AnimeDto.toDomain(): Anime {
        return Anime(
            id = id,
            title = titleRu ?: title,
            posterUrl = poster,
            description = description,
            rating = rating,
            episodes = episodes,
            userStatus = null,
            userRating = null,
            userComment = null,
            isFavorite = false
        )
    }

    // === ИНИЦИАЛИЗАЦИЯ КЭША ===
    override suspend fun initializeCache() {
        try {
            if (animeDao.getCount() == 0) {
                println("Инициализация кэша...")
                remoteDataSource.getAnime(limit = 50).fold(
                    onSuccess = { dtoList ->
                        val entities = dtoList.map { it.toEntity() }
                        animeDao.insertAllAnime(entities)
                        println("Кэш инициализирован: ${entities.size} аниме")
                    },
                    onFailure = { error ->
                        println("Ошибка инициализации кэша: ${error.message}")
                    }
                )
            }
        } catch (e: Exception) {
            println("Исключение при инициализации кэша: ${e.message}")
        }
    }

    override suspend fun clearCache() {
        animeDao.clearAll()
    }

    override suspend fun getCachedAnimeCount(): Int {
        return animeDao.getCount()
    }

    // === ОСНОВНЫЕ МЕТОДЫ ===
    override suspend fun getAllAnime(): List<Anime> {
        return remoteDataSource.getAnime().fold(
            onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
            onFailure = {
                println("API error getAllAnime: ${it.message}")
                emptyList<Anime>()
            }
        )
    }

    override suspend fun getAnime(limit: Int, offset: Int): List<Anime> {
        return remoteDataSource.getAnime(limit, offset).fold(
            onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
            onFailure = { emptyList() }
        )
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        return remoteDataSource.getAnimeById(id).fold(
            onSuccess = { dto -> dto.toDomain() },
            onFailure = { null }
        )
    }

    // === ПОИСК ===
    override suspend fun searchAnime(query: String): List<Anime> {
        return searchAnime(query, limit = 20, offset = 0)
    }

    override suspend fun searchAnime(query: String, limit: Int, offset: Int): List<Anime> {
        val local = animeDao.searchAnime(query).firstOrNull()

        return if (local.isNullOrEmpty()) {
            remoteDataSource.searchAnime(query, limit, offset).fold(
                onSuccess = { dtoList ->
                    val entities = dtoList.map { it.toEntity() }
                    animeDao.insertAllAnime(entities)
                    dtoList.map { it.toDomain() }
                },
                onFailure = { emptyList() }
            )
        } else {
            local.map { it.toDomain() }
        }
    }

    // === FLOW МЕТОДЫ ===
    override fun getAllAnimeFlow(): Flow<List<Anime>> {
        return combine(
            animeDao.getAllAnime(),
            userAnimeDao.getAllUserAnime()
        ) { animeEntities, userEntities ->
            val userMap = userEntities.associateBy { it.animeId }
            animeEntities.map { entity ->
                entity.toDomain(userMap[entity.id])
            }
        }
    }

    override fun searchAnimeFlow(query: String): Flow<List<Anime>> = flow {
        emit(searchAnime(query))
    }

    // === ПОЛЬЗОВАТЕЛЬСКИЕ ДАННЫЕ ===
    override suspend fun getAnimeByStatus(status: AnimeStatus): List<Anime> {
        val userAnimeList = userAnimeDao.getUserAnimeByStatus(status).firstOrNull() ?: emptyList()
        val animeIds = userAnimeList.map { it.animeId }

        return if (animeIds.isNotEmpty()) {
            // TODO: Нужен метод для получения нескольких аниме по IDs
            emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun toggleFavorite(animeId: Int): Boolean {
        val current = userAnimeDao.getUserAnime(animeId)
        val newFavorite = !(current?.isFavorite ?: false)

        val userAnime = current ?: UserAnimeEntity(
            animeId = animeId,
            userStatus = null,
            userRating = null,
            userComment = null,
            isFavorite = newFavorite
        )

        userAnimeDao.upsertUserAnime(userAnime.copy(isFavorite = newFavorite))
        return newFavorite
    }

    override suspend fun setFavorite(animeId: Int, isFavorite: Boolean) {
        val current = userAnimeDao.getUserAnime(animeId)
        val userAnime = current ?: UserAnimeEntity(
            animeId = animeId,
            userStatus = null,
            userRating = null,
            userComment = null,
            isFavorite = isFavorite
        )
        userAnimeDao.upsertUserAnime(userAnime.copy(isFavorite = isFavorite))
    }

    override suspend fun updateStatus(animeId: Int, status: AnimeStatus?) {
        val current = userAnimeDao.getUserAnime(animeId)

        if (current == null && status != null) {
            userAnimeDao.upsertUserAnime(
                UserAnimeEntity(
                    animeId = animeId,
                    userStatus = status,
                    userRating = null,
                    userComment = null
                )
            )
        } else if (current != null) {
            userAnimeDao.updateStatus(animeId, status)
        }
    }

    override suspend fun updateRating(animeId: Int, rating: Int?, comment: String?) {
        val current = userAnimeDao.getUserAnime(animeId)

        if (current == null && (rating != null || comment != null)) {
            userAnimeDao.upsertUserAnime(
                UserAnimeEntity(
                    animeId = animeId,
                    userStatus = null,
                    userRating = rating,
                    userComment = comment
                )
            )
        } else if (current != null) {
            userAnimeDao.updateRating(animeId, rating, comment)
        }
    }

    // === СИНХРОНИЗАЦИЯ ===
    override suspend fun syncWithApi() {
        println("Синхронизация с API (заглушка)")
        // TODO: Реализовать синхронизацию пользовательских данных с API
    }

    override suspend fun syncFavorites() {
        println("Синхронизация избранного (заглушка)")
        // TODO: Реализовать синхронизацию избранного с API
    }
}
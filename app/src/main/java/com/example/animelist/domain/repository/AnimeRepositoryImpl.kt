// data/repository/AnimeRepositoryImpl.kt
package com.example.animelist.domain.repository

import com.example.animelist.data.local.dao.AnimeDao
import com.example.animelist.data.local.dao.UserAnimeDao
import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.data.remote.YummyAnimeDataSource
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime
import com.example.animelist.domain.model.AnimeStatus
import com.example.animelist.data.mapper.AnimeMapper.toEntity
import com.example.animelist.data.mapper.AnimeMapper.toDomain
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

    // === ИНИЦИАЛИЗАЦИЯ КЭША ===
    override suspend fun initializeCache() {
        println("=== INITIALIZE CACHE ===")

        try {
            val count = animeDao.getCount()
            println("in DB: $count info")

            if (count == 0) {
                println("DB is empty, load from API...")

                remoteDataSource.getAnime(limit = 10) // Начните с 10 для теста
                    .fold(
                        onSuccess = { dtoList ->
                            println("Get from API: ${dtoList.size} anime")

                            val entities = dtoList.map {
                                println("  Convert: ${it.title}")
                                it.toEntity()
                            }

                            animeDao.insertAllAnime(entities)
                            println("Saved in ROOM: ${entities.size} anime")
                        },
                        onFailure = { error ->
                            println("API ERROR: ${error.message}")
                        }
                    )
            } else {
                println("We have a cache, loading skiped")
            }
        } catch (e: Exception) {
            println("EXCEPTION в initializeCache: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun clearCache() {
        animeDao.clearAll()
    }

    override suspend fun getCachedAnimeCount(): Int {
        return animeDao.getCount()
    }

    // === ОСНОВНЫЕ МЕТОДЫ ===
//    override suspend fun getAllAnime(): List<Anime> {
//        return remoteDataSource.getAnime().fold(
//            onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
//            onFailure = {
//                println("API error getAllAnime: ${it.message}")
//                emptyList<Anime>()
//            }
//        )
//    }

    // В AnimeRepositoryImpl.kt
    override suspend fun getAllAnime(): List<Anime> {
        // Сначала пробуем кэш
        val cached = animeDao.getAllAnime().firstOrNull()
        if (!cached.isNullOrEmpty()) {
            val userDataMap = userAnimeDao.getAllUserAnime()
                .firstOrNull()
                ?.associateBy { it.animeId } ?: emptyMap()
            return cached.map { it.toDomain(userDataMap[it.id]) }
        }

        // Если кэш пуст — загружаем из API
        return remoteDataSource.getAnime().fold(
            onSuccess = { dtoList ->
                val entities = dtoList.map { it.toEntity() }
                animeDao.insertAllAnime(entities)
                dtoList.map { it.toDomain() }
            },
            onFailure = { emptyList() }
        )
    }

    override suspend fun getAnime(limit: Int, offset: Int): List<Anime> {
        return remoteDataSource.getAnime(limit, offset).fold(
            onSuccess = { dtoList -> dtoList.map { it.toDomain() } },
            onFailure = { emptyList() }
        )
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        println("getAnimeById: $id")

        // Сначала проверяем локальный кэш
        val cachedAnime = animeDao.getAnimeById(id)
        val userData = userAnimeDao.getUserAnime(id)

        if (cachedAnime != null) {
            println("Found in cache: ${cachedAnime.title}")
            return cachedAnime.toDomain(userData)
        }

        // Если нет в кэше — загружаем из API
        println("Not in cache, loading from API...")
        return remoteDataSource.getAnimeById(id).fold(
            onSuccess = { dto ->
                println("Loaded from API: ${dto.title}")
                // Сохраняем в кэш
                val entity = dto.toEntity()
                animeDao.insertAnime(entity)
                dto.toDomain()
            },
            onFailure = { error ->
                println("API error: ${error.message}")
                null
            }
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
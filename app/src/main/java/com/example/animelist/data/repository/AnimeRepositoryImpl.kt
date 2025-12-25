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
        println("=== USE TEST ANIME ===")

        return listOf(
            Anime(
                id = 1,
                title = "Атака титанов",
                posterUrl = "https://example.com/titan.jpg",
                description = "Эрен Йегер сражается с титанами",
                rating = 9.1,
                episodes = 75,
                userStatus = AnimeStatus.COMPLETED,
                userRating = 5,
                userComment = "Шедевр!",
                isFavorite = true
            ),
            Anime(
                id = 2,
                title = "Наруто",
                posterUrl = "https://example.com/naruto.jpg",
                description = "Мальчик-ниндзя мечтает стать хокаге",
                rating = 8.3,
                episodes = 720,
                userStatus = AnimeStatus.WATCHING,
                userRating = 4,
                userComment = "Длинно, но интересно",
                isFavorite = false
            ),
            Anime(
                id = 3,
                title = "Стальной алхимик",
                posterUrl = "https://example.com/fma.jpg",
                description = "Братья ищут философский камень",
                rating = 9.0,
                episodes = 64,
                userStatus = AnimeStatus.PLANNED,
                userRating = null,
                userComment = null,
                isFavorite = true
            ),
            Anime(
                id = 4,
                title = "Твоё имя",
                posterUrl = "https://example.com/kiminonawa.jpg",
                description = "Парень и девушка меняются телами",
                rating = 8.9,
                episodes = 1,
                userStatus = AnimeStatus.COMPLETED,
                userRating = 5,
                userComment = "Плакала",
                isFavorite = true
            ),
            Anime(
                id = 5,
                title = "Чёрный клевер",
                posterUrl = "https://example.com/blackclover.jpg",
                description = "Мальчик без магии хочет стать магическим императором",
                rating = 7.5,
                episodes = 170,
                userStatus = AnimeStatus.DROPPED,
                userRating = 3,
                userComment = "Надоел главный герой",
                isFavorite = false
            )
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
// data/local/dao/AnimeDao.kt
package com.example.animelist.data.local.dao

import androidx.room.*
import com.example.animelist.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime_cache ORDER BY title")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime_cache WHERE id = :id")
    suspend fun getAnimeById(id: Int): AnimeEntity?

    @Query("SELECT * FROM anime_cache WHERE title LIKE '%' || :query || '%' OR titleRu LIKE '%' || :query || '%'")
    fun searchAnime(query: String): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)

    @Query("DELETE FROM anime_cache WHERE cachedAt < :expiryDate")
    suspend fun deleteExpiredCache(expiryDate: Date)

    @Query("DELETE FROM anime_cache")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM anime_cache")
    suspend fun getCount(): Int
}
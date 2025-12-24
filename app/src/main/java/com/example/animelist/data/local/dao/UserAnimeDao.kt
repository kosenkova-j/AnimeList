package com.example.animelist.data.local.dao

import androidx.room.*
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.domain.model.AnimeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnimeDao {

    @Query("SELECT * FROM user_anime WHERE animeId = :animeId")
    suspend fun getUserAnime(animeId: Int): UserAnimeEntity?

    @Query("SELECT * FROM user_anime")
    fun getAllUserAnime(): Flow<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_anime WHERE userStatus = :status")
    fun getUserAnimeByStatus(status: AnimeStatus): Flow<List<UserAnimeEntity>>

    @Query("SELECT * FROM user_anime WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<UserAnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserAnime(userAnime: UserAnimeEntity)

    @Update
    suspend fun updateUserAnime(userAnime: UserAnimeEntity)

    @Query("UPDATE user_anime SET userStatus = :status WHERE animeId = :animeId")
    suspend fun updateStatus(animeId: Int, status: AnimeStatus?)

    @Query("UPDATE user_anime SET userRating = :rating, userComment = :comment WHERE animeId = :animeId")
    suspend fun updateRating(animeId: Int, rating: Int?, comment: String?)

    @Query("UPDATE user_anime SET isFavorite = :isFavorite WHERE animeId = :animeId")
    suspend fun updateFavorite(animeId: Int, isFavorite: Boolean)

    @Query("DELETE FROM user_anime WHERE animeId = :animeId")
    suspend fun deleteUserAnime(animeId: Int)
}
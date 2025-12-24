package com.example.animelist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animelist.domain.model.AnimeStatus
import java.util.Date

@Entity(tableName = "user_anime")
data class UserAnimeEntity(
    @PrimaryKey val animeId: Int,
    val userStatus: AnimeStatus?,
    val userRating: Int?,        // 1-10 звёзд
    val userComment: String?,
    val isFavorite: Boolean = false,
    val lastUpdated: Date = Date()
)
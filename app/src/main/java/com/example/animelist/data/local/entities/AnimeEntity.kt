// data/local/entities/AnimeEntity.kt
package com.example.animelist.data.local.entities

//import androidx.databinding.adapters.Converters
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.animelist.data.local.database.RoomConverters
import java.util.Date

@Entity(tableName = "anime_cache")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val titleRu: String?,
    val posterUrl: String?,
    val description: String?,
    val season: Int?,
    val rating: Double?,
    val episodes: Int?,
    val type: String?,
    val status: String?,
    val genres: List<String>?,
    val studios: List<String>?,
    val year: Int?,
    val cachedAt: Date = Date()  // Когда закэшировано
)
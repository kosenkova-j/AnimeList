// data/local/database/RoomConverters.kt
package com.example.animelist.data.local.database

import androidx.room.TypeConverter
import com.example.animelist.domain.model.AnimeStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class RoomConverters {

    // AnimeStatus enum
    @TypeConverter
    fun fromStatus(status: AnimeStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toStatus(name: String?): AnimeStatus? {
        return name?.let { AnimeStatus.valueOf(it) }
    }

    // List<String> для жанров/студий
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        return json?.let {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(it, type)
        }
    }

    // Date
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }
}
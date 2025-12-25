package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "title_ru") val titleRu: String?,
    @Json(name = "poster") val poster: String?,
    @Json(name = "rating") val rating: Double?,
    @Json(name = "description") val description: String?,
    @Json(name = "episodes") val episodes: Int?,
    @Json(name = "type") val type: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "genres") val genres: List<String>?,
    @Json(name = "studios") val studios: List<String>?,
    @Json(name = "year") val year: Int?
)
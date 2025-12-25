package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeDto(
    @Json(name = "anime_id") val animeId: Int,              // ? anime_id, не id!
    @Json(name = "title") val title: String,                 // ? Строка, не объект!
    @Json(name = "anime_url") val animeUrl: String?,
    @Json(name = "poster") val poster: PosterDto?,
    @Json(name = "description") val description: String?,
    @Json(name = "rating") val rating: RatingDto?,
    @Json(name = "genres") val genres: List<GenreDto>?,
    @Json(name = "year") val year: Int?,
    @Json(name = "type") val type: TypeDto?,
    @Json(name = "anime_status") val animeStatus: AnimeStatusDto?,
    @Json(name = "episodes") val episodes: Int? = null,      // Может отсутствовать
    @Json(name = "season") val season: Int? = null,
    @Json(name = "views") val views: Int? = null
)
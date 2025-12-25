// data/remote/dto/AnimeListResponse.kt
package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// УДАЛИТЕ внешний класс, оставьте ТОЛЬКО:
@JsonClass(generateAdapter = true)
data class AnimeListResponse(
    @Json(name = "data") val data: List<AnimeDto>,
    @Json(name = "total") val total: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "offset") val offset: Int
)

@JsonClass(generateAdapter = true)
data class AnimeDetailResponse(
    @Json(name = "data") val data: AnimeDto
)
// data/remote/dto/AnimeListResponse.kt
package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeListResponse(
    @Json(name = "response") val response: List<AnimeDto>? = null
)

@JsonClass(generateAdapter = true)
data class AnimeDetailResponse(
    @Json(name = "response") val response: AnimeDto? = null
)
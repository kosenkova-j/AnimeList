package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PosterDto(
    @Json(name = "fullsize") val fullsize: String?,
    @Json(name = "big") val big: String?,
    @Json(name = "small") val small: String?,
    @Json(name = "medium") val medium: String?,
    @Json(name = "huge") val huge: String?,
    @Json(name = "mega") val mega: String?
) {
    // Получаем лучший доступный URL
    fun getBestUrl(): String? = fullsize ?: big ?: huge ?: medium ?: small
}
package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeStatusDto(
    @Json(name = "value") val value: Int?,
    @Json(name = "title") val title: String?,
    @Json(name = "alias") val alias: String?
)
package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingDto(
    @Json(name = "average") val average: Double?,
    @Json(name = "counters") val counters: Int?,
    @Json(name = "kp_rating") val kpRating: Double?,
    @Json(name = "myanimelist_rating") val malRating: Double?,
    @Json(name = "shikimori_rating") val shikimoriRating: Double?
)

@JsonClass(generateAdapter = true)
data class RateAnimeRequest(
    @Json(name = "rating") val rating: Int
)

@JsonClass(generateAdapter = true)
data class UpdateListRequest(
    @Json(name = "status") val status: String
)

@JsonClass(generateAdapter = true)
data class ReviewRequest(
    @Json(name = "text") val text: String,
    @Json(name = "rating") val rating: Int?
)
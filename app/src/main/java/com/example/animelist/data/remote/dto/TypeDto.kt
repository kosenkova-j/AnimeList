package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TypeDto(
    @Json(name = "value") val value: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortname") val shortname: String?
)
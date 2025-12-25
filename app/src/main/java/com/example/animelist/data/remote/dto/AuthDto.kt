package com.example.animelist.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
//data class LoginRequest(
//    @Json(name = "email") val email: String,
//    @Json(name = "password") val password: String
//)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token") val token: String,
    @Json(name = "expires_in") val expiresIn: Long
)

@JsonClass(generateAdapter = true)
data class TokenRefreshResponse(
    @Json(name = "token") val token: String
)
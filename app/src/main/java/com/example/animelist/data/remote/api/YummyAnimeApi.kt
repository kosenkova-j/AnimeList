package com.example.animelist.data.remote.api

import com.example.animelist.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface YummyAnimeApi {

    // === АУТЕНТИФИКАЦИЯ ===
//    @POST("profile/login")
//    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("profile/token")
    suspend fun refreshToken(): Response<TokenRefreshResponse>

    // === АНИМЕ (основные методы) ===
    @GET("anime")
    suspend fun getAnime(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("type") type: String? = null,
        @Query("status") status: String? = null,
        @Query("genre") genre: String? = null,
        @Query("year") year: Int? = null
    ): Response<AnimeListResponse>

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): Response<AnimeDetailResponse>

    @GET("search")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<AnimeListResponse>

    // === СПИСКИ ПОЛЬЗОВАТЕЛЯ ===
    @PUT("anime/{id}/list")
    suspend fun updateAnimeStatus(
        @Path("id") animeId: Int,
        @Body request: UpdateListRequest
    ): Response<Unit>

    @GET("anime/{id}/list")
    suspend fun getUserAnimeStatus(
        @Path("id") animeId: Int
    ): Response<UpdateListRequest>

    // === РЕЙТИНГ ===
    @PUT("anime/{id}/rate")
    suspend fun rateAnime(
        @Path("id") animeId: Int,
        @Body request: RateAnimeRequest
    ): Response<Unit>

    @GET("anime/{id}/rate")
    suspend fun getUserRating(
        @Path("id") animeId: Int
    ): Response<RateAnimeRequest>

    // === ИЗБРАННОЕ ===
    @PUT("anime/{id}/list/fav")
    suspend fun addToFavorites(
        @Path("id") animeId: Int
    ): Response<Unit>

    @DELETE("anime/{id}/list/fav")
    suspend fun removeFromFavorites(
        @Path("id") animeId: Int
    ): Response<Unit>

    // === ОТЗЫВЫ ===
    @POST("reviews")
    suspend fun createReview(
        @Body request: ReviewRequest
    ): Response<Unit>
}
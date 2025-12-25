package com.example.animelist.domain.model

// класс аниме
data class Anime(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val description: String?,
    val rating: Double?,
    val season: Int?,
    val episodes: Int?,
    val userStatus: AnimeStatus?,
    val userRating: Int?,
    val userComment: String?,
    val isFavorite: Boolean = false
)
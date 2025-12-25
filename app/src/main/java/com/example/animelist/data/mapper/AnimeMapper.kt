package com.example.animelist.data.mapper

import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime

// data/mapper/AnimeMapper.kt
object AnimeMapper {

    fun AnimeDto.toEntity() = AnimeEntity(
        id = id,
        title = title,
        titleRu = titleRu,
        posterUrl = poster,
        description = description,
        rating = rating,
        episodes = episodes,
        type = type,
        status = status,
        genres = genres,
        studios = studios,
        year = year
    )

    fun AnimeEntity.toDomain(userData: UserAnimeEntity? = null) = Anime(
        id = id,
        title = titleRu ?: title,
        posterUrl = posterUrl,
        description = description,
        rating = rating,
        episodes = episodes,
        userStatus = userData?.userStatus,
        userRating = userData?.userRating,
        userComment = userData?.userComment,
        isFavorite = userData?.isFavorite ?: false
    )

    fun AnimeDto.toDomain() = Anime(
        id = id,
        title = titleRu ?: title,
        posterUrl = poster,
        description = description,
        rating = rating,
        episodes = episodes,
        userStatus = null,
        userRating = null,
        userComment = null,
        isFavorite = false
    )
}

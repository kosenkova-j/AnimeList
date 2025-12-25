// data/mapper/AnimeMapper.kt
package com.example.animelist.data.mapper

import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime

object AnimeMapper {

    fun AnimeDto.toEntity() = AnimeEntity(
        id = animeId,
        title = title,
        titleRu = title,
        posterUrl = poster?.getBestUrl()?.let {
            if (it.startsWith("//")) "https:$it" else it
        },
        description = description,
        rating = rating?.average,
        season = season,
        episodes = episodes,
        type = type?.name,
        status = animeStatus?.title,
        genres = genres?.mapNotNull { it.title },
        studios = null,
        year = year
    )

    fun AnimeEntity.toDomain(userData: UserAnimeEntity? = null) = Anime(
        id = id,
        title = titleRu ?: title,
        posterUrl = posterUrl,
        description = description,
        rating = rating,
        season = season,
        episodes = episodes,
        userStatus = userData?.userStatus,
        userRating = userData?.userRating,
        userComment = userData?.userComment,
        isFavorite = userData?.isFavorite ?: false
    )

    fun AnimeDto.toDomain() = Anime(
        id = animeId,
        title = title,
        posterUrl = poster?.getBestUrl()?.let {
            if (it.startsWith("//")) "https:$it" else it
        },
        description = description,
        rating = rating?.average,
        season = season,
        episodes = episodes,
        userStatus = null,
        userRating = null,
        userComment = null,
        isFavorite = false
    )
}
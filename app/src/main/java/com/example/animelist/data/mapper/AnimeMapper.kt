// data/mapper/AnimeMapper.kt
package com.example.animelist.data.mapper

import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.local.entities.UserAnimeEntity
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime

object AnimeMapper {

    fun AnimeDto.toEntity() = AnimeEntity(
        id = animeId,                                    // ? animeId
        title = title,                                   // ? Просто строка
        titleRu = title,                                 // ? Одно и то же (API уже на русском)
        posterUrl = poster?.getBestUrl()?.let {
            if (it.startsWith("//")) "https:$it" else it // ? Добавляем https:
        },
        description = description,
        rating = rating?.average,
        episodes = episodes,
        type = type?.name,
        status = animeStatus?.title,
        genres = genres?.mapNotNull { it.title },
        studios = null,                                  // API не возвращает в списке
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
        id = animeId,
        title = title,
        posterUrl = poster?.getBestUrl()?.let {
            if (it.startsWith("//")) "https:$it" else it
        },
        description = description,
        rating = rating?.average,
        episodes = episodes,
        userStatus = null,
        userRating = null,
        userComment = null,
        isFavorite = false
    )
}
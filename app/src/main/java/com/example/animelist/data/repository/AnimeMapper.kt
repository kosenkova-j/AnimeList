package com.example.animelist.data.repository

import com.example.animelist.data.local.entities.AnimeEntity
import com.example.animelist.data.remote.dto.AnimeDto
import com.example.animelist.domain.model.Anime

class AnimeMapper {
    fun AnimeDto.toEntity(): AnimeEntity {
        return AnimeEntity(
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
    }

    fun AnimeEntity.toDomain(): Anime {
        return Anime(
            id = id,
            title = titleRu ?: title,
            posterUrl = posterUrl,
            description = description,
            rating = rating,
            episodes = episodes,
            userStatus = null, // Будет из UserAnimeDao
            userRating = null,
            userComment = null,
            isFavorite = false
        )
    }
}
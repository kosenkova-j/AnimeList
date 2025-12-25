package com.example.animelist.domain.model

// domain/model/AnimeFilter.kt
enum class AnimeFilter {
    ALL,         // Все
    WATCHING,    // Смотрю
    COMPLETED,   // Просмотрено
    PLANNED,     // В планах
    DROPPED,     // Брошено
    FAVORITES    // Избранное (по флагу isFavorite)
}
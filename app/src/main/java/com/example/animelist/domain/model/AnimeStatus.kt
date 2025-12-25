package com.example.animelist.domain.model

// domain/model/AnimeStatus.kt
enum class AnimeStatus {
    WATCHING,   // Смотрю
    COMPLETED,  // Просмотрено
    PLANNED,    // В планах
    DROPPED,    // Брошено
    FAVORITE
}

// isFavorite остаётся отдельным boolean полем — это правильно!
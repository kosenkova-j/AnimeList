package com.example.animelist.presentation.di

import com.example.animelist.data.repository.AnimeRepository
import com.example.animelist.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAnimeUseCase(
        animeRepository: AnimeRepository
    ): GetAnimeUseCase {
        return GetAnimeUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideSearchAnimeUseCase(
        animeRepository: AnimeRepository
    ): SearchAnimeUseCase {
        return SearchAnimeUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        animeRepository: AnimeRepository
    ): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateAnimeStatusUseCase(
        animeRepository: AnimeRepository
    ): UpdateAnimeStatusUseCase {
        return UpdateAnimeStatusUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideRateAnimeUseCase(
        animeRepository: AnimeRepository
    ): RateAnimeUseCase {
        return RateAnimeUseCase(animeRepository)
    }

    // di/UseCaseModule.kt
    @Provides
    @Singleton
    fun provideInitializeCacheUseCase(
        animeRepository: AnimeRepository
    ): InitializeCacheUseCase {
        return InitializeCacheUseCase(animeRepository)
    }
}
// di/UseCaseModule.kt
package com.example.animelist.presentation.di

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
        animeRepository: com.example.animelist.data.repository.AnimeRepository
    ): GetAnimeUseCase {
        return GetAnimeUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideSearchAnimeUseCase(
        animeRepository: com.example.animelist.data.repository.AnimeRepository
    ): SearchAnimeUseCase {
        return SearchAnimeUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        animeRepository: com.example.animelist.data.repository.AnimeRepository
    ): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateAnimeStatusUseCase(
        animeRepository: com.example.animelist.data.repository.AnimeRepository
    ): UpdateAnimeStatusUseCase {
        return UpdateAnimeStatusUseCase(animeRepository)
    }

    @Provides
    @Singleton
    fun provideRateAnimeUseCase(
        animeRepository: com.example.animelist.data.repository.AnimeRepository
    ): RateAnimeUseCase {
        return RateAnimeUseCase(animeRepository)
    }
}
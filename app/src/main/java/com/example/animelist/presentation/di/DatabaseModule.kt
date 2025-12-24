// di/DatabaseModule.kt
package com.example.animelist.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.animelist.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AppDatabase) = database.animeDao()

    @Provides
    @Singleton
    fun provideUserAnimeDao(database: AppDatabase) = database.userAnimeDao()
}
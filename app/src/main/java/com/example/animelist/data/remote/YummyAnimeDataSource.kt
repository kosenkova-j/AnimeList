// data/remote/YummyAnimeDataSource.kt
package com.example.animelist.data.remote

import com.example.animelist.data.remote.api.YummyAnimeApi
import com.example.animelist.data.remote.dto.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YummyAnimeDataSource @Inject constructor(
    private val api: YummyAnimeApi
) {

    suspend fun getAnime(limit: Int = 20, offset: Int = 0): Result<List<AnimeDto>> {
        return try {
            val response = api.getAnime(limit, offset)
            if (response.isSuccessful) {
                val body = response.body()
                val animeList = body?.response ?: emptyList()
                println("API Success: получено ${animeList.size} аниме")
                animeList.take(3).forEach {
                    println("  - ${it.title} (id=${it.animeId})")
                }
                Result.success(animeList)
            } else {
                println("API Error: ${response.code()}")
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            println("API Exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun searchAnime(query: String, limit: Int = 20, offset: Int = 0): Result<List<AnimeDto>> {
        return try {
            val response = api.searchAnime(query, limit, offset)
            if (response.isSuccessful) {
                Result.success(response.body()?.response ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnimeById(id: Int): Result<AnimeDto> {
        return try {
            val response = api.getAnimeById(id)
            val anime = response.body()?.response
            if (response.isSuccessful && anime != null) {
                Result.success(anime)
            } else {
                Result.failure(Exception("Anime not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
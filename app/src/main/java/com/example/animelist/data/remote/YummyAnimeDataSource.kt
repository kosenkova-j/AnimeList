// data/remote/YummyAnimeDataSource.kt
package com.example.animelist.data.remote

import com.example.animelist.data.remote.api.YummyAnimeApi
import com.example.animelist.data.remote.dto.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YummyAnimeDataSource @Inject constructor(
    private val api: YummyAnimeApi,
    private val tokenManager: TokenManager
) {

    suspend fun getAnimeWithPagination(limit: Int = 20, offset: Int = 0): Result<AnimeListResponse> {
        return try {
            val response = api.getAnime(limit, offset)
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Для обычных случаев - просто список
    suspend fun getAnime(limit: Int = 20, offset: Int = 0): Result<List<AnimeDto>> {
        return getAnimeWithPagination(limit, offset).map { it.data }
    }

    suspend fun searchAnime(query: String, limit: Int = 20, offset: Int = 0): Result<List<AnimeDto>> {
        return try {
            val response = api.searchAnime(query, limit, offset)
            handleResponse(response).map { it.data } // ← Извлекаем data
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnimeById(id: Int): Result<AnimeDto> {
        return try {
            val response = api.getAnimeById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception("Failed to load anime"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun <T> handleResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Empty response body"))
            }
        } else {
            Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
        }
    }

}
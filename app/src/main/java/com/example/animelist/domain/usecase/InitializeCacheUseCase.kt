// domain/usecase/InitializeCacheUseCase.kt
package com.example.animelist.domain.usecase

import com.example.animelist.data.repository.AnimeRepository
import javax.inject.Inject

class InitializeCacheUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke() {
        repository.initializeCache()
    }
}
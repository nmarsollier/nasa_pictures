package com.example.exercise.models.api.tools

enum class CacheStrategy {
    NETWORK, DB
}

sealed class Result<T> {
    data class Success<T>(
        val cacheStrategy: CacheStrategy, val data: List<T>
    ) : Result<T>()

    data class Error<T>(val e: Exception) : Result<T>()
}

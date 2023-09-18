package com.example.exercise.models.api.tools

sealed class Result<T> {
    data class Success<T>(
        val data: List<T>
    ) : Result<T>()

    data class Error<T>(val e: Exception) : Result<T>()
}

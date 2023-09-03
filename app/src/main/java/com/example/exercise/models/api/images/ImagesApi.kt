package com.example.exercise.models.api.images

import com.example.exercise.models.businessObjects.ImageValue
import retrofit2.http.GET
import retrofit2.http.Path

interface ImagesApi {
    @GET("/api/enhanced/date/{date}")
    suspend fun listImages(@Path("date") date: String): List<ImageValue>
}

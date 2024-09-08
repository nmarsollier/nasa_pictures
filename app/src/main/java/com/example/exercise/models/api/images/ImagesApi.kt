package com.example.exercise.models.api.images

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface ImagesApiInterface {
    @GET("/api/enhanced/date/{date}")
    suspend fun fetchImages(@Path("date") date: String): List<ImageValue>
}

class ImagesApi(
    retrofitClient: Retrofit
) {
    private val service = retrofitClient.create(ImagesApiInterface::class.java)

    suspend fun fetchImages(date: String) = service.fetchImages(date)
}
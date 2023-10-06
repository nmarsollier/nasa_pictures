package com.example.exercise.models.api.images

import com.example.exercise.models.api.tools.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface ImagesApiInterface {
    @GET("/api/enhanced/date/{date}")
    suspend fun fetchImages(@Path("date") date: String): List<ImageValue>
}

class ImagesApi(
    retrofitClient: Retrofit
) {
    private val service = retrofitClient.create(ImagesApiInterface::class.java)

    suspend fun fetchImages(date: String): Result<ImageValue> = suspendCoroutine {
        try {
            MainScope().launch(Dispatchers.IO) {
                it.resume(
                    Result.Success(service.fetchImages(date))
                )
            }
        } catch (e: Exception) {
            it.resume(Result.Error(e))
        }
    }
}
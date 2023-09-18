package com.example.exercise.models.api.images

import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ImageValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImagesApiClient(
    retrofitClient: Retrofit
) {
    private val service: ImagesApi = retrofitClient.create(ImagesApi::class.java)

    suspend fun listImages(date: String): Result<ImageValue> = suspendCoroutine {
        try {
            MainScope().launch(Dispatchers.IO) {
                it.resume(
                    Result.Success(service.listImages(date))
                )
            }
        } catch (e: Exception) {
            it.resume(Result.Error(e))
        }
    }
}
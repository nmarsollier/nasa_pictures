package com.example.exercise.models.api.images

import androidx.annotation.VisibleForTesting
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.api.tools.RetrofitClient
import com.example.exercise.models.businessObjects.ImageValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class ImagesApiClient {
    private var service: ImagesApi =
        RetrofitClient.retrofit(urlBaseOverride ?: "https://epic.gsfc.nasa.gov")
            .create(ImagesApi::class.java)

    fun listImages(date: String): Flow<Result<ImageValue>> = channelFlow {
        try {
            send(
                Result.Success(CacheStrategy.NETWORK, service.listImages(date))
            )
        } catch (e: Exception) {
            send(Result.Error(e))
        }
    }

    companion object {
        @VisibleForTesting
        var urlBaseOverride: String? = null
    }
}
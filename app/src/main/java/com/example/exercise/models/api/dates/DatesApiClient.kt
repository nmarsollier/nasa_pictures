package com.example.exercise.models.api.dates

import androidx.annotation.VisibleForTesting
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.api.tools.RetrofitClient
import com.example.exercise.models.businessObjects.DateValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class DatesApiClient {
    private var service: DatesApi =
        RetrofitClient.retrofit(urlBaseOverride ?: "https://epic.gsfc.nasa.gov/")
            .create(DatesApi::class.java)

    fun listDates(): Flow<Result<DateValue>> = channelFlow {
        try {
            send(
                Result.Success(CacheStrategy.NETWORK, service.listDates())
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
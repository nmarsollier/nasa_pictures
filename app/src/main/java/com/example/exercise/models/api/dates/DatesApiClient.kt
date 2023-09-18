package com.example.exercise.models.api.dates

import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.DateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DatesApiClient(
    retrofitClient: Retrofit
) {
    private val service: DatesApi = retrofitClient.create(DatesApi::class.java)

    suspend fun listDates(): Result<DateValue> = suspendCoroutine {
        try {
            MainScope().launch(Dispatchers.IO) {
                it.resume(
                    Result.Success(service.listDates())
                )
            }
        } catch (e: Exception) {
            it.resume(Result.Error(e))
        }
    }
}
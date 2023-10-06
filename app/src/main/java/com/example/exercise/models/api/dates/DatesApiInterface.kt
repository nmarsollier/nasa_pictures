package com.example.exercise.models.api.dates

import com.example.exercise.models.api.tools.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


interface DatesApiInterface {
    @GET("api/enhanced/all")
    suspend fun listDates(): List<DateValue>
}

class DatesApi(
    retrofitClient: Retrofit
) {
    private val service = retrofitClient.create(DatesApiInterface::class.java)

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

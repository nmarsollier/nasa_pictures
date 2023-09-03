package com.example.exercise.models.api.dates

import com.example.exercise.models.businessObjects.DateValue
import retrofit2.http.GET

interface DatesApi {
    @GET("api/enhanced/all")
    suspend fun listDates(): List<DateValue>
}

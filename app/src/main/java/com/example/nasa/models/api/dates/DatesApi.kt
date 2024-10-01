package com.nmarsollier.nasa.models.api.dates

import retrofit2.Retrofit
import retrofit2.http.GET


interface DatesApiInterface {
    @GET("api/enhanced/all")
    suspend fun listDates(): List<DateValue>
}

class DatesApi(
    retrofitClient: Retrofit
) {
    private val service = retrofitClient.create(DatesApiInterface::class.java)

    suspend fun listDates() = service.listDates()
}

package com.nmarsollier.nasa.models.api.dates

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DatesApi(
    val client: HttpClient,
    val baseUrl: String,
) {
    suspend fun listDates(): List<DateValue> {
        println("${baseUrl}api/enhanced/all")
        return client.get("${baseUrl}api/enhanced/all").body()
    }
}

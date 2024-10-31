package com.nmarsollier.nasa.models.api.images

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ImagesApi(
    val client: HttpClient,
    val baseUrl: String,
) {
    suspend fun fetchImages(date: String): List<ImageValue> {
        return client.get("${baseUrl}api/enhanced/date/${date}").body()
    }
}

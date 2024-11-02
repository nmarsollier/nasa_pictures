package com.nmarsollier.nasa.models.useCases

import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.api.images.ImagesApi
import com.nmarsollier.nasa.models.database.image.ImageEntityDao
import com.nmarsollier.nasa.models.database.image.storeIn

class FetchImagesUseCase(
    private val imagesApi: ImagesApi,
    private val imageDao: ImageEntityDao
) {
    suspend fun fetchImages(queryDate: String): List<ImageValue> {
        fetchImagesFromDatabase(queryDate).takeIf { it.isNotEmpty() }?.let {
            return it
        }

        syncRemoteImages(queryDate).let {
            return it
        }
    }

    private suspend fun fetchImagesFromDatabase(queryDate: String): List<ImageValue> =
        imageDao.findByDate(queryDate)
            ?.takeIf { it.isNotEmpty() }
            ?.map { image -> image.toImage() }
            ?: emptyList()

    private suspend fun syncRemoteImages(queryDate: String): List<ImageValue> =
        try {
            val data = imagesApi.fetchImages(queryDate).also {
                it.storeIn(imageDao)
            }
            data
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
}
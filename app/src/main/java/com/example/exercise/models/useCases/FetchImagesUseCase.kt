package com.example.exercise.models.useCases

import com.example.exercise.models.api.images.ImagesApiClient
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.models.database.image.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchImagesUseCase(
    private val imagesClient: ImagesApiClient,
    private val imageRepository: ImageRepository,
) {

    suspend fun fetchImages(queryDate: String): Result<ImageValue> = suspendCoroutine { suspend ->
        MainScope().launch(Dispatchers.IO) {
            fetchImagesFromDatabase(queryDate).takeIf { it.isNotEmpty() }?.let {
                suspend.resume(Result.Success(it))
                return@launch
            }

            syncRemoteImages(queryDate).let {
                suspend.resume(Result.Success(it))
                return@launch
            }
        }
    }

    private suspend fun fetchImagesFromDatabase(queryDate: String): List<ImageValue> =
        suspendCoroutine { suspend ->
            MainScope().launch(Dispatchers.IO) {
                imageRepository.findByDate(queryDate)?.takeIf { it.isNotEmpty() }?.let { images ->
                    suspend.resume(images.map { image -> image.toImage() })
                } ?: suspend.resume(emptyList())
            }
        }

    private suspend fun syncRemoteImages(queryDate: String): List<ImageValue> =
        suspendCoroutine { suspend ->
            MainScope().launch(Dispatchers.IO) {
                imagesClient.listImages(queryDate).let {
                    when (val result = it) {
                        is Result.Error -> {
                            suspend.resume(emptyList())
                        }

                        is Result.Success -> {
                            updateLocalDatabase(result)
                            suspend.resume(result.data)
                        }
                    }
                }
            }
        }

    private suspend fun updateLocalDatabase(result: Result.Success<ImageValue>) {
        result.data.forEach {
            imageRepository.insert(it.toImageEntity())
        }
    }
}
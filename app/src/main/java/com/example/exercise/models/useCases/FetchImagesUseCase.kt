package com.example.exercise.models.useCases

import com.example.exercise.models.api.images.ImagesApiClient
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.models.database.image.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

object FetchImagesUseCase {
    private val imagesClient = ImagesApiClient()

    fun fetchImages(queryDate: String): Flow<Result<ImageValue>> = channelFlow {
        var sendError = true
        ImageRepository.findByDate(queryDate).take(1).collect {
            it?.takeIf { it.isNotEmpty() }?.let {
                sendError = false
                send(Result.Success(CacheStrategy.DB, it.map { it.toImage() }))
                System.out.println("TEST Repository result ${it}")
            }
        }

        imagesClient.listImages(queryDate).take(1).collect {
            when (val result = it) {
                is Result.Error -> {
                    if (sendError) {
                        send(result)
                    }
                }

                is Result.Success -> {
                    updateLocalDatabase(result)
                    System.out.println("TEST Netowrk result ${it}")
                    send(result)
                }
            }
            close()
        }
        awaitClose()
    }

    private fun updateLocalDatabase(
        result: Result.Success<ImageValue>
    ) = MainScope().launch(
        Dispatchers.IO
    ) {
        result.data.forEach {
            ImageRepository.insert(it.toImageEntity())
        }
    }
}
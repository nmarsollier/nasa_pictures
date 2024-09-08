package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

sealed class Destination {
    data class Preview(val image: ImageValue) : Destination()
    data class Animate(val date: ExtendedDateValue) : Destination()
}

sealed class ImagesState {
    data object Loading : ImagesState()

    data object Error : ImagesState()

    data class Redirect(val destination: Destination) : ImagesState()

    data class Ready(
        val images: List<ImageValue>
    ) : ImagesState()
}

interface ImagesReducer {
    fun redirect(destination: Destination)
    fun fetchImages(date: ExtendedDateValue?): Job

    companion object
}

class ImagesViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase
) :
    BaseViewModel<ImagesState>(ImagesState.Loading), ImagesReducer {
    override fun fetchImages(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        ImagesState.Loading.sendToState()

        val queryDate = date?.date ?: run {
            ImagesState.Error.sendToState()
            return@launch
        }

        try {
            ImagesState.Ready(fetchImagesUseCase.fetchImages(queryDate))
        } catch (e: Exception) {
            ImagesState.Error
        }.sendToState()
    }

    override fun redirect(destination: Destination) {
        ImagesState.Redirect(destination).sendToState()
    }
}

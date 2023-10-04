package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.businessObjects.ImageValue
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

        viewModelScope.launch(Dispatchers.IO) {
            fetchImagesUseCase.fetchImages(queryDate).let {
                when (val result = it) {
                    is Result.Error -> ImagesState.Error.sendToState()
                    is Result.Success -> ImagesState.Ready(result.data).sendToState()
                }
            }
        }
    }

    override fun redirect(destination: Destination) {
        ImagesState.Redirect(destination).sendToState()
    }
}

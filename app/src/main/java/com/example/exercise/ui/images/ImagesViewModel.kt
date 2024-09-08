package com.example.exercise.ui.images

import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.example.exercise.ui.common.vm.StateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ImagesEvent {
    data class GoPreview(val image: ImageValue) : ImagesEvent
    data class GoAnimate(val date: ExtendedDateValue) : ImagesEvent
}

sealed interface ImagesState {
    data object Loading : ImagesState

    data object Error : ImagesState

    data class Ready(
        val images: List<ImageValue>
    ) : ImagesState
}

sealed interface ImagesAction {
    data class FetchImages(val date: ExtendedDateValue?) : ImagesAction
    data class GoPreview(val image: ImageValue) : ImagesAction
    data class GoAnimate(val date: ExtendedDateValue) : ImagesAction
}

class ImagesViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase
) :
    StateViewModel<ImagesState, ImagesEvent, ImagesAction>(ImagesState.Loading) {

    override fun reduce(action: ImagesAction) {
        when (action) {
            is ImagesAction.FetchImages -> fetchImages(action.date)
            is ImagesAction.GoPreview -> ImagesEvent.GoPreview(action.image).sendToEvent()
            is ImagesAction.GoAnimate -> ImagesEvent.GoAnimate(action.date).sendToEvent()
        }
    }

    fun fetchImages(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
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
}

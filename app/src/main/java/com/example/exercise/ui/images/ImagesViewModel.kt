package com.example.exercise.ui.images

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.exercise.common.vm.StateViewModel
import com.example.exercise.models.api.dates.refresh
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import com.example.exercise.models.useCases.FetchImagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

sealed interface ImagesEvent {
    @Stable
    data class GoPreview(val image: ImageValue) : ImagesEvent

    @Stable
    data class GoAnimate(val date: ExtendedDateValue) : ImagesEvent
}

sealed interface ImagesState {
    @Stable
    data object Loading : ImagesState

    @Stable
    data object Error : ImagesState

    @Stable
    data class Ready(
        val date: ExtendedDateValue?, val images: List<ImageValue>
    ) : ImagesState
}

sealed interface ImagesAction {
    @Stable
    data class FetchImages(val date: ExtendedDateValue?) : ImagesAction

    @Stable
    data class GoPreview(val image: ImageValue) : ImagesAction

    @Stable
    data class GoAnimate(val date: ExtendedDateValue) : ImagesAction

    @Stable
    data object UpdateDate : ImagesAction
}

class ImagesViewModel(
    private val frescoUtils: FrescoUtils, private val fetchImagesUseCase: FetchImagesUseCase
) : StateViewModel<ImagesState, ImagesEvent, ImagesAction>(ImagesState.Loading) {

    override fun reduce(action: ImagesAction) {
        when (action) {
            is ImagesAction.FetchImages -> fetchImages(action.date)
            is ImagesAction.GoPreview -> ImagesEvent.GoPreview(action.image).sendToEvent()
            is ImagesAction.GoAnimate -> ImagesEvent.GoAnimate(action.date).sendToEvent()
            ImagesAction.UpdateDate -> updateDate()
        }
    }

    private fun fetchImages(date: ExtendedDateValue?) = viewModelScope.launch(Dispatchers.IO) {
        ImagesState.Loading.sendToState()

        val queryDate = date?.date ?: run {
            ImagesState.Error.sendToState()
            return@launch
        }

        try {
            ImagesState.Ready(
                images = fetchImagesUseCase.fetchImages(queryDate),
                date = date.refresh(frescoUtils),
            )
        } catch (e: Exception) {
            ImagesState.Error
        }.sendToState()
    }

    private fun updateDate() = MainScope().launch(Dispatchers.IO) {
        (state.value as? ImagesState.Ready)?.let {
            it.copy(
                date = it.date?.refresh(frescoUtils)
            ).sendToState()
        }
    }
}

package com.example.exercise.ui.imagePreview

import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.ui.common.vm.StateViewModel

sealed interface ImagePreviewState {
    data object Loading : ImagePreviewState

    data class Ready(
        val imageValue: ImageValue,
        val showDetails: Boolean = false
    ) : ImagePreviewState
}

sealed interface ImagePreviewEvent {
}

sealed interface ImagePreviewAction {
    data class Init(val imageValue: ImageValue) : ImagePreviewAction
    data object ToggleDetails : ImagePreviewAction
}

class ImagePreviewViewModel :
    StateViewModel<ImagePreviewState, ImagePreviewEvent, ImagePreviewAction>(ImagePreviewState.Loading) {

    override fun reduce(action: ImagePreviewAction) {
        when (action) {
            is ImagePreviewAction.Init -> init(action.imageValue)
            ImagePreviewAction.ToggleDetails -> toggleDetails()
        }
    }

    fun init(imageValue: ImageValue) {
        ImagePreviewState.Ready(
            imageValue = imageValue,
            showDetails = false
        ).sendToState()
    }

    fun toggleDetails() {
        (state.value as? ImagePreviewState.Ready)?.let { st ->
            st.copy(showDetails = !st.showDetails).sendToState()
        }
    }

}

package com.example.exercise.ui.imagePreview

import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.ui.utils.BaseViewModel

sealed class ImagePreviewState {
    data object Loading : ImagePreviewState()

    data class Ready(
        val imageValue: ImageValue,
        val showDetails: Boolean = false
    ) : ImagePreviewState()
}

interface ImagesPreviewReducer {
    fun init(imageValue: ImageValue)
    fun toggleDetails()

    companion object
}

class ImagePreviewViewModel :
    BaseViewModel<ImagePreviewState>(ImagePreviewState.Loading), ImagesPreviewReducer {
    override fun init(imageValue: ImageValue) {
        ImagePreviewState.Ready(
            imageValue = imageValue,
            showDetails = false
        ).sendToState()
    }

    override fun toggleDetails() {
        (state.value as? ImagePreviewState.Ready)?.let { st ->
            st.copy(showDetails = !st.showDetails).sendToState()
        }
    }
}

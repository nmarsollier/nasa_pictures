package com.example.exercise.ui.imagePreview

import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.flow.update

sealed class ImagePreviewState {
    data object Loading : ImagePreviewState()

    data class Ready(
        val imageValue: ImageValue,
        val showDetails: Boolean = false
    ) : ImagePreviewState()
}

class ImagePreviewViewModel(initial: ImagePreviewState = ImagePreviewState.Loading) :
    BaseViewModel<ImagePreviewState>(initial) {
    fun init(imageValue: ImageValue) {
        mutableState.update {
            ImagePreviewState.Ready(
                imageValue = imageValue,
                showDetails = false
            )
        }
    }

    fun toggleDetails() {
        (state.value as? ImagePreviewState.Ready)?.let { st ->
            mutableState.update {
                st.copy(showDetails = !st.showDetails)
            }
        }
    }
}

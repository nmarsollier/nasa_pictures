package com.nmarsollier.nasa.ui.imagePreview

import androidx.compose.runtime.Stable
import com.nmarsollier.nasa.common.vm.StateViewModel
import com.nmarsollier.nasa.models.api.images.ImageValue

sealed interface ImagePreviewState {
    @Stable
    data object Loading : ImagePreviewState

    @Stable
    data class Ready(
        val imageValue: ImageValue,
        val showDetails: Boolean = false
    ) : ImagePreviewState
}

sealed interface ImagePreviewAction {
    @Stable
    data class Init(val imageValue: ImageValue) : ImagePreviewAction

    @Stable
    data object ToggleDetails : ImagePreviewAction
}

class ImagePreviewViewModel :
    StateViewModel<ImagePreviewState, Unit, ImagePreviewAction>(ImagePreviewState.Loading) {

    override fun reduce(action: ImagePreviewAction) {
        when (action) {
            is ImagePreviewAction.Init -> init(action.imageValue)
            ImagePreviewAction.ToggleDetails -> toggleDetails()
        }
    }

    private fun init(imageValue: ImageValue) {
        ImagePreviewState.Ready(
            imageValue = imageValue,
            showDetails = false
        ).sendToState()
    }

    private fun toggleDetails() {
        (state.value as? ImagePreviewState.Ready)?.let { st ->
            st.copy(showDetails = !st.showDetails).sendToState()
        }
    }
}

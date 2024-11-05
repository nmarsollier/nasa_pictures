package com.nmarsollier.nasa.ui.imageAnimation

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.nmarsollier.nasa.common.ui.CoilUtils
import com.nmarsollier.nasa.common.vm.StateViewModel
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.models.useCases.FetchImagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ImageAnimationState {
    @Stable
    data object Loading : ImageAnimationState

    @Stable
    data object Error : ImageAnimationState

    @Stable
    data class Ready(
        val bitmaps: List<Bitmap>,
    ) : ImageAnimationState
}

sealed interface ImageAnimationAction {
    @Stable
    data class FetchImages(val date: ExtendedDateValue) : ImageAnimationAction
}

class ImageAnimationViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase,
    private val coilUtils: CoilUtils,
) : StateViewModel<ImageAnimationState, Unit, ImageAnimationAction>(
    ImageAnimationState.Loading
) {
    override fun reduce(action: ImageAnimationAction) {
        when (action) {
            is ImageAnimationAction.FetchImages -> fetchImages(action.date)
        }
    }

    private fun fetchImages(dateValue: ExtendedDateValue) = viewModelScope.launch(Dispatchers.IO) {
        try {
            fetchImagesUseCase.fetchImages(dateValue.date)
        } catch (e: Exception) {
            emptyList()
        }.asState().sendToState()
    }

    private suspend fun animation(images: List<ImageValue>): List<Bitmap> {
        return images.mapNotNull { img ->
            getBitmapFromUri(Uri.parse(img.downloadUrl))
        }
    }

    private suspend fun getBitmapFromUri(imageUri: Uri) =
        coilUtils.loadImage(imageUri.toString(), 600)

    private suspend fun List<ImageValue>.asState(): ImageAnimationState {
        return if (this.isEmpty()) {
            ImageAnimationState.Error
        } else {
            ImageAnimationState.Ready(animation(this))
        }
    }
}

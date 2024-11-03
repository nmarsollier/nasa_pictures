package com.nmarsollier.nasa.ui.imageAnimation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.nmarsollier.nasa.common.vm.StateViewModel
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.models.useCases.FetchImagesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface ImageAnimationState {
    @Stable
    data object Loading : ImageAnimationState

    @Stable
    data object Error : ImageAnimationState

    @Stable
    data class Ready(
        val bitmaps: List<ImageBitmap>,
    ) : ImageAnimationState
}

sealed interface ImageAnimationAction {
    @Stable
    data class FetchImages(val date: ExtendedDateValue) : ImageAnimationAction
}

class ImageAnimationViewModel(
    private val context: Context,
    private val fetchImagesUseCase: FetchImagesUseCase,
    private val imageLoader: ImageLoader,
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

    private suspend fun animation(images: List<ImageValue>): List<ImageBitmap> {
        return images.map { img ->
            getBitmapFromUri(Uri.parse(img.downloadUrl))
        }.filter { it != null }.map { it!!.asImageBitmap() }
    }

    private suspend fun getBitmapFromUri(imageUri: Uri) = suspendCoroutine { continuation ->
        CoroutineScope(Dispatchers.IO).launch {
            continuation.resume(
                imageLoader.execute(
                    ImageRequest.Builder(context).data(imageUri).allowHardware(false).size(600, 600)
                        .build()
                ).takeIf { it is SuccessResult }?.image?.toBitmap()
            )
        }
    }

    private suspend fun List<ImageValue>.asState(): ImageAnimationState {
        return if (this.isEmpty()) {
            ImageAnimationState.Error
        } else {
            ImageAnimationState.Ready(animation(this))
        }
    }
}

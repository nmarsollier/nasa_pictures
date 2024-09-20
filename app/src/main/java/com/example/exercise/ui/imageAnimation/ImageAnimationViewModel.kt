package com.example.exercise.ui.imageAnimation

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.core.graphics.scale
import androidx.lifecycle.viewModelScope
import com.example.exercise.common.vm.StateViewModel
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
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
        val animation: AnimationDrawable
    ) : ImageAnimationState
}

sealed interface ImageAnimationAction {
    @Stable
    data class FetchImages(val date: ExtendedDateValue) : ImageAnimationAction
}

class ImageAnimationViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase,
    private val fresco: ImagePipeline
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

    private suspend fun animation(images: List<ImageValue>): AnimationDrawable {
        val animationDrawable = AnimationDrawable()
        animationDrawable.isOneShot = false

        images.forEach { img ->
            getBitmapFromUri(Uri.parse(img.downloadUrl)).let {
                animationDrawable.addFrame(BitmapDrawable(it), 50)
            }
        }

        return animationDrawable
    }

    private suspend fun getBitmapFromUri(imageUri: Uri) = suspendCoroutine { continuation ->
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build()
        fresco.fetchDecodedImage(imageRequest, this).subscribe(
            object : BaseBitmapDataSubscriber() {
                override fun onNewResultImpl(bitmap: Bitmap?) {
                    continuation.resume(bitmap?.scale(400, 400))
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    continuation.resume(null)
                }
            }, CallerThreadExecutor.getInstance()
        )
    }

    private suspend fun List<ImageValue>.asState(): ImageAnimationState {
        return if (this.isEmpty()) {
            ImageAnimationState.Error
        } else {
            ImageAnimationState.Ready(animation(this))
        }
    }
}

package com.example.exercise.ui.animatedPreiew

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

sealed interface AnimatedPreviewState {
    @Stable
    data object Loading : AnimatedPreviewState

    @Stable
    data object Error : AnimatedPreviewState

    @Stable
    data class Ready(
        val animation: AnimationDrawable
    ) : AnimatedPreviewState
}

sealed interface AnimatedPreviewAction {
    @Stable
    data class FetchImages(val date: ExtendedDateValue) : AnimatedPreviewAction
}

class AnimatedPreviewViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase,
    private val fresco: ImagePipeline
) : StateViewModel<AnimatedPreviewState, Unit, AnimatedPreviewAction>(
    AnimatedPreviewState.Loading
) {

    override fun reduce(action: AnimatedPreviewAction) {
        when (action) {
            is AnimatedPreviewAction.FetchImages -> fetchImages(action.date)
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

    private suspend fun List<ImageValue>.asState(): AnimatedPreviewState {
        return if (this.isEmpty()) {
            AnimatedPreviewState.Error
        } else {
            AnimatedPreviewState.Ready(animation(this))
        }
    }
}

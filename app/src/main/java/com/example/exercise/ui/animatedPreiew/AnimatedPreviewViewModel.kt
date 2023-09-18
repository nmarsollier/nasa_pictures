package com.example.exercise.ui.animatedPreiew

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.graphics.scale
import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.models.database.image.FrescoUtils
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class AnimatedPreviewState {
    data object Loading : AnimatedPreviewState()

    data object Error : AnimatedPreviewState()

    data class Ready(
        val animation: AnimationDrawable
    ) : AnimatedPreviewState()
}

interface AnimatedPreviewReducer {
    fun fetchImages(dateValue: ExtendedDateValue): Job
}

class AnimatedPreviewViewModel(
    private val fetchImagesUseCase: FetchImagesUseCase, private val frescoUtils: FrescoUtils
) : BaseViewModel<AnimatedPreviewState>(AnimatedPreviewState.Loading), AnimatedPreviewReducer {

    override fun fetchImages(dateValue: ExtendedDateValue) = viewModelScope.launch(Dispatchers.IO) {
        loadImages(dateValue).let { images ->
            if (images.isEmpty()) {
                mutableState.update {
                    AnimatedPreviewState.Error
                }
            } else {
                mutableState.update {
                    AnimatedPreviewState.Ready(animation(images))
                }
            }
        }
    }

    private suspend fun loadImages(dateValue: ExtendedDateValue) =
        suspendCoroutine<List<ImageValue>> {
            viewModelScope.launch(Dispatchers.IO) {
                fetchImagesUseCase.fetchImages(dateValue.date).let { result ->
                    when (result) {
                        is Result.Error -> {
                            it.resume(emptyList())
                        }

                        is Result.Success -> {
                            it.resume(result.data)
                        }
                    }
                }
            }
        }

    private suspend fun animation(images: List<ImageValue>) = suspendCoroutine { suspend ->
        val animationDrawable = AnimationDrawable()
        animationDrawable.isOneShot = false

        viewModelScope.launch(Dispatchers.IO) {
            images.forEach { img ->
                getBitmapFromUri(Uri.parse(img.downloadUrl)).let {
                    animationDrawable.addFrame(BitmapDrawable(it), 50)
                }
            }
            suspend.resume(animationDrawable)
        }
    }

    private suspend fun getBitmapFromUri(imageUri: Uri) = suspendCoroutine { suspend ->
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build()
        frescoUtils.fresco.fetchDecodedImage(imageRequest, this).subscribe(
            object : BaseBitmapDataSubscriber() {
                override fun onNewResultImpl(bitmap: Bitmap?) {
                    suspend.resume(bitmap?.scale(400, 400))
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    suspend.resume(null)
                }
            }, CallerThreadExecutor.getInstance()
        )
    }
}

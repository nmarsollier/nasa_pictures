package com.example.exercise.ui.animatedPreiew

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.graphics.scale
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.businessObjects.ImageValue
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import com.example.exercise.ui.utils.FrescoUtils
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AnimatedPreviewState {
    data object Loading : AnimatedPreviewState()

    data object Error : AnimatedPreviewState()

    data class Ready(
        val animation: AnimationDrawable
    ) : AnimatedPreviewState()
}

class AnimatedPreviewViewModel(initial: AnimatedPreviewState = AnimatedPreviewState.Loading) :
    BaseViewModel<AnimatedPreviewState>(initial) {

    fun fetchImages(dateValue: ExtendedDateValue) {
        MainScope().launch(Dispatchers.IO) {
            loadImages(dateValue).take(1).collect { images ->
                if (images.isEmpty()) {
                    mutableState.update {
                        AnimatedPreviewState.Error
                    }
                } else {
                    animation(images).take(1).collect { animation ->
                        mutableState.update {
                            AnimatedPreviewState.Ready(animation)
                        }
                    }
                }
            }
        }
    }

    private fun loadImages(dateValue: ExtendedDateValue) = channelFlow<List<ImageValue>> {
        FetchImagesUseCase.fetchImages(dateValue.date).take(1).collect { result ->
            when (result) {
                is Result.Error -> {
                    send(emptyList())
                    close()
                }

                is Result.Success -> {
                    send(result.data)
                    close()
                }
            }
        }
        awaitClose()
    }

    private fun animation(images: List<ImageValue>) = channelFlow {
        val animationDrawable = AnimationDrawable()
        animationDrawable.isOneShot = false

        var collected: Int = 0
        images.forEach { img ->
            getBitmapFromUri(Uri.parse(img.downloadUrl)).take(1).collect {
                collected++
                it?.let {
                    animationDrawable.addFrame(BitmapDrawable(it), 50)
                }
                if (collected == images.size) {
                    send(animationDrawable)
                    close()
                }
            }
        }
        awaitClose()
    }

    private fun getBitmapFromUri(imageUri: Uri) = channelFlow {
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build()
        FrescoUtils.fresco.fetchDecodedImage(imageRequest, this).subscribe(
            object : BaseBitmapDataSubscriber() {
                override fun onNewResultImpl(bitmap: Bitmap?) {
                    MainScope().launch {
                        send(bitmap?.scale(400, 400))
                    }
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    MainScope().launch {
                        send(null)
                    }
                }
            }, CallerThreadExecutor.getInstance()
        )
        awaitClose()
    }
}

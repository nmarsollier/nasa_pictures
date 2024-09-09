package com.example.exercise.ui.images

import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exercise.R
import com.example.exercise.common.ui.CircleProgressBarDrawable
import com.example.exercise.common.ui.KoinPreview
import com.example.exercise.models.api.images.ImageValue
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

@Composable
@ExperimentalFoundationApi
fun ImageItemView(
    image: ImageValue,
    imageReduce: (ImagesAction) -> Unit,
    dateReduce: (ImagesDateAction) -> Unit
) {
    Card(shape = RoundedCornerShape(10.dp),
        backgroundColor = (colorResource(id = R.color.blackBackground)),
        modifier = Modifier
            .size(165.dp)
            .combinedClickable(onClick = {})
            .clickable { imageReduce(ImagesAction.GoPreview(image)) }) {

        AndroidView(modifier = Modifier
            .padding(16.dp)
            .height(165.dp)
            .width(165.dp),
            factory = { context ->
                SimpleDraweeView(context)
            },
            update = { view ->
                loadImage(view, image) {
                    dateReduce(ImagesDateAction.UpdateDate(null))
                }
            })

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = image.identifier,
                fontSize = 10.sp,
                color = colorResource(id = R.color.textWhite)
            )
            Text(
                text = "${stringResource(R.string.captured)} ${image.formattedHourMinute}hs",
                fontSize = 10.sp,
                color = colorResource(id = R.color.textWhite)
            )
        }

    }
}

private fun loadImage(view: SimpleDraweeView, image: ImageValue, onImageLoadCallback: () -> Unit) {
    view.hierarchy.setProgressBarImage(CircleProgressBarDrawable())

    view.controller = Fresco.newDraweeControllerBuilder().setImageRequest(
        ImageRequestBuilder.newBuilderWithSource(Uri.parse(image.downloadUrl))
            .setResizeOptions(ResizeOptions(200, 200)).build()
    ).setControllerListener(onLoaded { onImageLoadCallback() }).build()
}

fun onLoaded(callback: () -> Unit) = object : ControllerListener<Any> {
    override fun onSubmit(id: String?, callerContext: Any?) = Unit

    override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) = Unit

    override fun onFailure(id: String?, throwable: Throwable?) = Unit

    override fun onRelease(id: String?) = Unit

    override fun onIntermediateImageSet(id: String?, imageInfo: Any?) = Unit

    override fun onFinalImageSet(
        id: String?, imageInfo: Any?, animatable: Animatable?
    ) {
        callback()
    }
}

@ExperimentalFoundationApi
@Preview(showSystemUi = true)
@Composable
fun ImageItemViewPreview() {
    KoinPreview {
        Column {
            ImageItemView(
                ImageValue.Samples.simpleImageValeSample,
                {},
                { }
            )
        }
    }
}

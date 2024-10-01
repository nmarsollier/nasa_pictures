package com.nmarsollier.nasa.ui.imagePreview

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.samples.zoomable.ZoomableDraweeView
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.ui.KoinPreview
import com.nmarsollier.nasa.models.api.images.ImageValue

@Composable
fun ImagePreviewContent(
    image: ImagePreviewState.Ready,
    reduce: (ImagePreviewAction) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blackBackground))
    ) {

        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            factory = { context ->
                ZoomableDraweeView(context)
            },
            update = { view ->
                loadImage(view, image.imageValue)
            })

        if (image.showDetails) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.blueBackground))
            ) {
                ImageSheet(image = image.imageValue, reduce)
            }
        }
    }
}

@Composable
fun ImageSheet(image: ImageValue, reduce: (ImagePreviewAction) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.image_details),
                fontSize = 20.sp,
                color = colorResource(id = R.color.textWhite)
            )
            Spacer(Modifier.weight(1f))
            Image(
                painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                contentDescription = null,
                modifier = Modifier.clickable { reduce(ImagePreviewAction.ToggleDetails) }
            )
        }
        Text(
            text = stringResource(id = R.string.image_number),
            fontSize = 16.sp,
            color = colorResource(id = R.color.textColorLightGray),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = image.identifier,
            fontSize = 18.sp,
            color = colorResource(id = R.color.textWhite),
            modifier = Modifier.padding(top = 2.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.capture),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textColorLightGray)
                )
                Text(
                    text = image.formattedHourMinute,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textWhite),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(id = R.string.latitude),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textColorLightGray)
                )
                Text(
                    text = image.coordinates.lat.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textWhite),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Column {
                Text(
                    text = stringResource(id = R.string.longitude),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.textColorLightGray)
                )
                Text(
                    text = image.coordinates.lon.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.textWhite),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

    }
}

private fun loadImage(view: ZoomableDraweeView, image: ImageValue) {
    view.controller = Fresco.newDraweeControllerBuilder().setImageRequest(
        ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(image.downloadUrl))
            .build()
    ).build()
}

@Preview(showSystemUi = true)
@Composable
fun OptionsViewPreview() {
    KoinPreview {
        Column {
            ImagePreviewContent(
                ImagePreviewState.Ready(
                    imageValue = ImageValue.Samples.simpleImageValeSample,
                    showDetails = false
                )
            ) { }
        }
    }
}

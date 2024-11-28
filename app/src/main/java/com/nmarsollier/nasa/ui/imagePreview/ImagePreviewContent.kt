package com.nmarsollier.nasa.ui.imagePreview

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Bitmap
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.ui.CoilUtils
import com.nmarsollier.nasa.common.ui.KoinPreview
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.api.images.Samples
import org.koin.compose.koinInject

@Composable
fun ImagePreviewContent(
    image: ImagePreviewState.Ready,
    coilUtils: CoilUtils = koinInject(),
    reduce: (ImagePreviewAction) -> Unit
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(image.imageValue.downloadUrl) {
        imageBitmap = coilUtils.loadImage(image.imageValue.downloadUrl)
    }
    val imageBm = imageBitmap

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blackBackground))
    ) {
        if (imageBm != null) {
            ZoomableImage(
                modifier = Modifier.fillMaxSize(),
                imageBitmap = imageBm
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .padding(16.dp),
                color = colorResource(id = R.color.textWhite)
            )
        }

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
                rememberVectorPainter(Icons.Filled.Close),
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

@Preview
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

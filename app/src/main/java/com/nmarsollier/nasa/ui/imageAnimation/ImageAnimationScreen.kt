package com.nmarsollier.nasa.ui.imageAnimation

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.ui.EmptyView
import com.nmarsollier.nasa.common.ui.KoinPreview
import com.nmarsollier.nasa.common.ui.LoadingView
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImageAnimationScreen(
    date: ExtendedDateValue, viewModel: ImageAnimationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    DisposableEffect(date) {
        viewModel.reduce(ImageAnimationAction.FetchImages(date))
        onDispose { }
    }

    Column(
        modifier = Modifier
            .background(
                colorResource(id = R.color.blueBackground)
            )
            .fillMaxSize()
    ) {
        when (val st = state) {
            is ImageAnimationState.Ready -> AnimatedPreviewContent(st)
            ImageAnimationState.Error -> EmptyView()
            else -> LoadingView()
        }
    }
}

val Int.px
    get() = (this / Resources.getSystem().displayMetrics.density).dp

@Composable
fun AnimatedPreviewContent(state: ImageAnimationState.Ready, frameDuration: Long = 50) {
    var currentFrame by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentFrame) {
        delay(frameDuration)
        currentFrame = (currentFrame + 1) % state.bitmaps.size
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blackBackground))
    ) {
        Canvas(modifier = Modifier.size(600.px, 600.px)) {
            drawImage(image = state.bitmaps[currentFrame].asImageBitmap(), topLeft = Offset.Zero)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OptionsViewPreview() {
    KoinPreview {
        Column {
            AnimatedPreviewContent(
                ImageAnimationState.Ready(
                    bitmaps = emptyList()
                )
            )
        }
    }
}

package com.nmarsollier.nasa.ui.imageAnimation

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.ui.EmptyView
import com.nmarsollier.nasa.common.ui.KoinPreview
import com.nmarsollier.nasa.common.ui.LoadingView
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImageAnimationScreen(
    date: ExtendedDateValue,
    viewModel: ImageAnimationViewModel = koinViewModel()
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

@Composable
fun AnimatedPreviewContent(state: ImageAnimationState.Ready) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blackBackground))
    ) {
        AndroidView(modifier = Modifier
            .align(Alignment.Center)
            .width(400.dp)
            .height(400.dp),
            factory = { context ->
                ImageView(context)
            },
            update = { view ->
                view.setBackgroundDrawable(state.animation)
                state.animation.start()
            })
    }
}

@Preview(showSystemUi = true)
@Composable
fun OptionsViewPreview() {
    KoinPreview {
        Column {
            AnimatedPreviewContent(
                ImageAnimationState.Ready(
                    animation = AnimationDrawable()
                )
            )
        }
    }
}

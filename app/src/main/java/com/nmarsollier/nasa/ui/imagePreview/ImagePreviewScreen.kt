package com.nmarsollier.nasa.ui.imagePreview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.nmarsollier.nasa.res.AppColors
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.ui.utils.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImagePreviewScreen(
    imageValue: ImageValue,
    viewModel: ImagePreviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    DisposableEffect(imageValue) {
        viewModel.reduce(ImagePreviewAction.Init(imageValue))
        onDispose { }
    }

    Scaffold(
        floatingActionButton = {
            if ((state as? ImagePreviewState.Ready)?.showDetails == false) {
                FloatingActionButton(
                    onClick = {
                        viewModel.reduce(ImagePreviewAction.ToggleDetails)
                    },
                    shape = CircleShape,
                    backgroundColor = AppColors.LightBlueBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        "",
                        tint = AppColors.TextWhite
                    )
                }
            }
        }, floatingActionButtonPosition = FabPosition.End, modifier = Modifier
            .background(AppColors.BlackBackground)
            .padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(AppColors.BlackBackground)
                .fillMaxSize()
        ) {
            when (val st = state) {
                is ImagePreviewState.Ready -> ImagePreviewContent(st, reduce = viewModel::reduce)
                else -> LoadingView()
            }
        }
    }
}

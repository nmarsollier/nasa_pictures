package com.example.exercise.ui.imagePreview

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.common.navigation.AppNavActions
import com.example.exercise.common.ui.LoadingView
import com.example.exercise.models.api.images.ImageValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ImagePreviewScreen(
    imageValue: ImageValue,
    viewModel: ImagePreviewViewModel = koinViewModel(),
    navActions: AppNavActions = koinInject()
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
                    backgroundColor = colorResource(R.color.lightBlueBackground)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        "",
                        tint = colorResource(R.color.textWhite)
                    )
                }
            }
        }, floatingActionButtonPosition = FabPosition.End, modifier = Modifier
            .background(
                colorResource(id = R.color.blackBackground)
            )
            .padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(
                    colorResource(id = R.color.blackBackground)
                )
                .fillMaxSize()
        ) {
            when (val st = state) {
                is ImagePreviewState.Ready -> ImagePreviewContent(st, viewModel::reduce)
                else -> LoadingView()
            }
        }
    }
}

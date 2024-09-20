package com.example.exercise.ui.images

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.exercise.common.ui.EmptyView
import com.example.exercise.common.ui.ErrorView
import com.example.exercise.common.ui.LoadingView
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.animatedPreiew.AnimatedPreviewActivity
import com.example.exercise.ui.home.MainScreenUpdate
import com.example.exercise.ui.imagePreview.ImagePreviewActivity
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ImagesScreen(
    date: ExtendedDateValue,
    viewModel: ImagesViewModel = koinViewModel(),
    mainScreenUpdate: MainScreenUpdate = koinInject()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val event by viewModel.event.collectAsState(null, viewModel.viewModelScope.coroutineContext)

    DisposableEffect(date) {
        viewModel.reduce(ImagesAction.FetchImages(date))
        onDispose { }
    }

    when (val e = event) {
        is ImagesEvent.GoAnimate -> AnimatedPreviewActivity.startActivity(
            LocalContext.current, e.date
        )

        is ImagesEvent.GoPreview -> ImagePreviewActivity.startActivity(
            LocalContext.current, e.image
        )

        null -> Unit
    }

    Scaffold(topBar = {
        ImagesMenu(date) {
            mainScreenUpdate.updateScreen()
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            when (val st = state) {
                is ImagesState.Ready -> {
                    if (st.images.isEmpty()) {
                        EmptyView()
                    } else {
                        ImagesListContent(st, viewModel::reduce)
                    }
                }

                is ImagesState.Error -> ErrorView {
                    viewModel.reduce(ImagesAction.FetchImages(date))
                }

                else -> LoadingView()
            }
        }
    }
}

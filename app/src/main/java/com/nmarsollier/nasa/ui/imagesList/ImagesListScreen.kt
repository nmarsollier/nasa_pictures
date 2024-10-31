package com.nmarsollier.nasa.ui.imagesList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.nmarsollier.nasa.common.navigation.AppNavActions
import com.nmarsollier.nasa.common.ui.EmptyView
import com.nmarsollier.nasa.common.ui.ErrorView
import com.nmarsollier.nasa.common.ui.LoadingView
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.ui.home.HomeScreenUpdater
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ImagesListScreen(
    date: ExtendedDateValue,
    viewModel: ImagesListViewModel = koinViewModel(),
    homeScreenUpdater: HomeScreenUpdater = koinInject(),
    navActions: AppNavActions = koinInject()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val event by viewModel.event.collectAsState(null, viewModel.viewModelScope.coroutineContext)

    DisposableEffect(date) {
        viewModel.reduce(ImagesListAction.FetchImages(date))
        onDispose { }
    }

    when (val e = event) {
        is ImagesListEvent.GoAnimate -> {
            navActions.goImageAnimation(e.date)
        }

        is ImagesListEvent.GoPreview -> {
            navActions.goImagePreview(e.image)
        }

        null -> Unit
    }

    Scaffold(topBar = {
        ImagesListMenu(date) {
            homeScreenUpdater.updateScreen()
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            when (val st = state) {
                is ImagesListState.Ready -> {
                    if (st.images.isEmpty()) {
                        EmptyView()
                    } else {
                        ImagesListContent(st, viewModel::reduce)
                    }
                }

                is ImagesListState.Error -> ErrorView {
                    viewModel.reduce(ImagesListAction.FetchImages(date))
                }

                else -> LoadingView()
            }
        }
    }
}

package com.example.exercise.ui.images

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.animatedPreiew.AnimatedPreviewActivity
import com.example.exercise.ui.common.EmptyView
import com.example.exercise.ui.common.ErrorView
import com.example.exercise.ui.common.LoadingView
import com.example.exercise.ui.imagePreview.ImagePreviewActivity
import com.example.exercise.ui.utils.Samples
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(
    date: ExtendedDateValue,
    viewModel: ImagesViewModel = koinViewModel(),
    dateViewModel: ImagesDateViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val datesState by dateViewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    LaunchedEffect(Unit) {
        dateViewModel.updateDate(date)
        viewModel.fetchImages(date)
    }

    val context = LocalContext.current

    Scaffold(topBar = {
        ImagesMenu(date)
    }) {
        Column(
            modifier = Modifier
                .background(
                    colorResource(id = R.color.blueBackground)
                )
                .fillMaxSize()
        ) {
            when (val st = state) {
                is ImagesState.Ready -> {
                    if (st.images.isEmpty()) {
                        EmptyView()
                    } else {
                        ImagesListPreview(st, viewModel, datesState, dateViewModel)
                    }
                }

                is ImagesState.Error -> ErrorView {
                    viewModel.fetchImages(date)
                }

                is ImagesState.Redirect -> when (val d = st.destination) {
                    is Destination.Animate -> AnimatedPreviewActivity.startActivity(context, d.date)
                    is Destination.Preview -> ImagePreviewActivity.startActivity(context, d.image)
                }

                else -> LoadingView()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        ImagesScreen(
            ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample,
            koinViewModel()
        )
    }
}

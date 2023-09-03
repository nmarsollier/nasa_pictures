package com.example.exercise.ui.images

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.ui.animatedPreiew.AnimatedPreviewActivity
import com.example.exercise.ui.common.EmptyView
import com.example.exercise.ui.common.ErrorView
import com.example.exercise.ui.common.LoadingView
import com.example.exercise.ui.imagePreview.ImagePreviewActivity
import com.example.exercise.ui.utils.providedViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesScreen(
    date: ExtendedDateValue,
    viewModel: ImagesViewModel = providedViewModel(),
    dateViewModel: ImagesDateViewModel = providedViewModel()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    dateViewModel.updateDate(date)
                    viewModel.fetchImages(date)
                }

                else -> Unit
            }
        }.also {
            lifecycleOwner.lifecycle.addObserver(it)
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
                        ImagesListView(st)
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
            ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample, ImagesViewModel()
        )
    }
}

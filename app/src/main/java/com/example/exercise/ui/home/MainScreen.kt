package com.example.exercise.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.exercise.R
import com.example.exercise.ui.common.ErrorView
import com.example.exercise.ui.common.KoinPreview
import com.example.exercise.ui.common.LoadingView
import com.example.exercise.ui.images.ImagesActivity
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.syncDates()
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

    Scaffold(
        topBar = {
            MainMenu()
        }, modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        Column {
            when (val st = state) {
                is MainState.Ready -> DatesListContent(st, viewModel)
                is MainState.Error -> ErrorView {
                    viewModel.syncDates()
                }

                is MainState.Redirect -> when (val d = st.destination) {
                    is Destination.Images -> ImagesActivity.startActivity(context, d.date)
                }

                else -> LoadingView()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    KoinPreview {
        MainScreen(koinViewModel())
    }
}

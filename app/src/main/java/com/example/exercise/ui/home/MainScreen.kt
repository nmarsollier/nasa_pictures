package com.example.exercise.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.exercise.ui.common.ui.ErrorView
import com.example.exercise.ui.common.ui.KoinPreview
import com.example.exercise.ui.common.ui.LoadingView
import com.example.exercise.ui.images.ImagesActivity
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val context = LocalContext.current

    // No queda otra si queremos que se actualice cuando
    // vuelve de la pantalla de imágenes
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.reduce(MainAction.SyncDates)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (val e = event) {
                is MainEvent.GoImages -> {
                    ImagesActivity.startActivity(context, e.date)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MainMenu()
        }, modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        Column {
            when (val st = state) {
                is MainState.Ready -> DatesListContent(st, viewModel::reduce)
                is MainState.Error -> ErrorView {
                    viewModel.reduce(MainAction.SyncDates)
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

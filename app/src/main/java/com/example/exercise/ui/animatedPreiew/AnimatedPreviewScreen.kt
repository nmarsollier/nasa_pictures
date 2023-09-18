package com.example.exercise.ui.animatedPreiew

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.ui.common.EmptyView
import com.example.exercise.ui.common.LoadingView
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnimatedPreviewScreen(
    date: ExtendedDateValue, viewModel: AnimatedPreviewViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
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

    Column(
        modifier = Modifier
            .background(
                colorResource(id = R.color.blueBackground)
            )
            .fillMaxSize()
    ) {
        when (val st = state) {
            is AnimatedPreviewState.Ready -> AnimatedPreviewContent(st)
            AnimatedPreviewState.Error -> EmptyView()
            else -> LoadingView()
        }
    }
}


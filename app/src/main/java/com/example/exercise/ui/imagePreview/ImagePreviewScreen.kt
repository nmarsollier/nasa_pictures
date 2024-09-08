package com.example.exercise.ui.imagePreview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.ui.common.ui.LoadingView
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagePreviewScreen(viewModel: ImagePreviewViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)

    Scaffold(
        floatingActionButton = {
            if ((state as? ImagePreviewState.Ready)?.showDetails == false) {
                FloatingActionButton(
                    onClick = {
                        viewModel.toggleDetails()
                    },
                    shape = CircleShape,
                    backgroundColor = colorResource(R.color.lightBlueBackground)
                ) {
                    Icon(
                        painterResource(id = android.R.drawable.ic_dialog_info),
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

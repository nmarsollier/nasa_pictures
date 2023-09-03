package com.example.exercise.ui.animatedPreiew

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exercise.MainApplication
import com.example.exercise.R

@Composable
fun AnimatedPreviewView(state: AnimatedPreviewState.Ready) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blackBackground))
    ) {
        AndroidView(modifier = Modifier
            .align(Alignment.Center)
            .width(400.dp)
            .height(400.dp),
            factory = { context ->
                ImageView(context)
            },
            update = { view ->
                view.setBackgroundDrawable(state.animation)
                state.animation.start()
            })
    }
}

@Preview(showSystemUi = true)
@Composable
fun OptionsViewPreview() {
    MainApplication.initializeLibrary(LocalContext.current)

    MaterialTheme {
        Column {
            AnimatedPreviewView(
                AnimatedPreviewState.Ready(
                    animation = AnimationDrawable()
                )
            )
        }
    }
}

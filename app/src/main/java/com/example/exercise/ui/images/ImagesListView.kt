package com.example.exercise.ui.images

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercise.MainApplication
import com.example.exercise.R
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.businessObjects.ImageValue

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesListView(
    state: ImagesState.Ready
) {
    Column(
        modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        HeaderButtonButton()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.images) {
                ImageItemView(it)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ImagesListViewPreview() {
    MainApplication.initializeLibrary(LocalContext.current)

    MaterialTheme {
        ImagesListView(
            ImagesState.Ready(
                images = listOf(
                    ImageValue.Samples.simpleImageValeSample
                ),
                CacheStrategy.NETWORK
            )
        )
    }
}

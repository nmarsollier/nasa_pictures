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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercise.R
import com.example.exercise.common.ui.KoinPreview
import com.example.exercise.common.utils.Samples
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagesListContent(
    state: ImagesState.Ready,
    imagesReducer: (event: ImagesAction) -> Unit,
    datesState: ImagesDateState,
    datesReducer: (event: ImagesDateAction) -> Unit,
) {
    Column(
        modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        HeaderButtonButton(datesState, imagesReducer)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.images) {
                ImageItemView(it, imagesReducer, datesReducer)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ImagesListViewPreview() {
    KoinPreview {
        ImagesListContent(
            ImagesState.Ready(
                images = listOf(
                    ImageValue.Samples.simpleImageValeSample
                )
            ),
            {},
            ImagesDateState.Ready(
                ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample
            ),
            { }
        )
    }
}

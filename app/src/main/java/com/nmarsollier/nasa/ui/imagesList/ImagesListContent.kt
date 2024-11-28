package com.nmarsollier.nasa.ui.imagesList

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
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.ui.KoinPreview
import com.nmarsollier.nasa.common.utils.Samples
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.api.images.Samples
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesListContent(
    state: ImagesListState.Ready,
    imagesReducer: (event: ImagesListAction) -> Unit,
) {
    Column(
        modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        HeaderButton(state, imagesReducer)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.images) {
                ImageItemContent(image = it, reduce = imagesReducer)
            }
        }
    }
}

@Preview
@Composable
fun ImagesListViewPreview() {
    KoinPreview {
        ImagesListContent(
            ImagesListState.Ready(
                images = listOf(
                    ImageValue.Samples.simpleImageValeSample
                ), date = ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample
            ),
        ) {}
    }
}

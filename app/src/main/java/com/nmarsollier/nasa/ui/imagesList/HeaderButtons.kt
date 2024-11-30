package com.nmarsollier.nasa.ui.imagesList


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmarsollier.nasa.res.AppColors
import com.nmarsollier.nasa.res.AppStrings
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.ui.utils.ExtendedDateValueSamples

@Composable
@ExperimentalFoundationApi
fun HeaderButton(
    state: ImagesListState,
    imagesReducer: (a: ImagesListAction) -> Unit
) {
    when (state) {
        ImagesListState.Loading -> LoadingItemsButton()
        is ImagesListState.Ready -> if (state.date?.isLoading == false) {
            PlayImagesButton(state.date, imagesReducer)
        } else {
            LoadingItemsButton()
        }

        ImagesListState.Error -> Unit
    }
}

@Composable
@ExperimentalFoundationApi
fun LoadingItemsButton() {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = AppColors.BlueCardBackground,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = AppStrings.downloadingImages,
                color = AppColors.TextColorGray,
                fontSize = 20.sp
            )

            Spacer(Modifier.width(16.dp))

            Image(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppColors.TextColorGray),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
@ExperimentalFoundationApi
fun PlayImagesButton(
    dates: ExtendedDateValue,
    reduce: (a: ImagesListAction) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = (AppColors.LightBlueCardBackground),
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .height(48.dp)
            .clickable { reduce(ImagesListAction.GoAnimate(dates)) }
            .fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = AppStrings.playImages,
                color = AppColors.TextWhite,
                fontSize = 20.sp
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun LoadingItemsViewPreview() {
    MaterialTheme {
        Column {
            LoadingItemsButton()
            PlayImagesButton(
                ExtendedDateValueSamples.fullyLoadedExtendedDateValueSample
            ) {}
            HeaderButton(
                ImagesListState.Ready(
                    images = emptyList(),
                    date = ExtendedDateValueSamples.partialLoadedExtendedDateValueSample
                )
            ) { }
            HeaderButton(
                ImagesListState.Ready(
                    images = emptyList(),
                    date = ExtendedDateValueSamples.fullyLoadedExtendedDateValueSample
                )
            ) { }
        }
    }
}


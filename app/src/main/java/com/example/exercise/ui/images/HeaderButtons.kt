package com.example.exercise.ui.images


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.exercise.R
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.ui.utils.providedViewModel

@Composable
@ExperimentalFoundationApi
fun HeaderButtonButton(
    imagesViewModel: ImagesViewModel = providedViewModel(),
    dateViewModel: ImagesDateViewModel = providedViewModel()
) {
    val state by dateViewModel.state.collectAsState(dateViewModel.viewModelScope.coroutineContext)

    when (val st = state) {
        ImagesDateState.Loading -> LoadingItemsButton()
        is ImagesDateState.Ready -> if (st.date?.isLoading == false) {
            PlayImagesButton(st.date, imagesViewModel)
        } else {
            LoadingItemsButton()
        }
    }
}

@Composable
@ExperimentalFoundationApi
fun LoadingItemsButton() {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = (colorResource(id = R.color.blueCardBackground)),
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
                text = stringResource(R.string.downloading_images),
                color = colorResource(R.color.textColorGray),
                fontSize = 20.sp
            )

            Spacer(Modifier.width(16.dp))

            Image(
                painterResource(id = R.drawable.ic_reload),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(R.color.textColorGray)),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
@ExperimentalFoundationApi
fun PlayImagesButton(dates: ExtendedDateValue, imagesViewModel: ImagesViewModel) {
    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10.dp),
        backgroundColor = (colorResource(id = R.color.lightBlueCardBackground)),
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .height(48.dp)
            .clickable { imagesViewModel.redirect(Destination.Animate(dates)) }
            .fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.play_images),
                color = colorResource(R.color.textWhite),
                fontSize = 20.sp
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(showSystemUi = true)
@Composable
fun LoadingItemsViewPreview() {
    MaterialTheme {
        Column {
            LoadingItemsButton()
            PlayImagesButton(
                ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample,
                ImagesViewModel()
            )
            HeaderButtonButton(
                ImagesViewModel(),
                ImagesDateViewModel(
                    ImagesDateState.Ready(
                        date = ExtendedDateValue.Samples.partialLoadedExtendedDateValueSample
                    )
                )
            )
            HeaderButtonButton(
                ImagesViewModel(),
                ImagesDateViewModel(
                    ImagesDateState.Ready(
                        date = ExtendedDateValue.Samples.fullyLoadedExtendedDateValueSample
                    )
                )
            )
        }
    }
}


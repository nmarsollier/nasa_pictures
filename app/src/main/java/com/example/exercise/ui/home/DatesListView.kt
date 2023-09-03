package com.example.exercise.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.exercise.R
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.businessObjects.ExtendedDateValue

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DatesListView(state: MainState.Ready) {
    Column(
        modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(state.dates) {
                DateItemView(it)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DatesListViewPreview() {
    MaterialTheme {
        DatesListView(
            MainState.Ready(
                ExtendedDateValue.Samples.combinedListExtendedDateValueSample,
                CacheStrategy.NETWORK
            )
        )
    }
}

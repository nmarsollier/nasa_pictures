package com.example.exercise.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.exercise.R

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DatesListContent(
    state: MainState.Ready, reducer: MainReducer
) {
    val dates = state.pager.collectAsLazyPagingItems()

    when (dates.loadState.refresh) {
        else -> {
            Column(
                modifier = Modifier.background(colorResource(id = R.color.blueBackground))
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(
                        count = dates.itemCount,
                        key = dates.itemKey { it.date },
                        contentType = dates.itemContentType { "contentType" }) { index ->
                        dates[index]?.let {
                            DateItemView(it, reducer)
                        }
                    }
                }
            }
        }
    }
}

/*
@Preview(showSystemUi = true)
@Composable
fun DatesListViewPreview() {
    KoinPreview {
        DatesListContent(
            MainState.Ready(
                Pager(ExtendedDateValue.Samples.combinedListExtendedDateValueSample)
            ), MainReducer.Samples.empty
        )
    }
}
*/

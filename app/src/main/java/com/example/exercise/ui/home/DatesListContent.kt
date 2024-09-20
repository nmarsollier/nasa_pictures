package com.example.exercise.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.exercise.R
import com.example.exercise.common.ui.LoadingView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatesListContent(
    state: MainState.Ready, reduce: (MainAction) -> Unit
) {
    val dates = state.pager.collectAsLazyPagingItems()

    when (dates.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingView()
        }

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
                            DateItemView(it, reduce)
                        }
                    }
                }
            }
        }
    }
}

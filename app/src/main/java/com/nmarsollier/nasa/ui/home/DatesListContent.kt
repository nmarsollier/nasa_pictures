package com.nmarsollier.nasa.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.nmarsollier.nasa.res.AppColors
import com.nmarsollier.nasa.ui.utils.LoadingView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatesListContent(
    state: HomeState.Ready, reduce: (MainAction) -> Unit
) {
    val dates = state.pager.collectAsLazyPagingItems()

    when (dates.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingView()
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .background(AppColors.BlueBackground)
                    .fillMaxSize()
            ) {
                items(
                    count = dates.itemCount,
                    key = dates.itemKey { it.date }
                ) { index ->
                    dates[index]?.let {
                        DateItemView(it, reduce)
                    }
                }
            }
        }
    }
}

package com.nmarsollier.nasa.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewModelScope
import com.nmarsollier.nasa.R
import com.nmarsollier.nasa.common.navigation.AppNavActions
import com.nmarsollier.nasa.common.ui.ErrorView
import com.nmarsollier.nasa.common.ui.LoadingView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(), navActions: AppNavActions = koinInject()
) {
    val state by viewModel.state.collectAsState(viewModel.viewModelScope.coroutineContext)
    val event by viewModel.event.collectAsState(viewModel.viewModelScope.coroutineContext)

    when (val e = event) {
        is MainEvent.GoImages -> {
            navActions.goImagesList(e.date)
        }
    }

    Scaffold(
        topBar = {
            HomeMenu()
        }, modifier = Modifier.background(colorResource(id = R.color.blueBackground))
    ) {
        Box(Modifier.padding(it)) {
            when (val st = state) {
                is HomeState.Ready -> DatesListContent(st, viewModel::reduce)
                is HomeState.Error -> ErrorView {
                    viewModel.reduce(MainAction.RefreshDates)
                }

                else -> LoadingView()
            }
        }
    }
}

package com.nmarsollier.nasa.ui.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.nmarsollier.nasa.common.vm.StateViewModel
import com.nmarsollier.nasa.models.api.dates.asDateValue
import com.nmarsollier.nasa.models.api.dates.asExtendedDateValue
import com.nmarsollier.nasa.models.database.dates.DatesEntity
import com.nmarsollier.nasa.models.database.dates.DatesEntityDao
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.models.extendedDate.FrescoUtils
import com.nmarsollier.nasa.models.useCases.FetchDatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

sealed interface MainEvent {
    @Stable
    data class GoImages(val date: ExtendedDateValue) : MainEvent
}

sealed interface HomeState {
    @Stable
    data object Loading : HomeState

    @Stable
    data object Error : HomeState

    @Stable
    data class Ready(
        val pager: Flow<PagingData<ExtendedDateValue>>
    ) : HomeState
}

sealed interface MainAction {
    @Stable
    data object RefreshDates : MainAction

    @Stable
    data class GoImages(val date: ExtendedDateValue) : MainAction
}

class HomeViewModel(
    private val dateDao: DatesEntityDao,
    private val frescoUtils: FrescoUtils,
    private val fetchDatesUseCase: FetchDatesUseCase,
    private val homeScreenUpdater: HomeScreenUpdater
) : StateViewModel<HomeState, MainEvent, MainAction>(HomeState.Loading) {
    init {
        loadDates()

        viewModelScope.launch(Dispatchers.IO) {
            fetchDatesUseCase.updateFlow.collect {
                reduce(MainAction.RefreshDates)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            homeScreenUpdater.updateFlow.collect {
                reduce(MainAction.RefreshDates)
            }
        }
    }

    override fun reduce(action: MainAction) {
        when (action) {
            MainAction.RefreshDates -> loadDates()
            is MainAction.GoImages -> MainEvent.GoImages(action.date).sendToEvent()
        }
    }

    private fun loadDates() = viewModelScope.launch(Dispatchers.IO) {
        HomeState.Ready(createPager()).sendToState()
    }

    private fun createPager(): Flow<PagingData<ExtendedDateValue>> {
        return Pager(PagingConfig(pageSize = 30)) {
            DatesPagingSource(dateDao, frescoUtils)
        }.flow.cachedIn(viewModelScope)
    }
}

private const val PAGE_SIZE = 30;

class DatesPagingSource(
    private val dateRepository: DatesEntityDao, private val frescoUtils: FrescoUtils
) : PagingSource<Int, ExtendedDateValue>() {
    override fun getRefreshKey(state: PagingState<Int, ExtendedDateValue>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExtendedDateValue> {
        val loadPage = params.key ?: 1
        return dateRepository.findAll(loadPage * PAGE_SIZE, PAGE_SIZE).asResultPage(loadPage)
    }

    private suspend fun List<DatesEntity>?.asResultPage(loadPage: Int): LoadResult.Page<Int, ExtendedDateValue> =
        (this ?: emptyList()).let {
            LoadResult.Page(
                data = it.map { entity -> entity.date.asDateValue.asExtendedDateValue(frescoUtils) },
                prevKey = if (loadPage > 1) loadPage - 1 else null,
                nextKey = if (it.size == PAGE_SIZE) loadPage + 1 else null
            )
        }
}
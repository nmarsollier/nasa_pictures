package com.example.exercise.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.exercise.models.api.dates.asDateValue
import com.example.exercise.models.api.dates.asExtendedDateValue
import com.example.exercise.models.database.config.LocalDatabase
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import com.example.exercise.models.useCases.FetchDatesUseCase
import com.example.exercise.ui.common.vm.StateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface MainEvent {
    data class GoImages(val date: ExtendedDateValue) : MainEvent
}

sealed interface MainState {
    data object Loading : MainState

    data object Error : MainState

    data class Ready(
        val pager: Flow<PagingData<ExtendedDateValue>>
    ) : MainState
}

sealed interface MainAction {
    data object SyncDates : MainAction
    data class GoImages(val date: ExtendedDateValue) : MainAction
}

class MainViewModel(
    private val fetchDatesUseCase: FetchDatesUseCase,
    database: LocalDatabase,
    private val frescoUtils: FrescoUtils
) : StateViewModel<MainState, MainEvent, MainAction>(MainState.Loading) {
    private val dateDao = database.datesDao()

    private fun createPager(): Flow<PagingData<ExtendedDateValue>> {
        return Pager(PagingConfig(pageSize = 30)) {
            DatesPaging(dateDao, frescoUtils)
        }.flow
    }

    override fun reduce(action: MainAction) {
        when (action) {
            MainAction.SyncDates -> syncDates()
            is MainAction.GoImages -> MainEvent.GoImages(action.date).sendToEvent()
        }
    }

    private fun syncDates() = viewModelScope.launch(Dispatchers.IO) {
        if (dateDao.findLast() != null) {
            MainState.Ready(createPager()).sendToState()
        } else {
            MainState.Loading.sendToState()
        }

        coroutineScope {
            fetchDatesUseCase.syncDates()
            MainState.Ready(createPager()).sendToState()
        }
    }
}

private const val PAGE_SIZE = 30;

class DatesPaging(
    private val dateRepository: DatesEntityDao, private val frescoUtils: FrescoUtils
) : PagingSource<Int, ExtendedDateValue>() {
    override fun getRefreshKey(state: PagingState<Int, ExtendedDateValue>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2)
            .coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExtendedDateValue> =
        suspendCoroutine {
            val loadPage = params.key ?: 1
            MainScope().launch(Dispatchers.IO) {
                it.resume(
                    dateRepository.findAll(loadPage * PAGE_SIZE, PAGE_SIZE).asResultPage(loadPage)
                )
            }
        }

    private suspend fun List<DatesEntity>?.asResultPage(loadPage: Int)
            : LoadResult.Page<Int, ExtendedDateValue> =
        (this ?: emptyList()).let {
            LoadResult.Page(
                data = it.map { entity -> entity.date.asDateValue.asExtendedDateValue(frescoUtils) },
                prevKey = if (loadPage > 1) loadPage - 1 else null,
                nextKey = if (it.size == PAGE_SIZE) loadPage + 1 else null
            )
        }
}
package com.example.exercise.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.businessObjects.asDateValue
import com.example.exercise.models.businessObjects.asExtendedDateValue
import com.example.exercise.models.database.config.ExampleDatabase
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.FrescoUtils
import com.example.exercise.models.useCases.FetchDatesUseCase
import com.example.exercise.ui.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val PAGE_SIZE = 30;

sealed class Destination {
    data class Images(val date: ExtendedDateValue) : Destination()
}

sealed class MainState {
    data object Loading : MainState()

    data object Error : MainState()

    data class Redirect(val destination: Destination) : MainState()

    data class Ready(
        val pager: Flow<PagingData<ExtendedDateValue>>
    ) : MainState()
}

interface MainReducer {
    fun syncDates(): Job
    fun redirect(destination: Destination)

    companion object
}

class MainViewModel(
    private val fetchDatesUseCase: FetchDatesUseCase,
    database: ExampleDatabase,
    private val frescoUtils: FrescoUtils
) : BaseViewModel<MainState>(MainState.Loading), MainReducer {
    private val dateDao = database.datesDao()

    private val pager = Pager(PagingConfig(pageSize = 30)) {
        DatesPaging(dateDao, frescoUtils)
    }.flow

    override fun syncDates() = viewModelScope.launch(Dispatchers.IO) {
        MainState.Ready(pager).sendToState()

        coroutineScope {
            fetchDatesUseCase.syncDates()
            MainState.Ready(pager).sendToState()
        }
    }

    override fun redirect(destination: Destination) {
        MainState.Redirect(destination).sendToState()
    }
}

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
package com.example.exercise.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.dates.DateRepository
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
    private val dateRepository: DateRepository,
    private val frescoUtils: FrescoUtils
) : BaseViewModel<MainState>(MainState.Loading), MainReducer {
    val pager = Pager(PagingConfig(pageSize = 30)) {
        DatesPaging(dateRepository, frescoUtils)
    }.flow

    override fun syncDates() = viewModelScope.launch(Dispatchers.IO) {
        mutableState.update {
            MainState.Ready(pager)
        }

        coroutineScope {
            fetchDatesUseCase.syncDates().let {
                mutableState.update {
                    MainState.Ready(pager)
                }
            }
        }
    }

    override fun redirect(destination: Destination) {
        mutableState.update {
            MainState.Redirect(destination)
        }
    }
}

class DatesPaging(
    private val dateRepository: DateRepository, private val frescoUtils: FrescoUtils
) : PagingSource<Int, ExtendedDateValue>() {
    override fun getRefreshKey(state: PagingState<Int, ExtendedDateValue>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExtendedDateValue> =
        suspendCoroutine {
            val loadPage = params.key ?: 1
            MainScope().launch(Dispatchers.IO) {
                val response = dateRepository.findAll(loadPage) ?: emptyList()

                it.resume(
                    LoadResult.Page(
                        data = response.map { frescoUtils.toDatesData(DateValue(it.date)) },
                        prevKey = if (loadPage > 1) loadPage - 1 else null,
                        nextKey = if (response.size == 30) loadPage + 1 else null
                    )
                )
            }
        }
}
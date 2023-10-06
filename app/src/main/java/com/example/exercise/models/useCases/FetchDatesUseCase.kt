package com.example.exercise.models.useCases

import com.example.exercise.models.api.dates.DatesApi
import com.example.exercise.models.api.dates.asDateEntity
import com.example.exercise.models.api.dates.asDateValue
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.dates.storeIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchDatesUseCase(
    private val datesClient: DatesApi,
    private val dateDao: DatesEntityDao
) {
    suspend fun syncDates() = suspendCoroutine {
        MainScope().launch(Dispatchers.IO) {
            val date = lastDateFromDatabase()
            when {
                date == null -> it.resume(syncRemoteDates())
                date.parsedDate.isBefore(LocalDate.now()) -> it.resume(syncRemoteDates())
                else -> false
            }
        }
    }

    private suspend fun lastDateFromDatabase() =
        suspendCoroutine { suspend ->
            MainScope().launch(Dispatchers.IO) {
                dateDao.findLast()?.let { date ->
                    suspend.resume(date.date.asDateValue)
                } ?: kotlin.run { suspend.resume(null) }
            }
        }

    private suspend fun syncRemoteDates(): Boolean = suspendCoroutine { suspend ->
        MainScope().launch(Dispatchers.IO) {
            datesClient.listDates().let {
                when (val result = it) {
                    is Result.Error -> {
                        suspend.resume(false)
                    }

                    is Result.Success -> {
                        result.data
                            .map { it.asDateEntity }.toList()
                            .storeIn(dateDao)

                        suspend.resume(true)
                    }
                }
            }
        }
    }
}

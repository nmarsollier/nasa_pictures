package com.example.exercise.models.useCases

import com.example.exercise.models.api.dates.DatesApiClient
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.config.ExampleDatabase
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.FrescoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchDatesUseCase(
    private val datesClient: DatesApiClient,
    database: ExampleDatabase,
    private val frescoUtils: FrescoUtils
) {
    private val dateDao = database.datesDao()

    suspend fun syncDates() = suspendCoroutine {
        MainScope().launch(Dispatchers.IO) {
            val dates = lastDateFromDatabase() as Result.Success

            if (dates.data.isEmpty() || dates.data.first().parsedDate.isBefore(LocalDate.now())) {
                when (syncRemoteDates()) {
                    is Result.Error -> it.resume(false)
                    is Result.Success -> it.resume(true)
                }
            } else {
                it.resume(false)
            }
        }
    }

    private suspend fun lastDateFromDatabase() =
        suspendCoroutine<Result<ExtendedDateValue>> { suspend ->
            MainScope().launch(Dispatchers.IO) {
                dateDao.findLast()?.let { date ->
                    suspend.resume(
                        Result.Success(
                            listOf(frescoUtils.toDatesData(DateValue(date.date)))
                        )
                    )
                } ?: kotlin.run { suspend.resume(Result.Success(emptyList())) }
            }
        }

    suspend fun syncRemoteDates(): Result<ExtendedDateValue> = suspendCoroutine { suspend ->
        MainScope().launch(Dispatchers.IO) {
            datesClient.listDates().let {
                when (val result = it) {
                    is Result.Error -> {
                        suspend.resume(Result.Error(result.e))
                    }

                    is Result.Success -> {
                        updateLocalDatabase(result.data)
                        suspend.resume(Result.Success(result.data.map {
                            frescoUtils.toDatesData(
                                DateValue(it.date)
                            )
                        }))
                    }
                }
            }
        }
    }

    private fun updateLocalDatabase(data: List<DateValue>) {
        data.forEach {
            dateDao.insert(DatesEntity(it.date))
        }
    }
}

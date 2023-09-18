package com.example.exercise.models.useCases

import com.example.exercise.models.api.dates.DatesApiClient
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.dates.DateRepository
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.image.FrescoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FetchDatesUseCase(
    private val datesClient: DatesApiClient,
    private val dateRepository: DateRepository,
    private val frescoUtils: FrescoUtils
) {
    suspend fun syncDates() = suspendCoroutine {
        MainScope().launch(Dispatchers.IO) {
            val dates = listDatesFromDatabase() as Result.Success

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

    private suspend fun listDatesFromDatabase() =
        suspendCoroutine<Result<ExtendedDateValue>> { suspend ->
            MainScope().launch(Dispatchers.IO) {
                dateRepository.findAll(1)?.takeIf { it.isNotEmpty() }?.let { dates ->
                    suspend.resume(
                        Result.Success(
                            dates.map { date -> DateValue(date.date) }
                                .map { date -> frescoUtils.toDatesData(date) }
                                .sortedByDescending { it.date })
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
            dateRepository.insert(DatesEntity(it.date))
        }
    }
}

package com.example.exercise.models.useCases

import com.example.exercise.models.api.dates.DateValue
import com.example.exercise.models.api.dates.DatesApi
import com.example.exercise.models.api.dates.asDateEntity
import com.example.exercise.models.api.dates.asDateValue
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.dates.storeIn
import java.time.LocalDate

class FetchDatesUseCase(
    private val datesClient: DatesApi,
    private val dateDao: DatesEntityDao
) {
    suspend fun syncDates() {
        val date = lastDateFromDatabase()
        when {
            date == null -> syncRemoteDates()
            date.parsedDate.isBefore(LocalDate.now()) -> syncRemoteDates()
        }
    }

    private suspend fun lastDateFromDatabase(): DateValue? {
        return dateDao.findLast()?.date?.asDateValue
    }

    private suspend fun syncRemoteDates() {
        try {
            datesClient.listDates()
                .map { it.asDateEntity }.toList()
                .storeIn(dateDao)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

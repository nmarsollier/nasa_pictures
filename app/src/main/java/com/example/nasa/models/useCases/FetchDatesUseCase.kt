package com.nmarsollier.nasa.models.useCases

import androidx.compose.runtime.Stable
import com.nmarsollier.nasa.common.utils.StateEventObject
import com.nmarsollier.nasa.models.api.dates.DateValue
import com.nmarsollier.nasa.models.api.dates.DatesApi
import com.nmarsollier.nasa.models.api.dates.asDateEntity
import com.nmarsollier.nasa.models.api.dates.asDateValue
import com.nmarsollier.nasa.models.database.dates.DatesEntityDao
import com.nmarsollier.nasa.models.database.dates.storeIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Stable
enum class FetchDatesState {
    Updated
}

class FetchDatesUseCase(
    private val datesApi: DatesApi, private val dateDao: DatesEntityDao
) : StateEventObject<FetchDatesState>() {
    init {
        MainScope().launch(Dispatchers.IO) {
            syncDates()
        }
    }

    private suspend fun syncDates() {
        val date = lastDateFromDatabase()
        when {
            date == null -> syncRemoteDates()
            date.parsedDate.isBefore(LocalDate.now().minusDays(1)) -> syncRemoteDates()
        }
    }

    private suspend fun lastDateFromDatabase(): DateValue? {
        return dateDao.findLast()?.date?.asDateValue
    }

    private suspend fun syncRemoteDates() {
        try {
            datesApi.listDates().map { it.asDateEntity }.toList().storeIn(dateDao)
            FetchDatesState.Updated.sendToEvent()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.example.exercise.models.useCases

import com.example.exercise.models.api.dates.DatesApiClient
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.models.api.tools.Result
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.dates.DateRepository
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.ui.utils.toDatesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch


object FetchDatesUseCase {
    private val datesClient = DatesApiClient()

    fun listDates(): Flow<Result<ExtendedDateValue>> = channelFlow {
        var sendError = true
        DateRepository.findAll().take(1).collect {
            it?.takeIf { it.isNotEmpty() }?.let {
                sendError = false
                send(
                    Result.Success(
                        CacheStrategy.DB,
                        it.map { date -> DateValue(date.date) }
                            .map { date -> date.toDatesData() }
                            .sortedByDescending { it.date }
                    )
                )
            }
        }

        datesClient.listDates().take(1).collect {
            when (val result = it) {
                is Result.Error -> {
                    if (sendError) {
                        send(Result.Error(result.e))
                    }
                }

                is Result.Success -> {
                    updateLocalDatabase(result.data)
                    send(
                        Result.Success(
                            CacheStrategy.NETWORK,
                            result.data
                                .map { date -> date.toDatesData() }
                                .sortedByDescending { it.date }
                        )
                    )
                }
            }
            close()
        }
        awaitClose()
    }

    private fun updateLocalDatabase(data: List<DateValue>) = MainScope().launch(Dispatchers.IO) {
        data.forEach {
            DateRepository.insert(DatesEntity(it.date))
        }
    }
}

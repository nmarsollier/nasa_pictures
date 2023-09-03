package com.example.exercise.models.database.dates

import com.example.exercise.models.database.config.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

object DateRepository {
    fun findAll(): Flow<List<DatesEntity>?> = channelFlow {
        send(Database.getRoomDatabase().datesDao().findAll())
    }

    fun insert(date: DatesEntity) = MainScope().launch(Dispatchers.IO) {
        Database.getRoomDatabase().datesDao().let {
            it.insert(date)
        }
    }
}
package com.example.exercise.models.database.dates

import com.example.exercise.models.database.config.ExampleDatabase

class DateRepository(
    private val database: ExampleDatabase
) {
    fun findAll(page: Int): List<DatesEntity>? = database.datesDao().findAll(page * 30, 30)

    fun insert(date: DatesEntity) = database.datesDao().let {
        it.insert(date)
    }
}

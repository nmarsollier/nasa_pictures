package com.example.exercise.models.database.dates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exercise.models.api.dates.DateValue
import com.example.exercise.models.api.dates.asDateEntity

@Dao
interface DatesEntityDao {
    @Query("SELECT * FROM dates ORDER BY date DESC LIMIT :limit OFFSET :from ")
    fun findAll(from: Int, limit: Int): List<DatesEntity>?

    @Query("SELECT * FROM dates ORDER BY date DESC LIMIT 1 OFFSET 0 ")
    fun findLast(): DatesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(date: DatesEntity)
}

fun DatesEntity.storeIn(dao: DatesEntityDao) = dao.insert(this)

fun List<DatesEntity>.storeIn(dao: DatesEntityDao) {
    this.forEach {
        it.storeIn(dao)
    }
}

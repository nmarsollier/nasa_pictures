package com.example.exercise.models.database.dates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatesEntityDao {
    @Query("SELECT * FROM dates ORDER BY date DESC LIMIT :limit OFFSET :from ")
    suspend fun findAll(from: Int, limit: Int): List<DatesEntity>?

    @Query("SELECT * FROM dates ORDER BY date DESC LIMIT 1 OFFSET 0 ")
    suspend fun findLast(): DatesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: DatesEntity)
}

suspend fun DatesEntity.storeIn(dao: DatesEntityDao) = dao.insert(this)

suspend fun List<DatesEntity>.storeIn(dao: DatesEntityDao) {
    this.forEach {
        it.storeIn(dao)
    }
}

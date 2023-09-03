package com.example.exercise.models.database.dates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatesEntityDao {
    @Query("SELECT * FROM dates ORDER BY date DESC")
    fun findAll(): List<DatesEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(date: DatesEntity)

}

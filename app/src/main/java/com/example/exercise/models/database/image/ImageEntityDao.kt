package com.example.exercise.models.database.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageEntityDao {
    @Query("SELECT * FROM images WHERE day=:date ORDER BY date")
    fun findByDate(date: String): List<ImageEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageEntity)
}

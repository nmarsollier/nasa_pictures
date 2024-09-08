package com.example.exercise.models.database.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.ImageEntity
import com.example.exercise.models.database.image.ImageEntityDao

private var INSTANCE: LocalDatabase? = null
private const val DATABASE_NAME = "example"

@Database(entities = [ImageEntity::class, DatesEntity::class], version = 3, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageEntityDao
    abstract fun datesDao(): DatesEntityDao
}

fun getRoomDatabase(androidContext: Context): LocalDatabase {
    return INSTANCE ?: Room.databaseBuilder(
        androidContext, LocalDatabase::class.java, DATABASE_NAME
    ).build().also {
        INSTANCE = it
    }
}

package com.example.exercise.models.database.config

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.exercise.MainApplication
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.ImageEntity
import com.example.exercise.models.database.image.ImageEntityDao

private var INSTANCE: ExampleDatabase? = null
private const val DATABASE_NAME = "example"

@Database(entities = [ImageEntity::class, DatesEntity::class], version = 3, exportSchema = false)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageEntityDao
    abstract fun datesDao(): DatesEntityDao
}

object Database {
    fun getRoomDatabase(): ExampleDatabase {
        return INSTANCE ?: Room.databaseBuilder(
            MainApplication.context, ExampleDatabase::class.java, DATABASE_NAME
        ).build().also {
            INSTANCE = it
        }
    }
}

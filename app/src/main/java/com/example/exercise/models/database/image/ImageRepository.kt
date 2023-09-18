package com.example.exercise.models.database.image

import com.example.exercise.models.database.config.ExampleDatabase

class ImageRepository(
    private val database: ExampleDatabase
) {
    fun findByDate(date: String): List<ImageEntity>? = database.imageDao().findByDate(date)

    fun insert(image: ImageEntity) = database.imageDao().let {
        it.insert(image)
    }
}
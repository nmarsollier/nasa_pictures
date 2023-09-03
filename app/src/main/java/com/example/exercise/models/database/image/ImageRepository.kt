package com.example.exercise.models.database.image

import com.example.exercise.models.database.config.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

object ImageRepository {
    fun findByDate(date: String): Flow<List<ImageEntity>?> = channelFlow {
        send(Database.getRoomDatabase().imageDao().findByDate(date))
    }

    fun insert(image: ImageEntity) = MainScope().launch(Dispatchers.IO) {
        Database.getRoomDatabase().imageDao().let {
            it.insert(image)
        }
    }
}
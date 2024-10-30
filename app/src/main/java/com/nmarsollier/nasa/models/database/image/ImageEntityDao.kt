package com.nmarsollier.nasa.models.database.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nmarsollier.nasa.models.api.images.ImageValue

@Dao
interface ImageEntityDao {
    @Query("SELECT * FROM images WHERE day=:date ORDER BY date")
    suspend fun findByDate(date: String): List<ImageEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: ImageEntity)
}

suspend fun ImageEntity.storeIn(dao: ImageEntityDao) = dao.insert(this)

suspend fun List<ImageValue>.storeIn(dao: ImageEntityDao) {
    this.forEach {
        it.toImageEntity().storeIn(dao)
    }
}

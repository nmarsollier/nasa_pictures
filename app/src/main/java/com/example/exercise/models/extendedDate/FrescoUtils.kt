package com.example.exercise.models.extendedDate

import android.net.Uri
import com.example.exercise.models.api.dates.DateValue
import com.example.exercise.models.database.image.ImageEntityDao
import com.facebook.imagepipeline.core.ImagePipeline

class FrescoUtils(
    private val imageEntityDao: ImageEntityDao,
    private val fresco: ImagePipeline
) {
    /**
     * Esta funcion busca las imagenes y se fija cuales estan en cache
     * Y arma los totales, esto sirve para mostrar los totales en la pantalla
     * principal.
     */
    suspend fun toExtendedData(value: DateValue): ExtendedDateValue {
        val date = value.date
        val data = ExtendedDateValue(date = date)
        imageEntityDao.findByDate(date).let { images ->
            data.count = images?.size ?: 0
            data.caches = images?.count { entity ->
                fresco.isInDiskCache(Uri.parse(entity.url)).isFinished
            } ?: 0
        }
        return data
    }
}

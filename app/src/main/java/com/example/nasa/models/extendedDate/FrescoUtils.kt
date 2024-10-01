package com.nmarsollier.nasa.models.extendedDate

import android.net.Uri
import com.facebook.imagepipeline.core.ImagePipeline
import com.nmarsollier.nasa.models.api.dates.DateValue
import com.nmarsollier.nasa.models.database.image.ImageEntityDao

class FrescoUtils(
    private val imageEntityDao: ImageEntityDao, private val fresco: ImagePipeline
) {
    /**
     * Esta funcion busca las imagenes y se fija cuales estan en cache
     * Y arma los totales, esto sirve para mostrar los totales en la pantalla
     * principal.
     */
    suspend fun toExtendedData(value: DateValue): ExtendedDateValue {
        val date = value.date
        var count = 0
        var caches = 0
        imageEntityDao.findByDate(date).let { images ->
            count = images?.size ?: 0
            caches = images?.count { entity ->
                fresco.isInDiskCache(Uri.parse(entity.url)).isFinished
            } ?: 0
        }
        return ExtendedDateValue(date = date, count = count, caches = caches)
    }
}


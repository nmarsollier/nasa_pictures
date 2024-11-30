package com.nmarsollier.nasa.models.extendedDate

import coil3.ImageLoader
import com.nmarsollier.nasa.models.api.dates.DateValue
import com.nmarsollier.nasa.models.database.image.ImageEntityDao

/**
 * Esta funcion busca las imagenes y se fija cuales estan en cache
 * Y arma los totales, esto sirve para mostrar los totales en la pantalla
 * principal.
 */
class DateToExtendedDate(
    private val imageEntityDao: ImageEntityDao,
    private val imageLoader: ImageLoader
) {
    suspend fun toExtendedData(value: DateValue): ExtendedDateValue {
        val date = value.date
        var count = 0
        var caches = 0
        imageEntityDao.findByDate(date).let { images ->
            count = images?.size ?: 0
            caches = images?.count { entity ->
                imageLoader.diskCache?.openSnapshot(entity.url)?.use { true } == true
            } ?: 0
        }
        return ExtendedDateValue(date = date, count = count, caches = caches)
    }
}
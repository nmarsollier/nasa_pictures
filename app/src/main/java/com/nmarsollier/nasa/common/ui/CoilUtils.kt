package com.nmarsollier.nasa.common.ui

import android.content.Context
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.nmarsollier.nasa.models.api.dates.DateValue
import com.nmarsollier.nasa.models.database.image.ImageEntityDao
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoilUtils(
    private val context: Context,
    private val imageEntityDao: ImageEntityDao, private val imageLoader: ImageLoader
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
                imageLoader.diskCache?.openSnapshot(entity.url)?.use { true } == true
            } ?: 0
        }
        return ExtendedDateValue(date = date, count = count, caches = caches)
    }

    suspend fun loadImage(url: String, size: Int? = null): Bitmap? {
        val requestBuilder = ImageRequest.Builder(context)
            .allowHardware(false)
            .data(url)

        if (size != null) {
            requestBuilder.size(size)
        }

        return withContext(Dispatchers.IO) {
            val result = imageLoader.execute(requestBuilder.build())
            if (result is SuccessResult) {
                result.image.toBitmap()
            } else {
                null
            }
        }
    }
}

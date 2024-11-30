package com.nmarsollier.nasa.common.utils

import android.content.Context
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoilUtils(
    private val context: Context,
    private val imageLoader: ImageLoader
) {
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

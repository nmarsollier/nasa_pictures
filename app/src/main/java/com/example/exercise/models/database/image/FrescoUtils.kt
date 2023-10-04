package com.example.exercise.models.database.image

import android.content.Context
import android.net.Uri
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.core.ImagePipelineConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FrescoUtils(
    private val context: Context,
    private val imageRepository: ImageRepository
) {
    val fresco: ImagePipeline by lazy {
        Fresco.getImagePipeline()
    }

    fun initFresco(context: Context) {
        Fresco.initialize(
            context,
            ImagePipelineConfig.newBuilder(context).setDownsampleEnabled(true)
                .setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(context)
                        .setMaxCacheSize(100L * ByteConstants.MB)
                        .build()
                ).setDiskCacheEnabled(true).build()
        )
    }

    suspend fun toDatesData(value: DateValue): ExtendedDateValue {
        val date = value.date
        return suspendCoroutine {
            MainScope().launch(Dispatchers.IO) {
                val data = ExtendedDateValue(date = date)
                imageRepository.findByDate(date).let { images ->
                    data.count = images?.size ?: 0
                    data.caches = images?.count { entity ->
                        fresco.isInDiskCache(Uri.parse(entity.url)).isFinished
                    } ?: 0
                }
                it.resume(data)
            }
        }
    }
}


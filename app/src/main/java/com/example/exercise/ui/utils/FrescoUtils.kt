package com.example.exercise.ui.utils

import android.content.Context
import android.net.Uri
import com.example.exercise.MainApplication
import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.image.ImageRepository
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.core.ImagePipelineConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

object FrescoUtils {
    val fresco: ImagePipeline by lazy {
        Fresco.getImagePipeline()
    }

    fun initFresco(context: Context) {
        Fresco.initialize(
            context,
            ImagePipelineConfig.newBuilder(MainApplication.context).setDownsampleEnabled(true)
                .setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(MainApplication.context)
                        .setMaxCacheSize(100L * ByteConstants.MB)
                        .build()
                ).setDiskCacheEnabled(true).build()
        )
    }

    suspend fun toDatesData(value: DateValue): ExtendedDateValue {
        val date = value.date
        val result = ExtendedDateValue(date = date)
        MainScope().launch(Dispatchers.IO) {
            ImageRepository.findByDate(date).take(1).collect { images ->
                result.count = images?.size ?: 0
                result.caches = images?.filter { entity ->
                    fresco.isInDiskCache(Uri.parse(entity.url)).isFinished
                }?.count() ?: 0
            }
        }.join()
        return result
    }
}

suspend fun DateValue.toDatesData(): ExtendedDateValue = FrescoUtils.toDatesData(this)
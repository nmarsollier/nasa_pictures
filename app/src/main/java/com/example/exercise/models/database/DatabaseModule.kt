package com.example.exercise.models.database

import com.example.exercise.models.database.config.getRoomDatabase
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.core.ImagePipelineConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinDatabaseModule = module {
    single {
        getRoomDatabase(androidContext())
    }

    single<ImagePipeline> {
        Fresco.initialize(
            androidContext(),
            ImagePipelineConfig.newBuilder(androidContext()).setDownsampleEnabled(true)
                .setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(androidContext())
                        .setMaxCacheSize(100L * ByteConstants.MB)
                        .build()
                ).setDiskCacheEnabled(true).build()
        )
        Fresco.getImagePipeline()
    }
}
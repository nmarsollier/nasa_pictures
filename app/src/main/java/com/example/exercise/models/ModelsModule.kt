package com.example.exercise.models

import com.example.exercise.models.api.dates.DatesApi
import com.example.exercise.models.api.images.ImagesApi
import com.example.exercise.models.api.tools.RetrofitClient
import com.example.exercise.models.database.config.LocalDatabase
import com.example.exercise.models.database.config.getRoomDatabase
import com.example.exercise.models.extendedDate.FrescoUtils
import com.example.exercise.models.useCases.FetchDatesUseCase
import com.example.exercise.models.useCases.FetchImagesUseCase
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.core.ImagePipelineConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val modelsModule = module {
    single {
        getRoomDatabase(androidContext())
    }

    single {
        val database: LocalDatabase = get()
        database.datesDao()
    }

    single {
        val database: LocalDatabase = get()
        database.imageDao()
    }

    singleOf(::FetchImagesUseCase)
    singleOf(::FetchDatesUseCase)

    single { RetrofitClient("https://epic.gsfc.nasa.gov/").retrofit }
    singleOf(::DatesApi)
    singleOf(::ImagesApi)

    // Fresco
    singleOf(::FrescoUtils)
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
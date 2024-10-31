package com.nmarsollier.nasa.models

import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.nmarsollier.nasa.models.api.dates.DatesApi
import com.nmarsollier.nasa.models.api.images.ImagesApi
import com.nmarsollier.nasa.models.database.config.LocalDatabase
import com.nmarsollier.nasa.models.database.config.getRoomDatabase
import com.nmarsollier.nasa.models.extendedDate.FrescoUtils
import com.nmarsollier.nasa.models.useCases.FetchDatesUseCase
import com.nmarsollier.nasa.models.useCases.FetchImagesUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named

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

    single(named("nasaBaseUrl")) { "https://epic.gsfc.nasa.gov/" }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    single { DatesApi(get(), get(named("nasaBaseUrl")))}
    single { ImagesApi(get(), get(named("nasaBaseUrl"))) }

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
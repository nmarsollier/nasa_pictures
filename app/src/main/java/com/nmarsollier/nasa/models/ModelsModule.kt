package com.nmarsollier.nasa.models

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import com.nmarsollier.nasa.models.api.dates.DatesApi
import com.nmarsollier.nasa.models.api.images.ImagesApi
import com.nmarsollier.nasa.models.database.config.LocalDatabase
import com.nmarsollier.nasa.models.database.config.getRoomDatabase
import com.nmarsollier.nasa.common.ui.CoilUtils
import com.nmarsollier.nasa.models.useCases.FetchDatesUseCase
import com.nmarsollier.nasa.models.useCases.FetchImagesUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
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

    single { DatesApi(get(), get(named("nasaBaseUrl"))) }
    single { ImagesApi(get(), get(named("nasaBaseUrl"))) }

    singleOf(::CoilUtils)

    single<ImageLoader> {
        ImageLoader.Builder(androidContext())
            .memoryCache {
                MemoryCache.Builder().maxSizePercent(androidContext(), 0.50).build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(androidContext().cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
            .build()
    }
}
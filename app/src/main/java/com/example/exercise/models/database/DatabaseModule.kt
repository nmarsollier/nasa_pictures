package com.example.exercise.models.database

import com.example.exercise.models.database.config.getRoomDatabase
import com.example.exercise.models.database.dates.DatesEntityDao
import com.example.exercise.models.database.image.FrescoUtils
import com.example.exercise.models.database.image.ImageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinDatabaseModule = module {
    single {
        getRoomDatabase(androidContext())
    }

    factoryOf(::ImageRepository)
    singleOf(::FrescoUtils)
}
package com.example.exercise.models.useCases

import com.example.exercise.models.database.config.LocalDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val koinUseCaseModule = module {
    single {
        val database: LocalDatabase = get()
        database.datesDao()
    }

    single {
        val database: LocalDatabase = get()
        database.imageDao()
    }

    factoryOf(::FetchImagesUseCase)
    factoryOf(::FetchDatesUseCase)
}
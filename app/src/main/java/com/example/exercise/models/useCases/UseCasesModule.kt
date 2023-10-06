package com.example.exercise.models.useCases

import com.example.exercise.models.database.config.ExampleDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val koinUseCaseModule = module {
    single {
        val database: ExampleDatabase = get()
        database.datesDao()
    }

    factoryOf(::FetchImagesUseCase)
    factoryOf(::FetchDatesUseCase)
}
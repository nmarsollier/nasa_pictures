package com.example.exercise.models.useCases

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val koinUseCaseModule = module {
    factoryOf(::FetchImagesUseCase)
    factoryOf(::FetchDatesUseCase)
}
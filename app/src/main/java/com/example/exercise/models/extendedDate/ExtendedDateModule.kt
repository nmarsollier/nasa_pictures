package com.example.exercise.models.extendedDate

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinExtendedDateModule = module {
    singleOf(::FrescoUtils)
}
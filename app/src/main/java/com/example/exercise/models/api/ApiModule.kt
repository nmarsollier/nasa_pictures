package com.example.exercise.models.api

import com.example.exercise.models.api.dates.DatesApiClient
import com.example.exercise.models.api.images.ImagesApiClient
import com.example.exercise.models.api.tools.RetrofitClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val koinApiModule = module {
    factoryOf(::DatesApiClient)
    factoryOf(::ImagesApiClient)
    single { RetrofitClient("https://epic.gsfc.nasa.gov/").retrofit }
}
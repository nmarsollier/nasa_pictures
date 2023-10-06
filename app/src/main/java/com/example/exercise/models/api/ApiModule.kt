package com.example.exercise.models.api

import com.example.exercise.models.api.dates.DatesApi
import com.example.exercise.models.api.images.ImagesApi
import com.example.exercise.models.api.tools.RetrofitClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val koinApiModule = module {
    factoryOf(::DatesApi)
    factoryOf(::ImagesApi)
    single { RetrofitClient("https://epic.gsfc.nasa.gov/").retrofit }
}
package com.example.exercise.tools

import com.example.exercise.models.api.dates.DatesApiClient
import com.example.exercise.models.api.images.ImagesApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
open class BaseTest {
    init {
        Dispatchers.setMain(Dispatchers.Default)
        Dispatchers.setMain(Dispatchers.Unconfined)

        mockFresco()
        mocksDataBase()
    }

    val datesApiMock = ApiMockMapClass(
        urlOverride = { DatesApiClient.urlBaseOverride = it }, maps = mapOf(
            "api/enhanced/all" to "dates.json"
        )
    )

    val imagesApiMock = ApiMockMapClass(
        urlOverride = { ImagesApiClient.urlBaseOverride = it }, maps = mapOf(
            "/api/enhanced/date/2023-08-22" to "images_2023-08-22.json",
            "/api/enhanced/date/2023-08-21" to "images_2023-08-21.json",
            "/api/enhanced/date/2023-08-20" to "images_2023-08-20.json",
            "/api/enhanced/date/2023-08-19" to "images_2023-08-19.json",
            "/api/enhanced/date/2023-08-18" to "images_2023-08-18.json"
        )
    )
}
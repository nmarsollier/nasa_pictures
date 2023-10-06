package com.example.exercise.tools

import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.ui.utils.jsonToObject

object ImageValueMock {
    val images2023_08_22 = rawResToString("images_2023-08-22.json").jsonToObject<List<ImageValue>>()
    val images2023_08_21 = rawResToString("images_2023-08-21.json").jsonToObject<List<ImageValue>>()
    val images2023_08_20 = rawResToString("images_2023-08-20.json").jsonToObject<List<ImageValue>>()
    val images2023_08_19 = rawResToString("images_2023-08-19.json").jsonToObject<List<ImageValue>>()
    val images2023_08_18 = rawResToString("images_2023-08-18.json").jsonToObject<List<ImageValue>>()

    fun rawResToString(r: String): String {
        return javaClass.getResourceAsStream("/$r")?.bufferedReader().use {
            it?.readText() ?: ""
        }
    }
}
package com.example.exercise.tools

import com.example.exercise.models.businessObjects.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.ui.utils.FrescoUtils
import io.mockk.coEvery
import io.mockk.mockkObject

fun mockFresco() {
    mockkObject(FrescoUtils)
    coEvery { FrescoUtils.toDatesData(any()) } answers {
        val data = firstArg<DateValue>()
        ExtendedDateValue(data.date, 0, 0)
    }
}
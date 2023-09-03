package com.example.exercise.tools

import com.example.exercise.models.businessObjects.ExtendedDateValue

class ExtendedDateValueMocks {
    companion object {
        val mock2023_08_22 = ExtendedDateValue(
            date = "2023-08-22",
            count = 0,
            caches = 0
        )

        val mock2023_08_21 = ExtendedDateValue(
            date = "2023-08-21",
            count = 0,
            caches = 0
        )
        val mock2023_08_20 = ExtendedDateValue(
            date = "2023-08-20",
            count = 0,
            caches = 0
        )
        val mock2023_08_19 = ExtendedDateValue(
            date = "2023-08-19",
            count = 0,
            caches = 0
        )

        val mock2023_08_18 = ExtendedDateValue(
            date = "2023-08-18",
            count = 0,
            caches = 0
        )

        val dates =
            listOf(mock2023_08_22, mock2023_08_21, mock2023_08_20, mock2023_08_19, mock2023_08_18)
    }
}

package com.example.exercise.models.businessObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
private val dateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Parcelize
data class ExtendedDateValue(
    var date: String,
    var count: Int = 0,
    var caches: Int = 0
) : Parcelable {

    val parsedDate: LocalDate
        get() = LocalDate.parse(date, dateParser)

    val formattedDayString: String
        get() = dayFormatter.format(parsedDate)

    val formattedDateString: String
        get() = dateFormatter.format(parsedDate)

    val needsLoad: Boolean
        get() = count == 0

    val isLoading: Boolean
        get() = count == 0 || count != caches

    object Samples {
        val partialLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 3, 2)

        val fullyLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 2, 2)

        val unloadedLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 0, 0)

        val combinedListExtendedDateValueSample
            get() = listOf(
                ExtendedDateValue("2023-08-14", 16, 0),
                ExtendedDateValue("2023-08-13", 0, 0),
                ExtendedDateValue("2023-08-12", 5, 5),
                ExtendedDateValue("2023-08-11", 5, 3)
            )
    }
}

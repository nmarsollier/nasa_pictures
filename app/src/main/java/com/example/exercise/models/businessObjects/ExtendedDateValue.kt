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
    val date: String,
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

    companion object
}

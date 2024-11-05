package com.nmarsollier.nasa.models.extendedDate

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class ExtendedDateValue(
    val date: String, val count: Int = 0, val caches: Int = 0
) {

    private val parsedDate: LocalDate
        get() = date.toLocalDate()

    val formattedDayString: String
        get() = parsedDate.toJavaLocalDate().dayOfWeek.toString()

    val formattedDateString: String
        get() = "${parsedDate.dayOfMonth}/${parsedDate.monthNumber}/${parsedDate.year}"

    val needsLoad: Boolean
        get() = count == 0

    val isLoading: Boolean
        get() = count == 0 || count != caches

    companion object
}
package com.nmarsollier.nasa.models.api.dates

import com.nmarsollier.nasa.models.database.dates.DatesEntity
import com.nmarsollier.nasa.models.extendedDate.DateToExtendedDate
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateValue(
    @SerialName("date") val date: String,
) {
    val parsedDate: LocalDate
        get() = date.toLocalDate()
}

suspend fun DateValue.asExtendedDateValue(dateToExtendedDate: DateToExtendedDate): ExtendedDateValue {
    return dateToExtendedDate.toExtendedData(this)
}

suspend fun ExtendedDateValue.refresh(dateToExtendedDate: DateToExtendedDate): ExtendedDateValue {
    return this.date.asDateValue.asExtendedDateValue(dateToExtendedDate)
}

val String.asDateValue
    get() = DateValue(this)

val DateValue.asDateEntity
    get() = DatesEntity(this.date)
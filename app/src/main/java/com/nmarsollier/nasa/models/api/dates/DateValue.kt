package com.nmarsollier.nasa.models.api.dates

import android.os.Parcelable
import com.nmarsollier.nasa.models.database.dates.DatesEntity
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.models.extendedDate.FrescoUtils
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Serializable
@Parcelize
data class DateValue(
    @SerialName("date") val date: String,
) : Parcelable {
    val parsedDate: LocalDate
        get() = LocalDate.parse(date, dateParser)

}

suspend fun DateValue.asExtendedDateValue(frescoUtils: FrescoUtils): ExtendedDateValue {
    return frescoUtils.toExtendedData(this)
}

suspend fun ExtendedDateValue.refresh(frescoUtils: FrescoUtils): ExtendedDateValue {
    return this.date.asDateValue.asExtendedDateValue(frescoUtils)
}

val String.asDateValue
    get() = DateValue(this)

val DateValue.asDateEntity
    get() = DatesEntity(this.date)

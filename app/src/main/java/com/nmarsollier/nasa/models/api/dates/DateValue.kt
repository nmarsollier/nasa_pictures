package com.nmarsollier.nasa.models.api.dates

import android.os.Parcelable
import com.nmarsollier.nasa.models.database.dates.DatesEntity
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue
import com.nmarsollier.nasa.models.extendedDate.FrescoUtils
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DateValue(
    @SerialName("date") val date: String,
) : Parcelable {
    val parsedDate: LocalDate
        get() = date.toLocalDate()
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
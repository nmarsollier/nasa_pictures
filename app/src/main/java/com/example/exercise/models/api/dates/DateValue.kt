package com.example.exercise.models.api.dates

import android.os.Parcelable
import com.example.exercise.models.database.dates.DatesEntity
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Parcelize
data class DateValue(
    @SerializedName("date") val date: String,
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

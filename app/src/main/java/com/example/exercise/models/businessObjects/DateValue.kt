package com.example.exercise.models.businessObjects

import android.os.Parcelable
import com.example.exercise.models.database.image.FrescoUtils
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateValue(
    @SerializedName("date") val date: String,
) : Parcelable

suspend fun DateValue.asExtendedDateValue(frescoUtils: FrescoUtils): ExtendedDateValue {
    return frescoUtils.toDatesData(this)
}

val String.asDateValue
    get() = DateValue(this)

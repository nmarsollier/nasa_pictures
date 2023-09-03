package com.example.exercise.models.businessObjects

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateValue(
    @SerializedName("date") var date: String,
) : Parcelable

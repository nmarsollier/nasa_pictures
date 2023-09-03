package com.example.exercise.models.database.dates

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dates")
data class DatesEntity(
    @PrimaryKey val date: String
) : Parcelable
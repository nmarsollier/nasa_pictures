package com.nmarsollier.nasa.models.database.dates

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dates")
data class DatesEntity(
    @PrimaryKey val date: String
)
package com.nmarsollier.nasa.common.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

// "yyyy-MM-dd HH:mm:ss"
val String?.toLocalDateTime: LocalDateTime?
    get() = this?.let {
        return try {
            val parts = this.split(" ")

            val dateParts = parts[0].split("-")
            val timeParts = parts[1].split(":")

            return LocalDateTime(
                year = dateParts[0].toInt(),
                month = Month.entries[dateParts[1].toInt() - 1],
                dayOfMonth = dateParts[2].toInt(),
                hour = timeParts[0].toInt(),
                minute = timeParts[1].toInt(),
                second = timeParts[2].toInt(),
            )
        } catch (e: Exception) {
            null
        }
    }

val LocalDateTime.toDateTimeString: String
    get() = "${this.year}-${
        this.monthNumber.toString().padStart(2, '0')
    }-${this.dayOfMonth.toString().padStart(2, '0')}"

val LocalDateTime.toHourMinuteString: String
    get() = "${this.hour.toString().padStart(2, '0')}:${this.minute.toString().padStart(2, '0')}"

val LocalDateTime.toMonthString: String
    get() = this.monthNumber.toString().padStart(2, '0')

val LocalDateTime.toDayString: String
    get() = this.dayOfMonth.toString().padStart(2, '0')

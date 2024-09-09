package com.example.exercise.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dayFormatter = DateTimeFormatter.ofPattern("dd")
private val monthFormatter = DateTimeFormatter.ofPattern("MM")
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
private val hourMinuteFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

val String?.toLocalDateTime: LocalDateTime
    get() = LocalDateTime.parse(this, dateTimeFormatter)

val LocalDateTime.toDateTimeString: String
    get() = dateFormatter.format(this)

val LocalDateTime.toHourMinuteString: String
    get() = hourMinuteFormatter.format(this)

val LocalDateTime.toMonthString: String
    get() = monthFormatter.format(this)

val LocalDateTime.toDayString: String
    get() = dayFormatter.format(this)

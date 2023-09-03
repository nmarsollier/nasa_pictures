package com.example.exercise.models.businessObjects

import com.example.exercise.models.database.image.ImageEntity
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val dayFormatter = DateTimeFormatter.ofPattern("dd")
private val monthFormatter = DateTimeFormatter.ofPattern("MM")
private val dateTimeParser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
private val dateHourFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

data class CoordinatesValue(
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
)

data class ImageValue(
    @SerializedName("identifier") var identifier: String,
    @SerializedName("date") var date: String,
    @SerializedName("image") var image: String,
    @SerializedName("centroid_coordinates") var coordinates: CoordinatesValue,
) {
    val downloadUrl: String
        get() {
            val currentDate = LocalDateTime.parse(date, dateTimeParser)
            return "https://epic.gsfc.nasa.gov/archive/enhanced/${currentDate.year}/${
                monthFormatter.format(
                    currentDate
                )
            }/${dayFormatter.format(currentDate)}/png/${image}.png"
        }

    val formattedDateTime: LocalDateTime
        get() = LocalDateTime.parse(date, dateTimeParser)

    val formattedDateOnly: String
        get() {
            return dateFormatter.format(formattedDateTime)
        }

    val formattedDayHour: String
        get() = dateHourFormatter.format(formattedDateTime)


    fun toImageEntity(): ImageEntity {
        return ImageEntity(
            identifier = this.identifier,
            date = this.date,
            day = this.formattedDateOnly,
            image = this.image,
            url = this.downloadUrl,
            lat = this.coordinates.lat,
            lon = this.coordinates.lon
        )
    }

    object Samples {
        val simpleImageValeSample: ImageValue
            get() = ImageValue(
                identifier = "20180601042059",
                date = "2018-06-01 03:10:43",
                image = "epic_RGB_20180601042059",
                coordinates = CoordinatesValue(12.51231, 25.2123),
            )
    }
}


package com.example.exercise.models.api.images

import com.example.exercise.models.database.image.ImageEntity
import com.example.exercise.ui.utils.toDateTimeString
import com.example.exercise.ui.utils.toDayString
import com.example.exercise.ui.utils.toHourMinuteString
import com.example.exercise.ui.utils.toLocalDateTime
import com.example.exercise.ui.utils.toMonthString
import com.google.gson.annotations.SerializedName

data class CoordinatesValue(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
)

data class ImageValue(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("date") val date: String,
    @SerializedName("image") val image: String,
    @SerializedName("centroid_coordinates") val coordinates: CoordinatesValue,
) {
    val downloadUrl: String
        get() {
            val currentDate = date.toLocalDateTime
            return "https://epic.gsfc.nasa.gov/archive/enhanced/${currentDate.year}/" +
                    "${currentDate.toMonthString}/" +
                    "${currentDate.toDayString}/png/${image}.png"
        }

    val formattedDateOnly: String
        get() = date.toLocalDateTime.toDateTimeString

    val formattedHourMinute: String
        get() = date.toLocalDateTime.toHourMinuteString

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

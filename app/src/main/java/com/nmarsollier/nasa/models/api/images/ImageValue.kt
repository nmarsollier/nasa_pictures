package com.nmarsollier.nasa.models.api.images

import androidx.compose.runtime.Stable
import com.nmarsollier.nasa.common.utils.toDateTimeString
import com.nmarsollier.nasa.common.utils.toDayString
import com.nmarsollier.nasa.common.utils.toHourMinuteString
import com.nmarsollier.nasa.common.utils.toLocalDateTime
import com.nmarsollier.nasa.common.utils.toMonthString
import com.nmarsollier.nasa.models.database.image.ImageEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CoordinatesValue(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double,
)

@Stable
@Serializable
data class ImageValue(
    @SerialName("identifier") val identifier: String,
    @SerialName("date") val date: String,
    @SerialName("image") val image: String,
    @SerialName("centroid_coordinates") val coordinates: CoordinatesValue,
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

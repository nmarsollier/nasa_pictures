package com.nmarsollier.nasa.models.api.images

interface ImageSamples {
    val simpleImageValeSample: ImageValue
}

val ImageValue.Companion.Samples
    get() = object : ImageSamples {
        override val simpleImageValeSample: ImageValue
            get() = ImageValue(
                identifier = "20180601042059",
                date = "2018-06-01 03:10:43",
                image = "epic_RGB_20180601042059",
                coordinates = CoordinatesValue(12.51231, 25.2123),
            )
    }

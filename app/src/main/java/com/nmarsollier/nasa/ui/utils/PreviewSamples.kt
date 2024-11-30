package com.nmarsollier.nasa.ui.utils

import com.nmarsollier.nasa.models.api.images.CoordinatesValue
import com.nmarsollier.nasa.models.api.images.ImageValue
import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue

object ImageValueSamples {
    val simpleImageValeSample = ImageValue(
        identifier = "20180601042059",
        date = "2018-06-01 03:10:43",
        image = "epic_RGB_20180601042059",
        coordinates = CoordinatesValue(12.51231, 25.2123),
    )
}

object ExtendedDateValueSamples {
    val partialLoadedExtendedDateValueSample = ExtendedDateValue("2023-08-14", 3, 2)
    val fullyLoadedExtendedDateValueSample = ExtendedDateValue("2023-08-14", 2, 2)
    val unloadedLoadedExtendedDateValueSample = ExtendedDateValue("2023-08-14", 0, 0)
}


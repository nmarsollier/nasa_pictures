package com.nmarsollier.nasa.common.utils

import com.nmarsollier.nasa.models.extendedDate.ExtendedDateValue

interface ExtendedDateValueSamples {
    val partialLoadedExtendedDateValueSample: ExtendedDateValue
    val fullyLoadedExtendedDateValueSample: ExtendedDateValue
    val unloadedLoadedExtendedDateValueSample: ExtendedDateValue
    val combinedListExtendedDateValueSample: List<ExtendedDateValue>
}

val ExtendedDateValue.Companion.Samples
    get() = object : ExtendedDateValueSamples {
        override val partialLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 3, 2)

        override val fullyLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 2, 2)

        override val unloadedLoadedExtendedDateValueSample
            get() = ExtendedDateValue("2023-08-14", 0, 0)

        override val combinedListExtendedDateValueSample
            get() = listOf(
                ExtendedDateValue("2023-08-14", 16, 0),
                ExtendedDateValue("2023-08-13", 0, 0),
                ExtendedDateValue("2023-08-12", 5, 5),
                ExtendedDateValue("2023-08-11", 5, 3)
            )
    }


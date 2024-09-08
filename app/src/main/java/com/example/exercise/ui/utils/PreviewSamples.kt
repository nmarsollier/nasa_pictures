package com.example.exercise.ui.utils

import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.home.Destination
import com.example.exercise.ui.home.MainReducer
import com.example.exercise.ui.imagePreview.ImagesPreviewReducer
import com.example.exercise.ui.images.ImagesDateReducer
import com.example.exercise.ui.images.ImagesReducer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

interface MainReducerSamples {
    val empty: MainReducer
}

val MainReducer.Companion.Samples
    get() = object : MainReducerSamples {
        override val empty = object : MainReducer {
            override fun syncDates() = MainScope().launch {
            }

            override fun redirect(destination: Destination) = Unit
        }
    }

interface ImagesPreviewReducerSamples {
    val empty: ImagesPreviewReducer
}

val ImagesPreviewReducer.Companion.Samples
    get() = object : ImagesPreviewReducerSamples {
        override val empty = object : ImagesPreviewReducer {
            override fun init(imageValue: ImageValue) = Unit

            override fun toggleDetails() = Unit
        }
    }

interface ImagesReducerSamples {
    val empty: ImagesReducer
}

val ImagesReducer.Companion.Samples
    get() = object : ImagesReducerSamples {
        override val empty = object : ImagesReducer {
            override fun redirect(destination: com.example.exercise.ui.images.Destination) = Unit

            override fun fetchImages(date: ExtendedDateValue?) = MainScope().launch {
            }
        }
    }

interface ImagesDateReducerSamples {
    val empty: ImagesDateReducer
}

val ImagesDateReducer.Companion.Samples
    get() = object : ImagesDateReducerSamples {
        override val empty = object : ImagesDateReducer {
            override fun updateDate(date: ExtendedDateValue?) = MainScope().launch {
            }

            override fun updateDate() = MainScope().launch {
            }
        }
    }



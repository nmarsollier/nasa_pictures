package com.example.exercise.tools

import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import com.example.exercise.models.api.dates.DateValue
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.models.extendedDate.FrescoUtils
import com.facebook.imagepipeline.request.ImageRequestBuilder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic

fun mockFresco(): FrescoUtils {
    mockkConstructor(AnimationDrawable::class)
    every { anyConstructed<AnimationDrawable>().isOneShot = any() } returns Unit

    mockkStatic(ImageRequestBuilder::class)
    every { ImageRequestBuilder.newBuilderWithSource(any()) } returns mockk(relaxed = true) {
        every { build() } returns mockk(relaxed = true)
    }

    mockkStatic(Uri::class)
    every { Uri.parse(any()) } returns mockk(relaxed = true)

    return mockk(relaxed = true) {
        coEvery { toExtendedData(any()) } answers {
            val data = firstArg<DateValue>()
            ExtendedDateValue(data.date, 0, 0)
        }
    }
}
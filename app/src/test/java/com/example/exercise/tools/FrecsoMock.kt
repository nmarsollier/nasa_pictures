package com.example.exercise.tools

import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import com.example.exercise.models.api.dates.DateValue
import com.example.exercise.models.businessObjects.ExtendedDateValue
import com.example.exercise.models.database.image.FrescoUtils
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import java.util.concurrent.Executor

fun mockFresco() {
    mockkObject(FrescoUtils)

    coEvery { FrescoUtils.toDatesData(any()) } answers {
        val data = firstArg<DateValue>()
        ExtendedDateValue(data.date, 0, 0)
    }

    every { FrescoUtils.fresco } returns mockk(relaxed = true) {
        every { fetchDecodedImage(any(), any()) } answers {
            object : DataSource<CloseableReference<CloseableImage>> {
                override fun isClosed(): Boolean = true

                override fun getResult(): CloseableReference<CloseableImage>? = null

                override fun hasResult(): Boolean = true

                override fun getExtras(): MutableMap<String, Any>? = null

                override fun hasMultipleResults(): Boolean = false

                override fun isFinished(): Boolean = true

                override fun hasFailed(): Boolean = false

                override fun getFailureCause(): Throwable? = null

                override fun getProgress(): Float = 100f

                override fun close(): Boolean = true

                override fun subscribe(
                    dataSubscriber: DataSubscriber<CloseableReference<CloseableImage>>?,
                    executor: Executor?
                ) {
                    dataSubscriber?.onFailure(mockk(relaxed = true))
                }
            }
        }
    }

    mockkConstructor(AnimationDrawable::class)
    every { anyConstructed<AnimationDrawable>().isOneShot = any() } returns Unit

    mockkStatic(ImageRequestBuilder::class)
    every { ImageRequestBuilder.newBuilderWithSource(any()) } returns mockk(relaxed = true) {
        every { build() } returns mockk(relaxed = true)
    }

    mockkStatic(Uri::class)
    every { Uri.parse(any()) } returns mockk(relaxed = true)
}
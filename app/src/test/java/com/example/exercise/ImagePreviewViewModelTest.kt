package com.example.exercise

import app.cash.turbine.test
import com.example.exercise.models.api.images.ImageValue
import com.example.exercise.tools.BaseTest
import com.example.exercise.ui.imagePreview.ImagePreviewState
import com.example.exercise.ui.imagePreview.ImagePreviewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ImagePreviewViewModelTest : BaseTest() {

    @Test
    fun testStateChanges() = runBlocking {

        val model = ImagePreviewViewModel()

        model.state.test {
            model.init(ImageValue.Samples.simpleImageValeSample)
            model.toggleDetails()
            model.toggleDetails()
            model.toggleDetails()

            // Initial state is loading
            Assert.assertEquals(ImagePreviewState.Loading, awaitItem())

            // Asset initial data captured properly
            Assert.assertEquals(
                ImagePreviewState.Ready(ImageValue.Samples.simpleImageValeSample, false),
                awaitItem()
            )

            // Test toggle details
            Assert.assertEquals(
                ImagePreviewState.Ready(
                    ImageValue.Samples.simpleImageValeSample, true
                ), awaitItem()
            )
            Assert.assertEquals(
                ImagePreviewState.Ready(
                    ImageValue.Samples.simpleImageValeSample, false
                ), awaitItem()
            )
            Assert.assertEquals(
                ImagePreviewState.Ready(
                    ImageValue.Samples.simpleImageValeSample, true
                ), awaitItem()
            )
        }
    }
}

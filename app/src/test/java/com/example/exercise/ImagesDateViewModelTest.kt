package com.example.exercise

import app.cash.turbine.test
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.tools.BaseTest
import com.example.exercise.ui.images.ImagesDateState
import com.example.exercise.ui.images.ImagesDateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ImagesDateViewModelTest : BaseTest() {
    @Test
    fun testStateChanges() = runBlocking {
        val model = ImagesDateViewModel()

        model.state.test {
            model.updateDate(date = ExtendedDateValue.Samples.partialLoadedExtendedDateValueSample)
            Thread.sleep(3000)
            model.updateDate()

            // Initial state is loading
            Assert.assertEquals(ImagesDateState.Loading, awaitItem())

            // Date date stored properly initially
            Assert.assertEquals(
                ImagesDateState.Ready(
                    ExtendedDateValue.Samples.partialLoadedExtendedDateValueSample
                ),
                awaitItem()
            )

            // Date updated properly
            Assert.assertEquals(
                ImagesDateState.Ready(
                    ExtendedDateValue.Samples.unloadedLoadedExtendedDateValueSample
                ),
                awaitItem()
            )
        }
    }
}

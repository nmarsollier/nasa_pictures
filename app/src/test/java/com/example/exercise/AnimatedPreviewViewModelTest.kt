package com.example.exercise

import app.cash.turbine.test
import com.example.exercise.models.api.images.ImagesApi
import com.example.exercise.tools.BaseTest
import com.example.exercise.tools.ExtendedDateValueMocks
import com.example.exercise.tools.mockForUnitTest
import com.example.exercise.ui.animatedPreiew.AnimatedPreviewState
import com.example.exercise.ui.animatedPreiew.AnimatedPreviewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AnimatedPreviewViewModelTest : BaseTest() {
    @Test
    fun testStateChanges() = runBlocking {

        val model = AnimatedPreviewViewModel()

        imagesApiMock.mockForUnitTest {
            ImagesApi.urlBaseOverride != null
        }

        model.state.test {
            model.fetchImages(ExtendedDateValueMocks.mock2023_08_22)

            // Initial loading state tested
            Assert.assertEquals(AnimatedPreviewState.Loading, awaitItem())

            Assert.assertThat(
                awaitItem(),
                IsInstanceOf.instanceOf(AnimatedPreviewState.Ready::class.java)
            )
        }
    }
}

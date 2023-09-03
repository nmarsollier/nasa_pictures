package com.example.exercise

import app.cash.turbine.test
import com.example.exercise.models.api.images.ImagesApiClient
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.tools.BaseTest
import com.example.exercise.tools.ExtendedDateValueMocks
import com.example.exercise.tools.ImageValueMock
import com.example.exercise.tools.mockForUnitTest
import com.example.exercise.ui.images.ImagesState
import com.example.exercise.ui.images.ImagesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ImagesViewModelTest : BaseTest() {

    @Test
    fun testStateChanges() = runBlocking {
        val model = ImagesViewModel()

        imagesApiMock.mockForUnitTest {
            ImagesApiClient.urlBaseOverride != null
        }

        model.state.test {
            model.fetchImages(ExtendedDateValueMocks.mock2023_08_22)
            Thread.sleep(3000)
            model.fetchImages(ExtendedDateValueMocks.mock2023_08_22)

            // Initial loading state tested
            Assert.assertEquals(ImagesState.Loading, awaitItem())

            // Loading from network the first time
            Assert.assertEquals(
                ImagesState.Ready(
                    images = ImageValueMock.images2023_08_22!!,
                    CacheStrategy.NETWORK
                ),
                awaitItem()
            )

            // Loading from database the second time
            Assert.assertEquals(ImagesState.Loading, awaitItem())
            Assert.assertEquals(
                ImagesState.Ready(
                    images = ImageValueMock.images2023_08_22!!,
                    CacheStrategy.DB
                ),
                awaitItem()
            )
        }
    }
}

package com.example.exercise

import app.cash.turbine.test
import com.example.exercise.models.api.dates.DatesApi
import com.example.exercise.models.api.images.ImagesApi
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.tools.BaseTest
import com.example.exercise.tools.ExtendedDateValueMocks
import com.example.exercise.tools.mockForUnitTest
import com.example.exercise.ui.home.MainState
import com.example.exercise.ui.home.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseTest() {
    @Test
    fun testStateChanges() = runBlocking {
        val model = MainViewModel()

        imagesApiMock.mockForUnitTest {
            ImagesApi.urlBaseOverride != null
        }
        datesApiMock.mockForUnitTest {
            DatesApi.urlBaseOverride != null
        }

        model.state.test {
            model.fetchDates()
            Thread.sleep(3000)
            model.fetchDates()

            // Initial loading state tested
            Assert.assertEquals(MainState.Loading, awaitItem())

            // Loading from network the first time
            Assert.assertEquals(
                MainState.Ready(
                    dates = ExtendedDateValueMocks.dates!!,
                    CacheStrategy.NETWORK
                ),
                awaitItem()
            )

            // Loading from database the second time
            Assert.assertEquals(MainState.Loading, awaitItem())
            Assert.assertEquals(
                MainState.Ready(
                    dates = ExtendedDateValueMocks.dates!!,
                    CacheStrategy.DB
                ),
                awaitItem()
            )
        }
    }
}

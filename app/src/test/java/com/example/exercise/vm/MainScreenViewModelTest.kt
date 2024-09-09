package com.example.exercise.vm

import app.cash.turbine.test
import com.example.exercise.tools.BaseUnitTest
import com.example.exercise.ui.home.MainAction
import com.example.exercise.ui.home.MainState
import com.example.exercise.ui.home.MainViewModel
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.koin.test.get

@ExperimentalCoroutinesApi
class MainScreenViewModelTest : BaseUnitTest() {
    @Test
    fun testStateChanges() = runBlocking {
        val model = MainViewModel(get(), get(), get())

        testHttpServer.setAnswer(
            "/api/enhanced/all",
            "dates.json",
        )

        model.state.test {
            Assert.assertEquals(MainState.Loading, awaitItem())

            model.reduce(MainAction.SyncDates)

            Assert.assertTrue(
                awaitItem() is MainState.Ready
            )

            testHttpServer.assertRequestCount(1)
            coVerify(exactly = 2) { datesDao.findLast() }
            coVerify(exactly = 5) { datesDao.insert(any()) }

            model.reduce(MainAction.SyncDates)
            Assert.assertTrue(
                awaitItem() is MainState.Ready
            )
            testHttpServer.assertRequestCount(1)
            coVerify(exactly = 4) { datesDao.findLast() }
            cancelAndConsumeRemainingEvents()
        }
    }
}
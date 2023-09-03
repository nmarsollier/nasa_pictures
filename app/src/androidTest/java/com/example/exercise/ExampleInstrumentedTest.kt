package com.example.exercise

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.viewModelScope
import com.example.exercise.models.api.tools.CacheStrategy
import com.example.exercise.tools.ExtendedDateValueMocks
import com.example.exercise.tools.assertTextIsDisplayed
import com.example.exercise.tools.mockFresco
import com.example.exercise.ui.home.MainActivity
import com.example.exercise.ui.home.MainState
import com.example.exercise.ui.home.MainViewModel
import com.example.exercise.ui.utils.cleanMockedViewModels
import com.example.exercise.ui.utils.mockViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
    val viewModel = mockk<MainViewModel>(relaxed = true).apply {
        every { viewModelScope } returns (CoroutineScope(SupervisorJob() + Dispatchers.Default))

        every { state } returns MutableStateFlow<MainState>(
            MainState.Ready(
                ExtendedDateValueMocks.dates, cacheStrategy = CacheStrategy.NETWORK
            )
        )
    }

    init {
        mockFresco()
        mockViewModel(viewModel)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testHomeFragment() {
        composeTestRule.assertTextIsDisplayed("Tuesday")
        composeTestRule.assertTextIsDisplayed("22/08/2023")
        composeTestRule.assertTextIsDisplayed("Start downloading")

        composeTestRule.assertTextIsDisplayed("Monday")
        composeTestRule.assertTextIsDisplayed("21/08/2023")
        composeTestRule.assertTextIsDisplayed("0/10")

        composeTestRule.assertTextIsDisplayed("Sunday")
        composeTestRule.assertTextIsDisplayed("20/08/2023")
        composeTestRule.assertTextIsDisplayed("5/10")

        composeTestRule.assertTextIsDisplayed("Saturday")
        composeTestRule.assertTextIsDisplayed("19/08/2023")
        composeTestRule.assertTextIsDisplayed("10/10")

        composeTestRule.assertTextIsDisplayed("Friday")
        composeTestRule.assertTextIsDisplayed("18/08/2023")
        composeTestRule.assertTextIsDisplayed("Start downloading")

        verify { viewModel.fetchDates() }

        cleanMockedViewModels()
    }

}

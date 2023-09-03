package com.example.exercise.tools

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst

fun AndroidComposeTestRule<*, *>.assertTextIsDisplayed(text: String) {
    onAllNodesWithText(text).onFirst().assertIsDisplayed()
}

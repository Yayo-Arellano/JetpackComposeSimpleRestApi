package com.nopalsoft.simple.rest

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addingItemsWorksCorrectly() {
        composeTestRule.setContent {
            MyApp1()
        }

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 1 LastName 1").assertExists()
    }

    @Test
    fun removingItemsWorkCorrectly() {
        composeTestRule.setContent {
            MyApp1()
        }
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Remove").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertDoesNotExist()
    }

    @Test
    fun loadingIsVisibleWhenAddingNewItem() {
        composeTestRule.setContent {
            MyApp(users = emptyList(), isLoading = true)
        }

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithTag("loadingCard").assertExists()
    }
}

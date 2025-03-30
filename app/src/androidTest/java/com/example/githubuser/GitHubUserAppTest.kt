package com.example.githubuser

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.githubuser.ui.navigation.Screen
import com.example.githubuser.ui.theme.GithubUserTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GitHubUserAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            GithubUserTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                GitHubUserApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_openProfile_navigatesToProfile() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Profile").performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()
    }

    @Test
    fun navHost_clickItem_NavigatesBack() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Profile").performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.menu_star))
            .performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Star.route)
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.menu_home))
            .performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Home.route)
    }
}
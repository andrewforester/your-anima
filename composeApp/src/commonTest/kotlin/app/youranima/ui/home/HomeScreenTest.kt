package app.youranima.ui.home

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.MockHomeRepository
import app.youranima.data.home.MoodCategory
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class HomeScreenTest {
    private val data = MockHomeRepository.homeData()

    private fun ComposeUiTest.showHome() =
        setContent {
            AppTheme { HomeScreen() }
        }

    @Test
    fun showsProfileAndReadingCards() =
        runComposeUiTest {
            showHome()

            onNodeWithTag(HomeScreenTags.USER_NAME).assertIsDisplayed()
            onNodeWithText(data.user.name).assertIsDisplayed()
            onNodeWithText("Sagittarius").assertIsDisplayed()
            onNodeWithTag(HomeScreenTags.BIRTH_CHART).assertIsDisplayed()
            data.readings.forEach { offer ->
                onNodeWithTag(HomeScreenTags.readingCard(offer.id)).performScrollTo().assertIsDisplayed()
                onNodeWithTag(HomeScreenTags.askButton(offer.id)).performScrollTo().assertIsDisplayed()
            }
            onNodeWithText("a free personal reading", substring = true).assertExists()
            onNodeWithText("a paid personal reading", substring = true).assertExists()
        }

    @Test
    fun tabsSwitchSelection() =
        runComposeUiTest {
            showHome()

            ForecastPeriod.entries.forEach { period ->
                onNodeWithTag(HomeScreenTags.tab(period)).performScrollTo().assertIsDisplayed()
            }
            onNodeWithTag(HomeScreenTags.tab(ForecastPeriod.Today)).assertIsSelected()

            onNodeWithTag(HomeScreenTags.tab(ForecastPeriod.Week)).performClick()

            onNodeWithTag(HomeScreenTags.tab(ForecastPeriod.Week)).assertIsSelected()
            onNodeWithTag(HomeScreenTags.tab(ForecastPeriod.Today)).assertIsNotSelected()
        }

    @Test
    fun showsFourMoodRings() =
        runComposeUiTest {
            showHome()

            onNodeWithTag(HomeScreenTags.FOCUS_MOOD).performScrollTo().assertIsDisplayed()
            MoodCategory.entries.forEach { category ->
                onNodeWithTag(HomeScreenTags.moodRing(category)).assertIsDisplayed()
            }
            onNodeWithText("50%").assertIsDisplayed()
            onNodeWithText("Career").assertIsDisplayed()
        }

    @Test
    fun showsFiveNavItemsAndSwitchesSelection() =
        runComposeUiTest {
            showHome()

            HomeNavItem.entries.forEach { item ->
                onNodeWithTag(HomeScreenTags.navItem(item)).assertIsDisplayed()
            }
            onNodeWithTag(HomeScreenTags.navItem(HomeNavItem.Today)).assertIsSelected()
            onNodeWithText("FREE").assertIsDisplayed()
            onNodeWithText("3").assertIsDisplayed()

            onNodeWithTag(HomeScreenTags.navItem(HomeNavItem.Readings)).performClick()

            onNodeWithTag(HomeScreenTags.navItem(HomeNavItem.Readings)).assertIsSelected()
            onNodeWithTag(HomeScreenTags.navItem(HomeNavItem.Today)).assertIsNotSelected()
        }
}

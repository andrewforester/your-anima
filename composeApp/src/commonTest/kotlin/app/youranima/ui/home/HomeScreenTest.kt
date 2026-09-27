package app.youranima.ui.home

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.MockHomeRepository
import app.youranima.data.home.MoodCategory
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

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

            // Semantics click: in the small test window the tab can sit under the bottom bar.
            onNodeWithTag(HomeScreenTags.tab(ForecastPeriod.Week)).performSemanticsAction(SemanticsActions.OnClick)

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
            onNode(hasText("Career") and hasAnyAncestor(hasTestTag(HomeScreenTags.FOCUS_MOOD))).assertIsDisplayed()
        }

    @Test
    fun showsFeedBlocksBelowFocusMood() =
        runComposeUiTest {
            showHome()

            onNodeWithTag(HomeScreenTags.CATEGORIES).performScrollTo().assertIsDisplayed()
            data.categories.forEach { forecast ->
                onNodeWithTag(HomeScreenTags.CATEGORIES)
                    .performScrollToNode(hasTestTag(HomeScreenTags.categoryCard(forecast.id)))
                onNodeWithTag(HomeScreenTags.categoryCard(forecast.id)).assertIsDisplayed()
                onNodeWithTag(HomeScreenTags.lockBadge(forecast.id), true).assertIsDisplayed()
            }

            onNodeWithTag(HomeScreenTags.TIP).performScrollTo().assertIsDisplayed()
            onNodeWithText("Tip for the day").assertIsDisplayed()
            onNodeWithText(data.tipOfTheDay).assertIsDisplayed()

            onNodeWithTag(HomeScreenTags.YES_NO).performScrollTo().assertIsDisplayed()
            onNodeWithTag(HomeScreenTags.YES, true).assertIsDisplayed()
            onNodeWithText("Yes for today").assertIsDisplayed()
            data.yesForToday.forEach { onNodeWithText(it).assertIsDisplayed() }

            onNodeWithTag(HomeScreenTags.NO).performScrollTo().assertIsDisplayed()
            onNodeWithText("No for today").assertIsDisplayed()
            data.noForToday.forEach { onNodeWithText(it).assertIsDisplayed() }

            onNodeWithTag(HomeScreenTags.TAROT).performScrollTo().assertIsDisplayed()
            onNodeWithText("Tarot Insight").assertIsDisplayed()
        }

    @Test
    fun categoryAndTarotClicksAreHoisted() =
        runComposeUiTest {
            var clickedCategory: String? = null
            var tarotClicks = 0
            setContent {
                AppTheme {
                    HomeScreen(
                        state = data.toUiState(ForecastPeriod.Today),
                        onPeriodSelect = {},
                        onCategoryClick = { clickedCategory = it },
                        onTarotClick = { tarotClicks++ },
                    )
                }
            }

            val love = data.categories.first { it.id == "love" }.id
            onNodeWithTag(HomeScreenTags.categoryCard(love)).performScrollTo()
            onNodeWithTag(HomeScreenTags.categoryCard(love)).performSemanticsAction(SemanticsActions.OnClick)
            onNodeWithTag(HomeScreenTags.TAROT).performScrollTo().performSemanticsAction(SemanticsActions.OnClick)

            assertEquals(love, clickedCategory)
            assertEquals(1, tarotClicks)
        }
}

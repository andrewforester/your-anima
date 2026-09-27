package app.youranima.ui.navigation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.components.ComingSoonScreenTags
import app.youranima.ui.home.HomeScreenTags
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppShellTest {
    @Test
    fun switchesTabsBetweenHomeAndPlaceholders() =
        runComposeUiTest {
            setContent { AppTheme { AppShell() } }

            onNodeWithTag(AppBottomBarTags.BAR).assertIsDisplayed()
            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).assertIsSelected()
            onNodeWithTag(HomeScreenTags.SCREEN).assertIsDisplayed()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Psychics)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Psychics)).assertIsSelected()
            onNodeWithTag(ComingSoonScreenTags.SCREEN).assertIsDisplayed()
            onNode(hasText("Psychics") and hasAnyAncestor(hasTestTag(ComingSoonScreenTags.SCREEN))).assertIsDisplayed()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).assertIsSelected()
            onNodeWithTag(HomeScreenTags.SCREEN).assertIsDisplayed()
        }
}

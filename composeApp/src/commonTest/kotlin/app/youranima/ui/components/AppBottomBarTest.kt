package app.youranima.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.navigation.NavBadges
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppBottomBarTest {
    private val badges = NavBadges(psychicsFree = true, unreadChats = 3)

    @Test
    fun showsFiveTabsAndSwitchesSelection() =
        runComposeUiTest {
            setContent {
                AppTheme {
                    var selected by remember { mutableStateOf(AppTab.Today) }
                    AppBottomBar(selected = selected, badges = badges, onSelect = { selected = it })
                }
            }

            AppTab.entries.forEach { tab -> onNodeWithTag(AppBottomBarTags.navItem(tab)).assertIsDisplayed() }
            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).assertIsSelected()
            onNodeWithText("FREE").assertIsDisplayed()
            onNodeWithText("3").assertIsDisplayed()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Readings)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Readings)).assertIsSelected()
            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Today)).assertIsNotSelected()
        }
}

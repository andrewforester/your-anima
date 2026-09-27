package app.youranima.ui.compatibility

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CompatibilityScreenTest {
    @Test
    fun showsTitleHeadlineAndPair() =
        runComposeUiTest {
            setContent { AppTheme { CompatibilityScreen() } }

            onNode(hasText("Compatibility") and hasTestTag(CompatibilityScreenTags.TITLE)).assertIsDisplayed()
            onNodeWithText("Check your love compatibility").assertIsDisplayed()
            onNodeWithText("Add your partner to see the result").assertIsDisplayed()
            onNodeWithText("You").assertIsDisplayed()
            onNodeWithText("Partner").assertIsDisplayed()
            onNodeWithTag(CompatibilityScreenTags.USER_AVATAR).assertIsDisplayed()
            onNodeWithTag(CompatibilityScreenTags.PARTNER_SLOT).assertIsDisplayed()
            onNodeWithContentDescription("Add partner").assertIsDisplayed()
        }

    @Test
    fun addPartnerCallsTheHoistedCallback() =
        runComposeUiTest {
            var clicks = 0
            setContent { AppTheme { CompatibilityScreen(onAddPartner = { clicks++ }) } }

            onNodeWithTag(CompatibilityScreenTags.ADD_PARTNER).performClick()

            assertEquals(1, clicks)
        }

    @Test
    fun compatibilityTabOpensTheScreen() =
        runComposeUiTest {
            setContent { AppTheme { AppShell() } }

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Compatibility)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Compatibility)).assertIsSelected()
            onNodeWithTag(CompatibilityScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(CompatibilityScreenTags.TITLE).assertIsDisplayed()
        }
}

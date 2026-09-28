package app.youranima.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.unit.dp
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.home.HomeScreenTags
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class PullToReloadTest {
    @Test
    fun swipeDownAtTopCallsReloadOnce() =
        runComposeUiTest {
            var reloads = 0
            setContent { AppTheme { PullToReload(onReload = { reloads++ }) { Feed(initialScroll = 0) } } }

            onNodeWithTag(FEED).performTouchInput { swipeDown() }
            waitForIdle()
            onNodeWithTag(FEED).performTouchInput { swipeDown() }
            waitForIdle()

            assertEquals(1, reloads)
        }

    @Test
    fun scrollingAScrolledFeedDoesNotReload() =
        runComposeUiTest {
            var reloads = 0
            setContent { AppTheme { PullToReload(onReload = { reloads++ }) { Feed(initialScroll = 2000) } } }

            onNodeWithTag(FEED).performTouchInput { swipeDown(startY = centerY - 150f, endY = centerY + 150f) }
            waitForIdle()

            assertEquals(0, reloads)
        }

    @Test
    fun nullReloaderAddsNoWrapper() =
        runComposeUiTest {
            setContent { AppTheme { PullToReload(onReload = null) { Feed(initialScroll = 0) } } }

            onNodeWithTag(FEED).assertIsDisplayed()
            onNodeWithTag(AppShellTags.PULL_TO_RELOAD).assertDoesNotExist()
        }

    @Test
    fun pullDownOnHomeReloads() =
        runComposeUiTest {
            var reloads = 0
            setContent { AppTheme { AppShell(onReload = { reloads++ }) } }

            onNodeWithTag(HomeScreenTags.SCREEN).performTouchInput { swipeDown() }
            waitForIdle()
            assertEquals(1, reloads)
        }

    @Test
    fun pullDownOnPsychicsTabReloads() =
        runComposeUiTest {
            var reloads = 0
            setContent { AppTheme { AppShell(onReload = { reloads++ }) } }

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Psychics)).performClick()
            onNodeWithTag(AppShellTags.PULL_TO_RELOAD).performTouchInput { swipeDown() }
            waitForIdle()
            assertEquals(1, reloads)
        }

    private companion object {
        const val FEED = FEED_TAG
    }
}

private const val FEED_TAG = "feed"

@Composable
private fun Feed(initialScroll: Int) {
    Column(Modifier.fillMaxSize().testTag(FEED_TAG).verticalScroll(rememberScrollState(initialScroll))) {
        repeat(20) { Box(Modifier.height(200.dp)) }
    }
}

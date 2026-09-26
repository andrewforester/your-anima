package app.youranima

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.ui.home.HomeScreenTags
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
    @Test
    fun homeScreenShowsAppTitle() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag(HomeScreenTags.TITLE)
                .assertIsDisplayed()
                .assertTextEquals("Your Anima")
        }
}

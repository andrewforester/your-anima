package app.youranima.ui.psychics

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.psychics.MockPsychicsRepository
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PsychicsScreenTest {
    private val data = MockPsychicsRepository.psychicsData()

    @Test
    fun showsTitleBannerSectionAndPsychic() =
        runComposeUiTest {
            setContent { AppTheme { PsychicsScreen() } }

            onNodeWithTag(PsychicsScreenTags.TITLE).assertIsDisplayed()
            onNode(hasText("Psychics") and hasTestTag(PsychicsScreenTags.TITLE)).assertIsDisplayed()
            onNode(hasText("You have 3 minutes FREE")).assertIsDisplayed()
            onNode(hasText("with 3 psychics")).assertIsDisplayed()

            val section = data.sections.first()
            onNode(hasText(section.title)).assertIsDisplayed()
            onNode(hasText(section.psychics.first().name) and hasAnyAncestor(hasTestTag(PsychicsScreenTags.section(section.id))))
                .performScrollTo()
                .assertIsDisplayed()
        }

    @Test
    fun allFilterIsSelected() =
        runComposeUiTest {
            setContent { AppTheme { PsychicsScreen() } }

            onNodeWithTag(PsychicsScreenTags.filter(PsychicFilter.All)).assertIsSelected()
            onNodeWithTag(PsychicsScreenTags.filter(PsychicFilter.Call)).assertIsNotSelected()
            onNodeWithTag(PsychicsScreenTags.filter(PsychicFilter.Chat)).assertIsNotSelected()
        }

    @Test
    fun buttonsFollowStatusAndChannels() =
        runComposeUiTest {
            setContent { AppTheme { PsychicsScreen() } }

            val psychics = data.sections.first().psychics
            val luna = psychics.first { it.name == "Luna" }
            val whimsy = psychics.first { it.name == "Whimsy Lou" }
            onNodeWithTag(PsychicsScreenTags.callButton(luna.id)).assertIsNotEnabled()
            onNodeWithTag(PsychicsScreenTags.chatButton(luna.id)).assertIsEnabled()
            onNodeWithTag(PsychicsScreenTags.callButton(whimsy.id)).assertIsEnabled()
        }

    @Test
    fun psychicsTabOpensTheScreen() =
        runComposeUiTest {
            setContent { AppTheme { AppShell() } }

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Psychics)).performClick()

            onNodeWithTag(PsychicsScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(PsychicsScreenTags.PROMO).assertIsDisplayed()
        }
}

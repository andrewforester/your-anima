package app.youranima.ui.readings

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.readings.MockReadingsRepository
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ReadingsScreenTest {
    private val content = MockReadingsRepository.readingsContent()

    @Test
    fun showsTitleSectionsAndCards() =
        runComposeUiTest {
            setContent { AppTheme { ReadingsScreen() } }

            onNode(hasText("Readings") and hasTestTag(ReadingsScreenTags.TITLE)).assertIsDisplayed()

            val challenges = content.challengeSection
            onNode(hasText(challenges.title)).assertIsDisplayed()
            val challenge = challenges.challenges.first()
            onNode(hasText(challenge.title) and hasAnyAncestor(hasTestTag(ReadingsScreenTags.challenge(challenge.id))))
                .assertIsDisplayed()
            onNode(hasText("5-day challenge")).assertIsDisplayed()

            content.quizSections.forEach { section ->
                onNode(hasText(section.title)).performScrollTo().assertIsDisplayed()
            }
            val quiz =
                content.quizSections
                    .first()
                    .quizzes
                    .first()
            onNode(hasText(quiz.title) and hasAnyAncestor(hasTestTag(ReadingsScreenTags.quiz(quiz.id))))
                .performScrollTo()
                .assertIsDisplayed()
        }

    @Test
    fun cardsAreNotClickable() =
        runComposeUiTest {
            setContent { AppTheme { ReadingsScreen() } }

            onNode(hasClickAction() and hasAnyAncestor(hasTestTag(ReadingsScreenTags.SCREEN))).assertDoesNotExist()
        }

    @Test
    fun readingsTabOpensTheScreen() =
        runComposeUiTest {
            setContent { AppTheme { AppShell() } }

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Readings)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Readings)).assertIsSelected()
            onNodeWithTag(ReadingsScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(ReadingsScreenTags.TITLE).assertIsDisplayed()
        }
}

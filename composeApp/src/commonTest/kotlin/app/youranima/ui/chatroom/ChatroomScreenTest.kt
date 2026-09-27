package app.youranima.ui.chatroom

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.runComposeUiTest
import app.youranima.data.chatroom.MockChatroomRepository
import app.youranima.ui.components.AppBottomBarTags
import app.youranima.ui.components.AppTab
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ChatroomScreenTest {
    private val data = MockChatroomRepository.chatroomData()

    @Test
    fun showsTitlePromoAndChats() =
        runComposeUiTest {
            setContent { AppTheme { ChatroomScreen() } }

            onNode(hasText("Chatroom") and hasTestTag(ChatroomScreenTags.TITLE)).assertIsDisplayed()
            onNode(hasText("Check our Psychics")).assertIsDisplayed()
            onNode(hasText("More than 900 psychics")).assertIsDisplayed()
            onNode(hasText("1 000 324"), useUnmergedTree = true).assertIsDisplayed()
            onNode(hasText("See Psychics") and hasTestTag(ChatroomScreenTags.SEE_PSYCHICS))
                .assertIsDisplayed()

            val first = data.chats.first()
            onNodeWithTag(ChatroomScreenTags.chat(first.id)).assertIsDisplayed()
            onNode(hasText(first.psychicName, substring = true) and hasTestTag(ChatroomScreenTags.chat(first.id)))
                .assertIsDisplayed()
            onNode(
                hasContentDescription("Available for calls") and hasAnyAncestor(hasTestTag(ChatroomScreenTags.chat(first.id))),
                useUnmergedTree = true,
            ).assertExists()

            val last = data.chats.last()
            onNodeWithTag(ChatroomScreenTags.SCREEN).performScrollToKey(last.id)
            onNodeWithTag(ChatroomScreenTags.chat(last.id)).assertIsDisplayed()
        }

    @Test
    fun onlySeePsychicsIsClickable() =
        runComposeUiTest {
            setContent { AppTheme { ChatroomScreen() } }

            onNode(hasClickAction() and hasAnyAncestor(hasTestTag(ChatroomScreenTags.SCREEN)))
                .assertIsDisplayed()
                .assert(hasTestTag(ChatroomScreenTags.SEE_PSYCHICS))
        }

    @Test
    fun chatroomTabOpensTheScreen() =
        runComposeUiTest {
            setContent { AppTheme { AppShell() } }

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Chatroom)).performClick()

            onNodeWithTag(AppBottomBarTags.navItem(AppTab.Chatroom)).assertIsSelected()
            onNodeWithTag(ChatroomScreenTags.SCREEN).assertIsDisplayed()
            onNodeWithTag(ChatroomScreenTags.TITLE).assertIsDisplayed()
        }
}

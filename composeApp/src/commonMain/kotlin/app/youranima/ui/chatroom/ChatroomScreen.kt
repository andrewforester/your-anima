package app.youranima.ui.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatroomRepository
import app.youranima.data.chatroom.MockChatroomRepository
import app.youranima.resources.Res
import app.youranima.resources.chatroom_title
import app.youranima.ui.components.ScreenTopBar
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

/** Clearance under the content for the shared bottom bar drawn by [app.youranima.ui.navigation.AppShell]. */
private val ContentBottomPadding = 100.dp

/** Stateful entry point: loads the data once. Nothing changes state this round (#50). */
@Composable
fun ChatroomScreen(
    modifier: Modifier = Modifier,
    repository: ChatroomRepository = MockChatroomRepository,
) {
    val state = remember(repository) { repository.chatroomData().toUiState() }
    ChatroomScreen(state = state, onSeePsychicsClick = {}, modifier = modifier)
}

/** Top bar, the "Check our Psychics" promo card and the chat list, all in one vertical list. */
@Composable
fun ChatroomScreen(
    state: ChatroomUiState,
    onSeePsychicsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(ChatroomScreenTags.SCREEN)
                .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = ContentBottomPadding + bottomInset),
    ) {
        item(key = "top_bar") {
            ScreenTopBar(
                title = stringResource(Res.string.chatroom_title),
                titleModifier = Modifier.testTag(ChatroomScreenTags.TITLE),
            )
        }
        item(key = "promo") {
            PsychicsPromoCard(
                promo = state.promo,
                onSeePsychicsClick = onSeePsychicsClick,
                modifier =
                    Modifier
                        .padding(start = ScreenPadding, end = ScreenPadding, top = 4.dp, bottom = 8.dp)
                        .testTag(ChatroomScreenTags.PROMO),
            )
        }
        itemsIndexed(state.chats, key = { _, chat -> chat.id }) { index, chat ->
            if (index > 0) HorizontalDivider(thickness = 1.dp, color = MaterialTheme.appColors.outline)
            ChatRow(chat, Modifier.testTag(ChatroomScreenTags.chat(chat.id)))
        }
    }
}

@Preview
@Composable
private fun ChatroomScreenPreview() {
    AppTheme { ChatroomScreen(state = PreviewChatroomUiState, onSeePsychicsClick = {}) }
}

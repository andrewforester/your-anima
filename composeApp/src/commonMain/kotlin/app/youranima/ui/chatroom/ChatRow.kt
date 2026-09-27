package app.youranima.ui.chatroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatPreview
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography

/**
 * One conversation: [StatusAvatar], name (+ [CallBadge]) and time, 2-line preview (+ [UnreadBadge]).
 * Read out as one node. Not clickable this round (#50).
 */
@Composable
fun ChatRow(
    chat: ChatPreview,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {}
                .padding(horizontal = ScreenPadding, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatusAvatar(avatar = chat.avatar, online = chat.online)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ChatHeader(chat)
            ChatPreviewLine(chat)
        }
    }
}

@Composable
private fun ChatHeader(chat: ChatPreview) {
    val colors = MaterialTheme.appColors
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = chat.psychicName,
                style = MaterialTheme.appTypography.cardTitle,
                color = colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            if (chat.canCall) {
                Spacer(Modifier.width(6.dp))
                CallBadge()
            }
        }
        Text(
            text = chat.timeLabel,
            style = MaterialTheme.appTypography.caption,
            color = colors.onSurfaceMuted,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun ChatPreviewLine(chat: ChatPreview) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = chat.lastMessage,
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.appColors.accentLavender,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (chat.unreadCount > 0) UnreadBadge(chat.unreadCount)
    }
}

@Preview
@Composable
private fun ChatRowPreview() {
    ChatroomPreview {
        Column {
            PreviewChatroomUiState.chats.take(2).forEach { ChatRow(it) }
            ChatRow(PreviewChatroomUiState.chats.first { it.online })
        }
    }
}

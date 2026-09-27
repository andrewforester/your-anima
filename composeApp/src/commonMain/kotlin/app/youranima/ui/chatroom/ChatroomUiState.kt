package app.youranima.ui.chatroom

import androidx.compose.runtime.Immutable
import app.youranima.data.chatroom.ChatPreview
import app.youranima.data.chatroom.ChatroomData
import app.youranima.data.chatroom.MockChatroomRepository
import app.youranima.data.chatroom.PsychicsPromo

/** What the Chatroom tab renders. Visual only this round (#50): no read/unread or presence updates. */
@Immutable
data class ChatroomUiState(
    val promo: PsychicsPromo,
    val chats: List<ChatPreview>,
)

fun ChatroomData.toUiState() = ChatroomUiState(promo = promo, chats = chats)

internal val PreviewChatroomUiState = MockChatroomRepository.chatroomData().toUiState()

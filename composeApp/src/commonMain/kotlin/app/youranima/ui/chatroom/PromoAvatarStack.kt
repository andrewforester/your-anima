package app.youranima.ui.chatroom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatAvatar
import app.youranima.data.chatroom.MockChatroomRepository
import app.youranima.ui.theme.appColors

private val StackWidth = 58.dp
private val StackHeight = 56.dp
private val RingWidth = 2.dp

/** Size and top-left offset of each photo, drawn in order (back-left, back-right, front on top). */
private val Slots =
    listOf(
        28.dp to DpOffset(0.dp, 12.dp),
        28.dp to DpOffset(30.dp, 0.dp),
        40.dp to DpOffset(18.dp, 16.dp),
    )

/** Three overlapping round photos (58×56), each with a 2dp `surface` ring separating the overlaps. */
@Composable
fun PromoAvatarStack(
    avatars: List<ChatAvatar?>,
    modifier: Modifier = Modifier,
) {
    Box(modifier.size(StackWidth, StackHeight)) {
        avatars.zip(Slots).forEach { (avatar, slot) ->
            val (size, offset) = slot
            RingedAvatar(avatar, size, Modifier.offset(offset.x, offset.y))
        }
    }
}

@Composable
private fun RingedAvatar(
    avatar: ChatAvatar?,
    size: Dp,
    modifier: Modifier,
) {
    Box(modifier) {
        ChatAvatarImage(avatar, size)
        Box(Modifier.size(size).border(RingWidth, MaterialTheme.appColors.surface, CircleShape))
    }
}

@Preview
@Composable
private fun PromoAvatarStackPreview() {
    ChatroomPreview { PromoAvatarStack(MockChatroomRepository.chatroomData().promo.avatars) }
}

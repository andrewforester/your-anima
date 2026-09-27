package app.youranima.ui.chatroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatAvatar
import app.youranima.ui.components.PhotoOrPlaceholder
import org.jetbrains.compose.resources.painterResource

/** Circular psychic photo of [size]; `null` [avatar] → the shared placeholder with an icon at half the size. */
@Composable
fun ChatAvatarImage(
    avatar: ChatAvatar?,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    PhotoOrPlaceholder(
        painter = avatar?.let { painterResource(it.drawable) },
        iconSize = size / 2,
        modifier = modifier.size(size).clip(CircleShape),
    )
}

@Preview
@Composable
private fun ChatAvatarImagePreview() {
    ChatroomPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChatAvatarImage(ChatAvatar.Chandra, 48.dp)
            ChatAvatarImage(null, 48.dp)
        }
    }
}

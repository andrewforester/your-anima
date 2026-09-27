package app.youranima.ui.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatAvatar
import app.youranima.resources.Res
import app.youranima.resources.chatroom_cd_offline
import app.youranima.resources.chatroom_cd_online
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

private val AvatarSize = 48.dp
private val RingSize = 12.dp
private val DotSize = 8.dp

/** 48dp chat avatar with a status dot (online `accentTeal`, offline `onSurfaceMuted`) in a `background` ring, bottom-end. */
@Composable
fun StatusAvatar(
    avatar: ChatAvatar?,
    online: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val status = stringResource(if (online) Res.string.chatroom_cd_online else Res.string.chatroom_cd_offline)
    Box(modifier.size(AvatarSize)) {
        ChatAvatarImage(avatar, AvatarSize)
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(RingSize)
                    .background(colors.background, CircleShape)
                    .semantics { contentDescription = status },
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(DotSize).background(if (online) colors.accentTeal else colors.onSurfaceMuted, CircleShape))
        }
    }
}

@Preview
@Composable
private fun StatusAvatarPreview() {
    ChatroomPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusAvatar(ChatAvatar.Ana, online = false)
            StatusAvatar(null, online = true)
        }
    }
}

package app.youranima.ui.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.chatroom_cd_unread
import app.youranima.resources.chatroom_unread_overflow
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

private const val MAX_SHOWN = 99
private val BadgeHeight = 18.dp

/** `accentOrange` pill with the unread [count] (the nav badge colour); "99+" above 99. */
@Composable
fun UnreadBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    val description = pluralStringResource(Res.plurals.chatroom_cd_unread, count, count)
    val label = if (count > MAX_SHOWN) stringResource(Res.string.chatroom_unread_overflow) else count.toString()
    Box(
        modifier =
            modifier
                .height(BadgeHeight)
                .widthIn(min = BadgeHeight)
                .background(MaterialTheme.appColors.accentOrange, CircleShape)
                .padding(horizontal = 5.dp)
                .clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.appTypography.caption.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.appColors.onSurface,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun UnreadBadgePreview() {
    ChatroomPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UnreadBadge(1)
            UnreadBadge(24)
            UnreadBadge(120)
        }
    }
}

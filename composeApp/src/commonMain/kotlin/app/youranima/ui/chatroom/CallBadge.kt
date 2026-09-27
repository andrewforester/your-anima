package app.youranima.ui.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.chatroom_cd_call_badge
import app.youranima.resources.psychics_ic_phone
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 16dp `accentLavender` circle with a phone: this psychic can take calls. */
@Composable
fun CallBadge(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    Box(modifier.size(16.dp).background(colors.accentLavender, CircleShape), contentAlignment = Alignment.Center) {
        // Shared with Psychics under its screen name; renaming to ic_phone is a Theme task (SPEC Decision 14).
        Icon(
            painter = painterResource(Res.drawable.psychics_ic_phone),
            contentDescription = stringResource(Res.string.chatroom_cd_call_badge),
            tint = colors.background,
            modifier = Modifier.size(10.dp),
        )
    }
}

@Preview
@Composable
private fun CallBadgePreview() {
    ChatroomPreview { CallBadge() }
}

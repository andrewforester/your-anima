package app.youranima.ui.psychics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.psychics_action_call
import app.youranima.resources.psychics_ic_phone
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val ButtonShape = RoundedCornerShape(16.dp)

/** Call / Chat button: `primary` when [enabled], glass with muted content (and no clicks) when not. */
@Composable
fun ChannelButton(
    label: String,
    icon: DrawableResource,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val content = if (enabled) colors.onSurface else colors.onSurfaceMuted
    val fill =
        if (enabled) {
            Modifier.background(colors.primary, ButtonShape)
        } else {
            Modifier.background(colors.glassFill, ButtonShape).border(BorderStroke(1.dp, colors.glassBorder), ButtonShape)
        }
    Row(
        modifier =
            modifier
                .height(32.dp)
                .clip(ButtonShape)
                .then(fill)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        Text(text = label, style = MaterialTheme.appTypography.pill, color = content, maxLines = 1)
    }
}

@Preview
@Composable
private fun ChannelButtonPreview() {
    PsychicsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChannelButton(stringResource(Res.string.psychics_action_call), Res.drawable.psychics_ic_phone, enabled = true, onClick = {
            }, modifier = Modifier.width(68.dp))
            ChannelButton(stringResource(Res.string.psychics_action_call), Res.drawable.psychics_ic_phone, enabled = false, onClick = {
            }, modifier = Modifier.width(68.dp))
        }
    }
}

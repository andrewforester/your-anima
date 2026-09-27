package app.youranima.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import app.youranima.resources.psychics_ic_phone
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val ButtonShape = RoundedCornerShape(16.dp)

/**
 * 32-high `pill` button with an optional leading [icon]: `primary` when [enabled], glass with muted content (and no
 * clicks) when not. Psychics Call/Chat, Chatroom "See Psychics".
 */
@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    enabled: Boolean = true,
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
        if (icon != null) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        }
        Text(text = label, style = MaterialTheme.appTypography.pill, color = content, maxLines = 1)
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.appColors.background).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PrimaryButton("Call", onClick = {}, icon = Res.drawable.psychics_ic_phone, modifier = Modifier.width(68.dp))
                PrimaryButton(
                    "Call",
                    onClick = {},
                    icon = Res.drawable.psychics_ic_phone,
                    enabled = false,
                    modifier = Modifier.width(68.dp),
                )
            }
            PrimaryButton("See Psychics", onClick = {}, modifier = Modifier.width(200.dp))
        }
    }
}

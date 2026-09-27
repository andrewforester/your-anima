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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.psychics_ic_phone
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** Fully rounded at any height (radius 16 at the default 32). */
private val ButtonShape = RoundedCornerShape(percent = 50)

/** Default height: the compact card buttons. */
private val DefaultHeight = 32.dp

/** From this height up the label uses the larger `button` style (full-width CTAs), below it `pill`. */
private val LargeLabelMinHeight = 40.dp

/**
 * Fully rounded button (32 high by default, `pill` label; `button` label from [LargeLabelMinHeight]) with an optional
 * leading [icon]: `primary` when [enabled], glass with muted content (and no clicks) when not. Psychics Call/Chat,
 * Chatroom "See Psychics", Paywall "Subscribe" (48).
 */
@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    enabled: Boolean = true,
    height: Dp = DefaultHeight,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    val labelStyle = if (height >= LargeLabelMinHeight) typography.button else typography.pill
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
                .height(height)
                .clip(ButtonShape)
                .then(fill)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        }
        Text(text = label, style = labelStyle, color = content, maxLines = 1)
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
            PrimaryButton("Subscribe", onClick = {}, height = 48.dp, modifier = Modifier.width(370.dp))
        }
    }
}

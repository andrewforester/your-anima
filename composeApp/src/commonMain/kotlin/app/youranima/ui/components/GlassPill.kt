package app.youranima.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
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
import app.youranima.resources.home_birth_chart
import app.youranima.resources.psychics_filter_all
import app.youranima.resources.psychics_filter_call
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

private val PillShape = RoundedCornerShape(20.dp)

/**
 * Glass pill from the Figma design ("Birth Chart"): radius 20, glass fill + border, optional icon + `pill` label.
 * [selected] `null` = a plain button; `true`/`false` = a selectable tab (`true` is drawn with the `primary` fill).
 * Content is centred, so the pill can be stretched (e.g. `weight(1f)` in a filter row).
 */
@Composable
fun GlassPill(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val colors = MaterialTheme.appColors
    val fill =
        if (selected == true) {
            Modifier.background(colors.primary, PillShape)
        } else {
            Modifier.background(colors.glassFill, PillShape).border(BorderStroke(1.dp, colors.glassBorder), PillShape)
        }
    val click =
        if (selected == null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier.selectable(selected = selected, role = Role.Tab, onClick = onClick)
        }
    Row(
        modifier =
            modifier
                .clip(PillShape)
                .then(fill)
                .then(click)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke()
        Text(text = label, style = MaterialTheme.appTypography.pill, color = colors.onSurface, maxLines = 1)
    }
}

@Preview
@Composable
private fun GlassPillPreview() {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassPill(label = stringResource(Res.string.psychics_filter_all), onClick = {}, selected = true)
                GlassPill(label = stringResource(Res.string.psychics_filter_call), onClick = {}, selected = false)
                GlassPill(label = stringResource(Res.string.home_birth_chart), onClick = {})
            }
        }
    }
}

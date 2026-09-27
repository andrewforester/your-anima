package app.youranima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography

private val ChipShape = RoundedCornerShape(10.dp)

/** Small non-interactive chip (20 high, fully rounded): optional [leading] icon/dot + `caption` [label] on [fill]. */
@Composable
fun TagChip(
    label: String,
    fill: Color,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.height(20.dp).background(fill, ChipShape).padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading?.invoke()
        Text(
            text = label,
            style = MaterialTheme.appTypography.caption,
            color = MaterialTheme.appColors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun TagChipPreview() {
    AppTheme {
        val colors = MaterialTheme.appColors
        Row(
            modifier = Modifier.background(colors.background).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagChip(label = "online", fill = colors.backgroundDeep) {
                Box(Modifier.size(6.dp).background(colors.accentTeal, CircleShape))
            }
            TagChip(label = "Quiz", fill = colors.primary.copy(alpha = 0.2f))
        }
    }
}

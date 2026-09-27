package app.youranima.ui.readings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.readings_ic_clock
import app.youranima.ui.components.TagChip
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource

/** Alpha of the `primary` chip fill (the original's blue chip in Figma colours, SPEC Decision 4). */
private const val CHIP_FILL_ALPHA = 0.2f

/** Readings [TagChip] ("5-day challenge", "Quiz", "3 min"), optionally with the clock icon. Not clickable. */
@Composable
fun ReadingsChip(
    label: String,
    modifier: Modifier = Modifier,
    withClock: Boolean = false,
) {
    val colors = MaterialTheme.appColors
    TagChip(
        label = label,
        fill = colors.primary.copy(alpha = CHIP_FILL_ALPHA),
        modifier = modifier,
        leading =
            if (withClock) {
                {
                    Icon(
                        painter = painterResource(Res.drawable.readings_ic_clock),
                        contentDescription = null,
                        tint = colors.onSurface,
                        modifier = Modifier.size(12.dp),
                    )
                }
            } else {
                null
            },
    )
}

@Preview
@Composable
private fun ReadingsChipPreview() {
    ReadingsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ReadingsChip(label = "5-day challenge", withClock = true)
            ReadingsChip(label = "Quiz")
            ReadingsChip(label = "3 min")
        }
    }
}

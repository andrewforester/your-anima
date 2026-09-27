package app.youranima.ui.psychics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.PsychicStatus
import app.youranima.resources.Res
import app.youranima.resources.psychics_status_busy
import app.youranima.resources.psychics_status_online
import app.youranima.ui.components.TagChip
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

private const val CHIP_FILL_ALPHA = 0.8f

/** "● online" / "● busy" [TagChip] over the photo; only the dot is coloured (teal online, gold busy). */
@Composable
fun StatusChip(
    status: PsychicStatus,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val (dot, label) =
        when (status) {
            PsychicStatus.Online -> colors.accentTeal to Res.string.psychics_status_online
            PsychicStatus.Busy -> colors.accentGold to Res.string.psychics_status_busy
        }
    TagChip(
        label = stringResource(label),
        fill = colors.backgroundDeep.copy(alpha = CHIP_FILL_ALPHA),
        modifier = modifier,
    ) {
        Box(Modifier.size(6.dp).background(dot, CircleShape))
    }
}

@Preview
@Composable
private fun StatusChipPreview() {
    PsychicsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip(PsychicStatus.Online)
            StatusChip(PsychicStatus.Busy)
        }
    }
}

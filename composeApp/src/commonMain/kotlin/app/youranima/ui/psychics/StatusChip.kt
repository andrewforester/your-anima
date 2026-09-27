package app.youranima.ui.psychics

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.PsychicStatus
import app.youranima.resources.Res
import app.youranima.resources.psychics_status_busy
import app.youranima.resources.psychics_status_online
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

private const val CHIP_FILL_ALPHA = 0.8f
private val ChipShape = RoundedCornerShape(10.dp)

/** "● online" / "● busy" chip over the photo; only the dot is coloured (teal online, gold busy). */
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
    Row(
        modifier =
            modifier
                .height(20.dp)
                .background(colors.backgroundDeep.copy(alpha = CHIP_FILL_ALPHA), ChipShape)
                .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(6.dp).background(dot, CircleShape))
        Text(text = stringResource(label), style = MaterialTheme.appTypography.caption, color = colors.onSurface)
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

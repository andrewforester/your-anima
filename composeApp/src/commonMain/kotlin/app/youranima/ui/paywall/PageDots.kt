package app.youranima.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.paywall_cd_page
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

private val DotSize = 6.dp

/** Page indicator: [count] 6dp dots, [current] in `onSurface`, the rest muted. Not clickable; read as "Page N of M". */
@Composable
fun PageDots(
    count: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val description = stringResource(Res.string.paywall_cd_page, current + 1, count)
    Row(
        modifier =
            modifier
                .testTag(PaywallScreenTags.PAGE_INDICATOR)
                .clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(count) { page ->
            val color = if (page == current) colors.onSurface else colors.onSurfaceMuted
            Box(Modifier.size(DotSize).background(color, CircleShape))
        }
    }
}

@Preview
@Composable
private fun PageDotsPreview() {
    PaywallPreview { PageDots(count = 4, current = 1) }
}

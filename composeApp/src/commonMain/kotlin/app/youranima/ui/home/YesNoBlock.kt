package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_ic_check
import app.youranima.resources.home_ic_cross
import app.youranima.resources.home_no_title
import app.youranima.resources.home_yes_title
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class YesNoKind { Yes, No }

/** "Yes for today" / "No for today": badge + title on the left, three marked items on the right. */
@Composable
fun YesNoBlock(
    kind: YesNoKind,
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val mark =
        painterResource(
            when (kind) {
                YesNoKind.Yes -> Res.drawable.home_ic_check
                YesNoKind.No -> Res.drawable.home_ic_cross
            },
        )
    val badgeBrush =
        when (kind) {
            // Teal top-right -> blue bottom-left.
            YesNoKind.Yes ->
                Brush.linearGradient(
                    colors = listOf(colors.yesGradientStart, colors.yesGradientEnd),
                    start = Offset(Float.POSITIVE_INFINITY, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY),
                )
            // Red top-left -> plum bottom-right.
            YesNoKind.No -> Brush.linearGradient(listOf(colors.noGradientStart, colors.noGradientEnd))
        }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag(if (kind == YesNoKind.Yes) HomeScreenTags.YES else HomeScreenTags.NO),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(20.dp).background(badgeBrush, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(painter = mark, contentDescription = null, tint = colors.onSurface, modifier = Modifier.size(10.dp))
            }
            Text(
                text =
                    stringResource(
                        when (kind) {
                            YesNoKind.Yes -> Res.string.home_yes_title
                            YesNoKind.No -> Res.string.home_no_title
                        },
                    ),
                style = MaterialTheme.appTypography.sectionTitle,
                color = colors.onSurface,
            )
        }
        Column(Modifier.weight(1f)) {
            items.forEach { item -> MarkedItem(text = item, mark = mark, color = colors.onSurface) }
        }
    }
}

@Composable
private fun MarkedItem(
    text: String,
    mark: Painter,
    color: Color,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = mark, contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(14.dp))
        Text(
            text = text,
            style = MaterialTheme.appTypography.bodyRegular,
            color = color,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun YesNoBlockPreview() {
    HomePreview {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            YesNoBlock(kind = YesNoKind.Yes, items = PreviewHomeUiState.yesForToday)
            YesNoBlock(kind = YesNoKind.No, items = PreviewHomeUiState.noForToday)
        }
    }
}

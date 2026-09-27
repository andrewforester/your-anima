package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

private val BadgeSize = 20.dp
private val ItemsIndent = 28.dp

enum class YesNoKind { Yes, No }

/** "Yes for today" and "No for today" in one card, separated by a divider. */
@Composable
fun YesNoCard(
    yes: List<String>,
    no: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag(HomeScreenTags.YES_NO)
                .homeCard(PaddingValues(horizontal = 16.dp, vertical = 20.dp)),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        YesNoList(kind = YesNoKind.Yes, items = yes)
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.appColors.outline)
        YesNoList(kind = YesNoKind.No, items = no)
    }
}

/** Badge + title, then the marked items below, indented under the title. */
@Composable
fun YesNoList(
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
    val accent =
        when (kind) {
            YesNoKind.Yes -> colors.accentTeal
            YesNoKind.No -> colors.accentOrange
        }
    Column(
        modifier = modifier.testTag(if (kind == YesNoKind.Yes) HomeScreenTags.YES else HomeScreenTags.NO),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(BadgeSize).background(accent, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(painter = mark, contentDescription = null, tint = colors.onSurface, modifier = Modifier.size(12.dp))
            }
            Text(
                text =
                    stringResource(
                        when (kind) {
                            YesNoKind.Yes -> Res.string.home_yes_title
                            YesNoKind.No -> Res.string.home_no_title
                        },
                    ),
                style = MaterialTheme.appTypography.cardTitle,
                color = colors.onSurface,
            )
        }
        items.forEach { item ->
            MarkedItem(
                text = item,
                mark = mark,
                markColor = accent,
                modifier = Modifier.padding(start = ItemsIndent),
            )
        }
    }
}

@Composable
private fun MarkedItem(
    text: String,
    mark: Painter,
    markColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = mark, contentDescription = null, tint = markColor, modifier = Modifier.size(14.dp))
        Text(
            text = text,
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.appColors.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun YesNoCardPreview() {
    HomePreview { YesNoCard(yes = PreviewHomeUiState.yesForToday, no = PreviewHomeUiState.noForToday) }
}

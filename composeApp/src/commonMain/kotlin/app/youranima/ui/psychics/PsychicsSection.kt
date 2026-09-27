package app.youranima.ui.psychics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.PsychicSection
import app.youranima.resources.Res
import app.youranima.resources.psychics_view_all
import app.youranima.ui.components.TintedIconBox
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val BadgeShape = RoundedCornerShape(8.dp)
private val ViewAllMinHeight = 32.dp

/** Section header (badge + title + View All), subtitle and a full-width horizontal carousel of [PsychicCard]s. */
@Composable
fun PsychicsSection(
    section: PsychicSection,
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit = {},
    onCallClick: (String) -> Unit = {},
    onChatClick: (String) -> Unit = {},
) {
    Column(modifier.fillMaxWidth().testTag(PsychicsScreenTags.section(section.id))) {
        SectionHeader(section = section, onViewAllClick = onViewAllClick)
        Spacer(Modifier.height(4.dp))
        Text(
            text = section.subtitle,
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.appColors.accentLavender,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = ScreenPadding),
        )
        Spacer(Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier.testTag(PsychicsScreenTags.carousel(section.id)),
            contentPadding = PaddingValues(horizontal = ScreenPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(section.psychics, key = { it.id }) { psychic ->
                PsychicCard(
                    psychic = psychic,
                    onCallClick = { onCallClick(psychic.id) },
                    onChatClick = { onChatClick(psychic.id) },
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    section: PsychicSection,
    onViewAllClick: () -> Unit,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ScreenPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TintedIconBox(
            icon = painterResource(section.icon.drawable),
            tint = section.icon.tint,
            size = 28.dp,
            iconSize = 16.dp,
            shape = BadgeShape,
            contentDescription = null,
        )
        Text(
            text = section.title,
            style = typography.cardTitle,
            color = colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(Res.string.psychics_view_all),
            style = typography.pill,
            color = colors.primary,
            modifier =
                Modifier
                    .clip(BadgeShape)
                    .clickable(onClick = onViewAllClick)
                    .heightIn(min = ViewAllMinHeight)
                    .wrapContentHeight()
                    .testTag(PsychicsScreenTags.viewAll(section.id)),
        )
    }
}

@Preview
@Composable
private fun PsychicsSectionPreview() {
    AppTheme { PsychicsSection(section = PreviewPsychicsUiState.sections.first()) }
}

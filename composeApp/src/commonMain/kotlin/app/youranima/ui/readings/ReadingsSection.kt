package app.youranima.ui.readings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography

/** Section title and a full-width horizontal carousel (content padding 16, spacing 12) filled by [items]. */
@Composable
fun ReadingsSection(
    id: String,
    title: String,
    modifier: Modifier = Modifier,
    items: LazyListScope.() -> Unit,
) {
    Column(modifier.fillMaxWidth().testTag(ReadingsScreenTags.section(id))) {
        Text(
            text = title,
            style = MaterialTheme.appTypography.cardTitle,
            color = MaterialTheme.appColors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = ScreenPadding),
        )
        Spacer(Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier.testTag(ReadingsScreenTags.carousel(id)),
            contentPadding = PaddingValues(horizontal = ScreenPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = items,
        )
    }
}

@Preview
@Composable
private fun ReadingsSectionPreview() {
    val section = PreviewReadingsUiState.quizSections.first()
    AppTheme {
        ReadingsSection(id = section.id, title = section.title) {
            items(section.quizzes, key = { it.id }) { QuizCard(it) }
        }
    }
}

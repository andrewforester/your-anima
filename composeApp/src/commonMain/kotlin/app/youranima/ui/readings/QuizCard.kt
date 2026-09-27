package app.youranima.ui.readings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.readings.Quiz
import app.youranima.resources.Res
import app.youranima.resources.readings_duration_minutes
import app.youranima.resources.readings_quiz
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val CardWidth = 144.dp
private val IllustrationSize = 112.dp
private val FallbackIconSize = 56.dp

/** Quiz card: square artwork tile, 2-line title and "Quiz" + duration chips below it, on the page background. */
@Composable
fun QuizCard(
    quiz: Quiz,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(CardWidth).testTag(ReadingsScreenTags.quiz(quiz.id)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        QuizTile(quiz.illustration.visual)
        Text(
            text = quiz.title,
            style = MaterialTheme.appTypography.cardTitle,
            color = MaterialTheme.appColors.onSurface,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ReadingsChip(label = stringResource(Res.string.readings_quiz))
            ReadingsChip(label = stringResource(Res.string.readings_duration_minutes, quiz.durationMinutes))
        }
    }
}

@Composable
private fun QuizTile(visual: ArtVisual) {
    Box(
        modifier = Modifier.size(CardWidth).appCard(PaddingValues()).clip(MaterialTheme.shapes.large),
        contentAlignment = Alignment.Center,
    ) {
        when (visual) {
            is ArtVisual.Image -> {
                Image(
                    painter = painterResource(visual.drawable),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(IllustrationSize),
                )
            }

            is ArtVisual.TintedIcon -> {
                Icon(
                    painter = painterResource(visual.drawable),
                    contentDescription = null,
                    tint = visual.tint,
                    modifier = Modifier.size(FallbackIconSize),
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuizCardPreview() {
    ReadingsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val quizzes = PreviewReadingsUiState.quizSections.first().quizzes
            QuizCard(quizzes[0])
            QuizCard(quizzes[2])
        }
    }
}

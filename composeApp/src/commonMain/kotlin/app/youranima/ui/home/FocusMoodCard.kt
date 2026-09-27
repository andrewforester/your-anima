package app.youranima.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.home.MoodScore
import app.youranima.resources.Res
import app.youranima.resources.home_focus_mood_info
import app.youranima.resources.home_focus_mood_title
import app.youranima.resources.home_ic_info
import app.youranima.resources.home_mood_percent
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val RingSize = 52.dp
private val RingStroke = 5.2.dp

@Composable
fun FocusMoodCard(
    scores: List<MoodScore>,
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag(HomeScreenTags.FOCUS_MOOD)
                .appCard(PaddingValues(horizontal = 16.dp, vertical = 20.dp)),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.home_focus_mood_title),
                style = MaterialTheme.appTypography.cardTitle,
                color = colors.onSurface,
            )
            Icon(
                painter = painterResource(Res.drawable.home_ic_info),
                contentDescription = stringResource(Res.string.home_focus_mood_info),
                tint = colors.onSurfaceMuted,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onInfoClick).size(16.dp),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            scores.forEach { score ->
                MoodRing(score = score, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MoodRing(
    score: MoodScore,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Column(
        modifier = modifier.testTag(HomeScreenTags.moodRing(score.category)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.size(RingSize), contentAlignment = Alignment.Center) {
            ProgressRing(
                fraction = score.percent / 100f,
                color = score.category.color,
                trackColor = colors.outline,
                modifier = Modifier.size(RingSize),
            )
            Text(
                text = stringResource(Res.string.home_mood_percent, score.percent),
                style = MaterialTheme.appTypography.ringValue,
                color = colors.onSurface,
            )
        }
        Text(
            text = stringResource(score.category.label),
            style = MaterialTheme.appTypography.caption,
            color = colors.accentLavender,
        )
    }
}

/** Track circle plus a progress arc clockwise from 12 o'clock. */
@Composable
private fun ProgressRing(
    fraction: Float,
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val stroke = RingStroke.toPx()
        val inset = stroke / 2
        val arcSize = Size(size.width - stroke, size.height - stroke)
        drawCircle(color = trackColor, radius = (size.minDimension - stroke) / 2, style = Stroke(stroke))
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * fraction.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

@Preview
@Composable
private fun FocusMoodCardPreview() {
    HomePreview { FocusMoodCard(scores = PreviewHomeUiState.mood) }
}

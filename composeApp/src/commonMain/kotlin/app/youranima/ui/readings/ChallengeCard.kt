package app.youranima.ui.readings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.readings.Challenge
import app.youranima.data.readings.ReadingArt
import app.youranima.resources.Res
import app.youranima.resources.readings_challenge_days
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val CardWidth = 296.dp
private val CardHeight = 176.dp
private val IllustrationHeight = 64.dp
private val ScrimHeight = 112.dp
private val GlowRadius = 160.dp

/** "Improve yourself" card: artwork (or a glow) on top, chip + title + 2-line description at the bottom over a scrim. */
@Composable
fun ChallengeCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Box(
        modifier
            .size(CardWidth, CardHeight)
            .appCard(PaddingValues())
            .clip(MaterialTheme.shapes.large)
            .testTag(ReadingsScreenTags.challenge(challenge.id)),
    ) {
        ChallengeArt(challenge.illustration)
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(ScrimHeight)
                .background(Brush.verticalGradient(listOf(colors.surface.copy(alpha = 0f), colors.surface))),
        )
        ChallengeText(challenge, Modifier.align(Alignment.BottomStart))
    }
}

@Composable
private fun ChallengeArt(art: ReadingArt?) {
    val visual = art?.visual
    if (visual is ArtVisual.Image) {
        Image(
            painter = painterResource(visual.drawable),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(IllustrationHeight),
        )
    } else {
        val glow = MaterialTheme.appColors.cardGlow
        Box(
            Modifier.fillMaxSize().drawBehind {
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(glow, glow.copy(alpha = 0f)),
                        center = Offset(size.width / 2, 0f),
                        radius = GlowRadius.toPx(),
                    ),
                )
            },
        )
    }
}

@Composable
private fun ChallengeText(
    challenge: Challenge,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Column(modifier.fillMaxWidth().padding(16.dp)) {
        ReadingsChip(label = stringResource(Res.string.readings_challenge_days, challenge.days), withClock = true)
        Spacer(Modifier.height(8.dp))
        Text(
            text = challenge.title,
            style = typography.cardTitle,
            color = colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = challenge.description,
            style = typography.body,
            color = colors.accentLavender,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun ChallengeCardPreview() {
    ReadingsPreview {
        Column {
            val challenges = PreviewReadingsUiState.challengeSection.challenges
            ChallengeCard(challenges[0])
            Spacer(Modifier.height(12.dp))
            ChallengeCard(challenges[1])
        }
    }
}

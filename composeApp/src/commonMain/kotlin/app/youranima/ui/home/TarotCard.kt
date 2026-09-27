package app.youranima.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_tarot_title
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

private val GlowRadius = 200.dp

/** "Tarot Insight": title and a radial glow only; the content is not designed yet (SPEC.md, Decision 2). */
@Composable
fun TarotCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(160.dp)
                .testTag(HomeScreenTags.TAROT)
                .homeCard(PaddingValues())
                .clip(MaterialTheme.shapes.large)
                .drawBehind {
                    drawRect(
                        Brush.radialGradient(
                            colors = listOf(colors.cardGlow, Color.Transparent),
                            center = Offset(size.width / 2, size.height),
                            radius = GlowRadius.toPx(),
                        ),
                    )
                }.clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        Text(
            text = stringResource(Res.string.home_tarot_title),
            style = MaterialTheme.appTypography.cardTitle,
            color = colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun TarotCardPreview() {
    HomePreview { TarotCard() }
}

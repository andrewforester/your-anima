package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_tip_label
import app.youranima.resources.home_tip_stars
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** "Tip for the day" banner: gradient fill, shooting stars bleeding off the top-right corner. */
@Composable
fun TipCard(
    tip: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val gradient =
        Brush.horizontalGradient(
            0f to colors.tipGradientStart,
            0.4f to colors.tipGradientStart,
            1f to colors.tipGradientEnd,
        )
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(gradient)
                .testTag(HomeScreenTags.TIP),
    ) {
        Image(
            painter = painterResource(Res.drawable.home_tip_stars),
            contentDescription = null,
            modifier = Modifier.align(Alignment.TopEnd),
        )
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 72.dp, top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = stringResource(Res.string.home_tip_label),
                style = MaterialTheme.appTypography.bodyRegular,
                color = colors.onSurface,
            )
            Text(
                text = tip,
                style = MaterialTheme.appTypography.sectionTitle,
                color = colors.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun TipCardPreview() {
    HomePreview { TipCard(tip = PreviewHomeUiState.tipOfTheDay) }
}

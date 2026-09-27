package app.youranima.ui.psychics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.FreeMinutesPromo
import app.youranima.resources.Res
import app.youranima.resources.psychics_ic_gift
import app.youranima.resources.psychics_promo_subtitle
import app.youranima.resources.psychics_promo_title
import app.youranima.ui.components.TintedIconBox
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** "You have N minutes FREE / with M psychics" card with a pink gift badge. Not clickable. */
@Composable
fun PromoBanner(
    promo: FreeMinutesPromo,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag(PsychicsScreenTags.PROMO)
                .appCard(PaddingValues(horizontal = 16.dp, vertical = 12.dp)),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TintedIconBox(
            icon = painterResource(Res.drawable.psychics_ic_gift),
            tint = colors.accentPink,
            size = 42.dp,
            iconSize = 20.dp,
            shape = MaterialTheme.shapes.medium,
            contentDescription = null,
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(Res.string.psychics_promo_title, promo.freeMinutes),
                style = typography.cardTitle,
                color = colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(Res.string.psychics_promo_subtitle, promo.psychicsCount),
                style = typography.body,
                color = colors.accentLavender,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun PromoBannerPreview() {
    PsychicsPreview { PromoBanner(promo = PreviewPsychicsUiState.promo) }
}

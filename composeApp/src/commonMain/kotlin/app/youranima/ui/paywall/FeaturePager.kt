package app.youranima.ui.paywall

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.paywall.PremiumFeature
import app.youranima.ui.components.TintedIconBox
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val FeatureIconSize = 96.dp
private val FeatureIconGlyphSize = 48.dp

/** Swipeable feature slides (no auto-advance, no looping); [pagerState] is shared with the [PageDots] below. */
@Composable
fun FeaturePager(
    features: List<PremiumFeature>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth().testTag(PaywallScreenTags.PAGER),
    ) { page ->
        FeatureSlide(feature = features[page])
    }
}

/** One slide: tinted feature icon in the glow, centred title and a 2-line subtitle (every page the same height). */
@Composable
fun FeatureSlide(
    feature: PremiumFeature,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TintedIconBox(
            icon = painterResource(feature.icon),
            tint = feature.tint,
            size = FeatureIconSize,
            iconSize = FeatureIconGlyphSize,
            shape = CircleShape,
            contentDescription = null,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(feature.title),
            style = MaterialTheme.appTypography.name,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(feature.subtitle),
            style = MaterialTheme.appTypography.body,
            color = colors.accentLavender,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun FeaturePagerPreview() {
    PaywallPreview {
        val features = PreviewPaywallUiState.features
        FeaturePager(features = features, pagerState = rememberPagerState(initialPage = 1) { features.size })
    }
}

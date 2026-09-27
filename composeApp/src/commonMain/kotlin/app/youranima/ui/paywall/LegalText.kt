package app.youranima.ui.paywall

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import app.youranima.data.paywall.SubscriptionPlan
import app.youranima.resources.Res
import app.youranima.resources.paywall_legal_charge
import app.youranima.resources.paywall_legal_intro
import app.youranima.resources.paywall_legal_outro
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

/** Auto-renew terms for [plan]: muted `caption`, with the charge sentence (price + period) emphasised in lavender. */
@Composable
fun LegalText(
    plan: SubscriptionPlan,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val intro = stringResource(Res.string.paywall_legal_intro)
    val charge = stringResource(Res.string.paywall_legal_charge, plan.price, plan.period)
    val outro = stringResource(Res.string.paywall_legal_outro)
    val text =
        buildAnnotatedString {
            append(intro)
            append(' ')
            withStyle(SpanStyle(color = colors.accentLavender)) { append(charge) }
            append(' ')
            append(outro)
        }
    Text(
        text = text,
        style = MaterialTheme.appTypography.caption,
        color = colors.onSurfaceMuted,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth().padding(horizontal = ScreenPadding).testTag(PaywallScreenTags.LEGAL),
    )
}

@Preview
@Composable
private fun LegalTextPreview() {
    PaywallPreview { LegalText(plan = PreviewPaywallUiState.selectedPlan) }
}

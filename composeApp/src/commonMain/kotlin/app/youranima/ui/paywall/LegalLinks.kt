package app.youranima.ui.paywall

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.paywall_link_separator
import app.youranima.resources.paywall_privacy
import app.youranima.resources.paywall_subscription_terms
import app.youranima.resources.paywall_terms
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** "Terms of Service · Privacy Policy · Subscription Terms": `primary` caption links with muted separators. */
@Composable
fun LegalLinks(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSubscriptionTermsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = ScreenPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LegalLink(Res.string.paywall_terms, PaywallScreenTags.TERMS, onTermsClick)
        LinkSeparator()
        LegalLink(Res.string.paywall_privacy, PaywallScreenTags.PRIVACY, onPrivacyClick)
        LinkSeparator()
        LegalLink(Res.string.paywall_subscription_terms, PaywallScreenTags.SUBSCRIPTION_TERMS, onSubscriptionTermsClick)
    }
}

@Composable
private fun LegalLink(
    label: StringResource,
    tag: String,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(label),
        style = MaterialTheme.appTypography.caption,
        color = MaterialTheme.appColors.primary,
        maxLines = 1,
        modifier = Modifier.testTag(tag).clickable(role = Role.Button, onClick = onClick).padding(vertical = 12.dp),
    )
}

@Composable
private fun LinkSeparator() {
    Text(
        text = stringResource(Res.string.paywall_link_separator),
        style = MaterialTheme.appTypography.caption,
        color = MaterialTheme.appColors.onSurfaceMuted,
        modifier = Modifier.padding(horizontal = 4.dp),
    )
}

@Preview
@Composable
private fun LegalLinksPreview() {
    PaywallPreview { LegalLinks(onTermsClick = {}, onPrivacyClick = {}, onSubscriptionTermsClick = {}) }
}

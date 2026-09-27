package app.youranima.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.youranima.data.paywall.PremiumFeature
import app.youranima.resources.Res
import app.youranima.resources.home_ic_moon
import app.youranima.resources.home_ic_sun
import app.youranima.resources.ic_heart
import app.youranima.resources.ic_message_circle
import app.youranima.resources.paywall_feature_compatibility_subtitle
import app.youranima.resources.paywall_feature_compatibility_title
import app.youranima.resources.paywall_feature_horoscope_subtitle
import app.youranima.resources.paywall_feature_horoscope_title
import app.youranima.resources.paywall_feature_psychics_subtitle
import app.youranima.resources.paywall_feature_psychics_title
import app.youranima.resources.paywall_feature_tarot_subtitle
import app.youranima.resources.paywall_feature_tarot_title
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/** Horizontal padding of the page blocks below the pager (plans, legal text, button, links). */
internal val ScreenPadding = 16.dp

internal val PremiumFeature.title: StringResource
    get() =
        when (this) {
            PremiumFeature.Horoscope -> Res.string.paywall_feature_horoscope_title
            PremiumFeature.Compatibility -> Res.string.paywall_feature_compatibility_title
            PremiumFeature.Tarot -> Res.string.paywall_feature_tarot_title
            PremiumFeature.Psychics -> Res.string.paywall_feature_psychics_title
        }

internal val PremiumFeature.subtitle: StringResource
    get() =
        when (this) {
            PremiumFeature.Horoscope -> Res.string.paywall_feature_horoscope_subtitle
            PremiumFeature.Compatibility -> Res.string.paywall_feature_compatibility_subtitle
            PremiumFeature.Tarot -> Res.string.paywall_feature_tarot_subtitle
            PremiumFeature.Psychics -> Res.string.paywall_feature_psychics_subtitle
        }

// TODO(theme): home_ic_sun / home_ic_moon are shared now; renaming to ic_* is a Theme task.
internal val PremiumFeature.icon: DrawableResource
    get() =
        when (this) {
            PremiumFeature.Horoscope -> Res.drawable.home_ic_sun
            PremiumFeature.Compatibility -> Res.drawable.ic_heart
            PremiumFeature.Tarot -> Res.drawable.home_ic_moon
            PremiumFeature.Psychics -> Res.drawable.ic_message_circle
        }

internal val PremiumFeature.tint: Color
    @Composable @ReadOnlyComposable
    get() =
        when (this) {
            PremiumFeature.Horoscope -> MaterialTheme.appColors.accentGold
            PremiumFeature.Compatibility -> MaterialTheme.appColors.accentOrange
            PremiumFeature.Tarot -> MaterialTheme.appColors.accentPurple
            PremiumFeature.Psychics -> MaterialTheme.appColors.accentPink
        }

/** Theme + screen background for component previews. */
@Composable
internal fun PaywallPreview(content: @Composable () -> Unit) {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(vertical = 16.dp)) {
            content()
        }
    }
}

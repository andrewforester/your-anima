package app.youranima.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Design-system sheet: every colour token and text style. Token names are not user-facing strings. */
@Composable
fun ThemeShowcase(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    val type = MaterialTheme.appTypography
    val swatches: List<Pair<String, Color>> =
        listOf(
            "background" to colors.background,
            "backgroundDeep" to colors.backgroundDeep,
            "surface" to colors.surface,
            "outline" to colors.outline,
            "heroGradientTop" to colors.heroGradientTop,
            "moon" to colors.moon,
            "primary" to colors.primary,
            "accentOrange" to colors.accentOrange,
            "accentPink" to colors.accentPink,
            "accentTeal" to colors.accentTeal,
            "accentLavender" to colors.accentLavender,
            "accentPurple" to colors.accentPurple,
            "accentGold" to colors.accentGold,
            "onSurface" to colors.onSurface,
            "onSurfaceMuted" to colors.onSurfaceMuted,
            "textSoft" to colors.textSoft,
            "glassFill" to colors.glassFill,
            "glassBorder" to colors.glassBorder,
            "lockBadge" to colors.lockBadge,
            "tipGradientStart" to colors.tipGradientStart,
            "tipGradientEnd" to colors.tipGradientEnd,
            "yesGradientStart" to colors.yesGradientStart,
            "yesGradientEnd" to colors.yesGradientEnd,
            "noGradientStart" to colors.noGradientStart,
            "noGradientEnd" to colors.noGradientEnd,
            "cardGlow" to colors.cardGlow,
        )
    val styles: List<Pair<String, TextStyle>> =
        listOf(
            "name" to type.name,
            "cardTitle" to type.cardTitle,
            "cardLead" to type.cardLead,
            "tab" to type.tab,
            "button" to type.button,
            "pill" to type.pill,
            "body" to type.body,
            "input" to type.input,
            "ringValue" to type.ringValue,
            "caption" to type.caption,
            "badge" to type.badge,
            "sectionTitle" to type.sectionTitle,
            "preview" to type.preview,
            "bodyRegular" to type.bodyRegular,
            "headline" to type.headline,
        )
    Column(
        modifier =
            modifier
                .background(colors.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        swatches.forEach { (label, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(32.dp)
                        .background(color, MaterialTheme.shapes.medium)
                        .border(1.dp, colors.outline, MaterialTheme.shapes.medium),
                )
                Text(
                    text = label,
                    style = type.body,
                    color = colors.accentLavender,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
        styles.forEach { (label, style) ->
            Text(text = label, style = style, color = colors.onSurface)
        }
    }
}

@Preview
@Composable
private fun ThemeShowcasePreview() {
    AppTheme { ThemeShowcase() }
}

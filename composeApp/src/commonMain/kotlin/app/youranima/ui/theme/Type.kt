package app.youranima.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import app.youranima.resources.Res
import app.youranima.resources.geist_bold
import app.youranima.resources.geist_medium
import app.youranima.resources.geist_regular
import app.youranima.resources.geist_semibold
import org.jetbrains.compose.resources.Font

/**
 * Text styles from the Figma frame `astrology-home` and docs/design/home-feed/SPEC.md.
 * Geist, line height `normal` (unspecified) unless the spec gives one.
 */
@Immutable
data class AppTypography(
    val name: TextStyle,
    val cardTitle: TextStyle,
    val cardLead: TextStyle,
    val tab: TextStyle,
    val button: TextStyle,
    val pill: TextStyle,
    val body: TextStyle,
    val input: TextStyle,
    val ringValue: TextStyle,
    val caption: TextStyle,
    val badge: TextStyle,
    val sectionTitle: TextStyle,
    val preview: TextStyle,
    val bodyRegular: TextStyle,
    val headline: TextStyle,
)

@Composable
internal fun geistFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.geist_regular, FontWeight.Normal),
        Font(Res.font.geist_medium, FontWeight.Medium),
        Font(Res.font.geist_semibold, FontWeight.SemiBold),
        Font(Res.font.geist_bold, FontWeight.Bold),
    )

internal fun appTypography(fontFamily: FontFamily): AppTypography {
    fun style(
        size: Int,
        weight: FontWeight,
        lineHeight: Int? = null,
    ) = TextStyle(
        fontFamily = fontFamily,
        fontSize = size.sp,
        fontWeight = weight,
        lineHeight = lineHeight?.sp ?: TextUnit.Unspecified,
    )

    return AppTypography(
        name = style(24, FontWeight.SemiBold),
        cardTitle = style(16, FontWeight.SemiBold),
        cardLead = style(16, FontWeight.Medium),
        tab = style(14, FontWeight.Medium),
        button = style(14, FontWeight.SemiBold),
        pill = style(13, FontWeight.SemiBold),
        body = style(13, FontWeight.Medium),
        input = style(13, FontWeight.Normal),
        ringValue = style(12, FontWeight.SemiBold),
        caption = style(11, FontWeight.Medium),
        badge = style(8, FontWeight.Bold),
        sectionTitle = style(18, FontWeight.Bold, lineHeight = 26),
        preview = style(16, FontWeight.Normal, lineHeight = 23),
        bodyRegular = style(14, FontWeight.Normal, lineHeight = 18),
        headline = style(22, FontWeight.Bold),
    )
}

/** Material 3 type scale with Geist, so plain Material components match the design. */
internal fun materialTypography(fontFamily: FontFamily): Typography {
    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = base.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = base.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = base.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = base.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = base.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = base.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = base.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = base.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = base.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = base.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = base.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = base.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = base.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = base.labelSmall.copy(fontFamily = fontFamily),
    )
}

internal val LocalAppTypography = staticCompositionLocalOf { appTypography(FontFamily.Default) }

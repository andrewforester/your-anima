package app.youranima.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Colour tokens from the Figma frame `astrology-home` (docs/design/astrology-home/SPEC.md). */
@Immutable
data class AppColors(
    val background: Color,
    val backgroundDeep: Color,
    val surface: Color,
    val outline: Color,
    val heroGradientTop: Color,
    val moon: Color,
    val primary: Color,
    val accentOrange: Color,
    val accentPink: Color,
    val accentTeal: Color,
    val accentLavender: Color,
    val accentPurple: Color,
    val accentGold: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val textSoft: Color,
    val glassFill: Color,
    val glassBorder: Color,
)

internal val DarkAppColors =
    AppColors(
        background = Color(0xFF0D0F2B),
        backgroundDeep = Color(0xFF0A0B21),
        surface = Color(0xFF1A1D42),
        outline = Color(0xFF262954),
        heroGradientTop = Color(0xFF18134F),
        moon = Color(0xFF353E7E),
        primary = Color(0xFF4D7CFF),
        accentOrange = Color(0xFFFF6B4A),
        accentPink = Color(0xFFFF52A3),
        accentTeal = Color(0xFF26D0CE),
        accentLavender = Color(0xFFA1A5DB),
        accentPurple = Color(0xFFB388FF),
        accentGold = Color(0xFFFFB84D),
        onSurface = Color(0xFFFFFFFF),
        onSurfaceMuted = Color(0xFF797C9B),
        textSoft = Color(0xFFEFEAEA),
        glassFill = Color.White.copy(alpha = 0.07f),
        glassBorder = Color.White.copy(alpha = 0.10f),
    )

internal val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

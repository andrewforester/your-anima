package app.youranima.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

/** App design system. Dark only: the design has no light variant. */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = DarkAppColors
    val fontFamily = geistFontFamily()
    val typography = remember(fontFamily) { appTypography(fontFamily) }
    val materialTypography = remember(fontFamily) { materialTypography(fontFamily) }
    val colorScheme =
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onSurface,
            background = colors.background,
            onBackground = colors.onSurface,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surface,
            onSurfaceVariant = colors.onSurfaceMuted,
            outline = colors.outline,
            outlineVariant = colors.outline,
            secondary = colors.accentLavender,
            tertiary = colors.accentPink,
            error = colors.accentOrange,
        )
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = materialTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current

val MaterialTheme.appTypography: AppTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTypography.current

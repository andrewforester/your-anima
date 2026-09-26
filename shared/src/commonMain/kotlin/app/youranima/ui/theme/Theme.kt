package app.youranima.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors =
    lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        background = Background,
        onBackground = OnBackground,
        surface = Background,
        onSurface = OnBackground,
    )

private val DarkColors =
    darkColorScheme(
        primary = PrimaryDark,
        onPrimary = OnPrimaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = BackgroundDark,
        onSurface = OnBackgroundDark,
    )

/** App-wide theme. All screens must be wrapped in it; never hardcode colors in screens. */
@Composable
fun AnimaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}

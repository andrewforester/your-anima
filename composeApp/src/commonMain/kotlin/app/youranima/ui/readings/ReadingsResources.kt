package app.youranima.ui.readings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.youranima.data.readings.ReadingArt
import app.youranima.resources.Res
import app.youranima.resources.home_ic_moon
import app.youranima.resources.home_ic_star
import app.youranima.resources.ic_heart
import app.youranima.resources.ic_user
import app.youranima.resources.readings_challenge_find_purpose
import app.youranima.resources.readings_love_hands_heart
import app.youranima.resources.readings_quiz_shaman_path
import app.youranima.resources.readings_quiz_witch_type
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.DrawableResource

/** Horizontal padding of the page content (carousels break out of it with the same content padding). */
internal val ScreenPadding = 16.dp

/** How a [ReadingArt] is drawn: a full-colour placeholder image or a single-colour line icon tinted in code. */
sealed interface ArtVisual {
    data class Image(
        val drawable: DrawableResource,
    ) : ArtVisual

    data class TintedIcon(
        val drawable: DrawableResource,
        val tint: Color,
    ) : ArtVisual
}

internal val ReadingArt.visual: ArtVisual
    @Composable @ReadOnlyComposable
    get() {
        val colors = MaterialTheme.appColors
        return when (this) {
            ReadingArt.Compass -> ArtVisual.Image(Res.drawable.readings_challenge_find_purpose)

            ReadingArt.WitchHat -> ArtVisual.Image(Res.drawable.readings_quiz_witch_type)

            ReadingArt.Flame -> ArtVisual.Image(Res.drawable.readings_quiz_shaman_path)

            ReadingArt.HandsHeart -> ArtVisual.Image(Res.drawable.readings_love_hands_heart)

            // TODO(theme): home_ic_moon / home_ic_star are shared now; rename to ic_* is a Theme task (SPEC Decision 13).
            ReadingArt.Moon -> ArtVisual.TintedIcon(Res.drawable.home_ic_moon, colors.accentPurple)

            ReadingArt.Star -> ArtVisual.TintedIcon(Res.drawable.home_ic_star, colors.accentGold)

            ReadingArt.Heart -> ArtVisual.TintedIcon(Res.drawable.ic_heart, colors.accentPink)

            ReadingArt.Spirit -> ArtVisual.TintedIcon(Res.drawable.ic_user, colors.accentLavender)
        }
    }

/** Theme + screen background for component previews. */
@Composable
internal fun ReadingsPreview(content: @Composable () -> Unit) {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            content()
        }
    }
}

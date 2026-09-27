package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.youranima.data.home.MoodCategory
import app.youranima.data.home.ZodiacSign
import app.youranima.resources.Res
import app.youranima.resources.home_mood_career
import app.youranima.resources.home_mood_family
import app.youranima.resources.home_mood_health
import app.youranima.resources.home_mood_love
import app.youranima.resources.home_sign_aquarius
import app.youranima.resources.home_sign_aries
import app.youranima.resources.home_sign_cancer
import app.youranima.resources.home_sign_capricorn
import app.youranima.resources.home_sign_gemini
import app.youranima.resources.home_sign_leo
import app.youranima.resources.home_sign_libra
import app.youranima.resources.home_sign_pisces
import app.youranima.resources.home_sign_sagittarius
import app.youranima.resources.home_sign_scorpio
import app.youranima.resources.home_sign_taurus
import app.youranima.resources.home_sign_virgo
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.StringResource

internal val ZodiacSign.label: StringResource
    get() =
        when (this) {
            ZodiacSign.Aries -> Res.string.home_sign_aries
            ZodiacSign.Taurus -> Res.string.home_sign_taurus
            ZodiacSign.Gemini -> Res.string.home_sign_gemini
            ZodiacSign.Cancer -> Res.string.home_sign_cancer
            ZodiacSign.Leo -> Res.string.home_sign_leo
            ZodiacSign.Virgo -> Res.string.home_sign_virgo
            ZodiacSign.Libra -> Res.string.home_sign_libra
            ZodiacSign.Scorpio -> Res.string.home_sign_scorpio
            ZodiacSign.Sagittarius -> Res.string.home_sign_sagittarius
            ZodiacSign.Capricorn -> Res.string.home_sign_capricorn
            ZodiacSign.Aquarius -> Res.string.home_sign_aquarius
            ZodiacSign.Pisces -> Res.string.home_sign_pisces
        }

internal val MoodCategory.label: StringResource
    get() =
        when (this) {
            MoodCategory.Career -> Res.string.home_mood_career
            MoodCategory.Love -> Res.string.home_mood_love
            MoodCategory.Health -> Res.string.home_mood_health
            MoodCategory.Family -> Res.string.home_mood_family
        }

internal val MoodCategory.color: Color
    @Composable @ReadOnlyComposable
    get() =
        when (this) {
            MoodCategory.Career -> MaterialTheme.appColors.primary
            MoodCategory.Love -> MaterialTheme.appColors.accentOrange
            MoodCategory.Health -> MaterialTheme.appColors.accentTeal
            MoodCategory.Family -> MaterialTheme.appColors.accentLavender
        }

/** Theme + screen background for component previews. */
@Composable
internal fun HomePreview(content: @Composable () -> Unit) {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            content()
        }
    }
}

package app.youranima.ui.psychics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.PsychicPhoto
import app.youranima.data.psychics.SectionIcon
import app.youranima.resources.Res
import app.youranima.resources.ic_briefcase
import app.youranima.resources.ic_heart
import app.youranima.resources.psychics_ic_target
import app.youranima.resources.psychics_photo_luna
import app.youranima.resources.psychics_photo_whimsy_lou
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.DrawableResource

/** Horizontal padding of the page content (carousels break out of it with the same content padding). */
internal val ScreenPadding = 16.dp

internal val SectionIcon.drawable: DrawableResource
    get() =
        when (this) {
            SectionIcon.Accurate -> Res.drawable.psychics_ic_target
            SectionIcon.Love -> Res.drawable.ic_heart
            SectionIcon.Career -> Res.drawable.ic_briefcase
        }

/** Love = orange and Career = primary, as in Focus & Mood and the home category cards. */
internal val SectionIcon.tint: Color
    @Composable @ReadOnlyComposable
    get() =
        when (this) {
            SectionIcon.Accurate -> MaterialTheme.appColors.accentPurple
            SectionIcon.Love -> MaterialTheme.appColors.accentOrange
            SectionIcon.Career -> MaterialTheme.appColors.primary
        }

internal val PsychicPhoto.drawable: DrawableResource
    get() =
        when (this) {
            PsychicPhoto.Luna -> Res.drawable.psychics_photo_luna
            PsychicPhoto.WhimsyLou -> Res.drawable.psychics_photo_whimsy_lou
        }

/** Theme + screen background for component previews. */
@Composable
internal fun PsychicsPreview(content: @Composable () -> Unit) {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            content()
        }
    }
}

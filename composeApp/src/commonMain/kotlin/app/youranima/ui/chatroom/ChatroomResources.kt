package app.youranima.ui.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.ChatAvatar
import app.youranima.resources.Res
import app.youranima.resources.chatroom_avatar_ana
import app.youranima.resources.chatroom_avatar_chandra
import app.youranima.resources.chatroom_avatar_esther_eclipse
import app.youranima.resources.chatroom_avatar_lyrienne
import app.youranima.resources.chatroom_avatar_madam_sarah
import app.youranima.resources.chatroom_avatar_miss_sheyna
import app.youranima.resources.chatroom_promo_avatar
import app.youranima.resources.psychics_photo_luna
import app.youranima.resources.psychics_photo_whimsy_lou
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.DrawableResource

/** Horizontal padding of the page content (promo card, chat rows). */
internal val ScreenPadding = 16.dp

internal val ChatAvatar.drawable: DrawableResource
    get() =
        when (this) {
            ChatAvatar.Chandra -> Res.drawable.chatroom_avatar_chandra
            ChatAvatar.EstherEclipse -> Res.drawable.chatroom_avatar_esther_eclipse
            ChatAvatar.Ana -> Res.drawable.chatroom_avatar_ana
            ChatAvatar.MissSheyna -> Res.drawable.chatroom_avatar_miss_sheyna
            ChatAvatar.MadamSarah -> Res.drawable.chatroom_avatar_madam_sarah
            ChatAvatar.Lyrienne -> Res.drawable.chatroom_avatar_lyrienne
            ChatAvatar.PromoFront -> Res.drawable.chatroom_promo_avatar
            ChatAvatar.Luna -> Res.drawable.psychics_photo_luna
            ChatAvatar.WhimsyLou -> Res.drawable.psychics_photo_whimsy_lou
        }

/** Theme + screen background for component previews. */
@Composable
internal fun ChatroomPreview(content: @Composable () -> Unit) {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            content()
        }
    }
}

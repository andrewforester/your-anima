package app.youranima.ui.compatibility

import app.youranima.data.compatibility.PersonAvatar
import app.youranima.resources.Res
import app.youranima.resources.home_avatar_character
import org.jetbrains.compose.resources.DrawableResource

internal val PersonAvatar.drawable: DrawableResource
    get() =
        when (this) {
            PersonAvatar.Character -> Res.drawable.home_avatar_character
        }

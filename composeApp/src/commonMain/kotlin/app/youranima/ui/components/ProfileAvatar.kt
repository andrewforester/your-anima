package app.youranima.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_avatar_character
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource

/** Transparent ring around the visible circle of [ProfileAvatar]. */
val ProfileAvatarInset = 4.dp

/**
 * The user's circular avatar: [size] box, [ProfileAvatarInset] transparent inset, then [painter] cropped into a
 * `background`-filled circle. Users: home profile header (100), Compatibility "You" (140).
 */
@Composable
fun ProfileAvatar(
    painter: Painter,
    size: Dp,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .size(size)
                .padding(ProfileAvatarInset)
                .background(MaterialTheme.appColors.background, CircleShape)
                .clip(CircleShape),
    )
}

@Preview
@Composable
private fun ProfileAvatarPreview() {
    AppTheme {
        ProfileAvatar(painterResource(Res.drawable.home_avatar_character), size = 140.dp, contentDescription = null)
    }
}

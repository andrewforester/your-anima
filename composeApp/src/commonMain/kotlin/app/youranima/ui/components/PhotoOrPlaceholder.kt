package app.youranima.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.ic_user
import app.youranima.resources.psychics_photo_luna
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource

/**
 * A psychic's photo (crop, top-aligned) filling [modifier]'s bounds, or, when [painter] is null, an `outline` fill
 * with a centred `ic_user` of [iconSize]. Callers clip it (Psychics card header full-bleed, Chatroom avatars circle).
 */
@Composable
fun PhotoOrPlaceholder(
    painter: Painter?,
    iconSize: Dp,
    modifier: Modifier = Modifier,
) {
    if (painter != null) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = modifier.fillMaxSize(),
        )
    } else {
        Box(modifier.fillMaxSize().background(MaterialTheme.appColors.outline), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(Res.drawable.ic_user),
                contentDescription = null,
                tint = MaterialTheme.appColors.onSurfaceMuted,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@Preview
@Composable
private fun PhotoOrPlaceholderPreview() {
    AppTheme {
        Row(
            modifier = Modifier.background(MaterialTheme.appColors.background).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PhotoOrPlaceholder(painterResource(Res.drawable.psychics_photo_luna), 24.dp, Modifier.size(48.dp).clip(CircleShape))
            PhotoOrPlaceholder(null, 24.dp, Modifier.size(48.dp).clip(CircleShape))
        }
    }
}

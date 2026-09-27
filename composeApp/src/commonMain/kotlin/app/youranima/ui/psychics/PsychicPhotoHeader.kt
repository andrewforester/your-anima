package app.youranima.ui.psychics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.Psychic
import app.youranima.data.psychics.PsychicPhoto
import app.youranima.resources.Res
import app.youranima.resources.ic_user
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource

private val PhotoHeight = 152.dp
private val GradientHeight = 56.dp

/** Full-bleed photo (or placeholder) with the status chip top-start and the name over a bottom fade into the card. */
@Composable
fun PsychicPhotoHeader(
    psychic: Psychic,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Box(modifier.fillMaxWidth().height(PhotoHeight)) {
        PhotoOrPlaceholder(psychic.photo)
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(GradientHeight)
                .background(Brush.verticalGradient(listOf(Color.Transparent, colors.surface))),
        )
        StatusChip(status = psychic.status, modifier = Modifier.align(Alignment.TopStart).padding(8.dp))
        Text(
            text = psychic.name,
            style = MaterialTheme.appTypography.cardTitle,
            color = colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        )
    }
}

@Composable
private fun PhotoOrPlaceholder(photo: PsychicPhoto?) {
    if (photo != null) {
        Image(
            painter = painterResource(photo.drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
        )
    } else {
        Box(Modifier.fillMaxSize().background(MaterialTheme.appColors.outline), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(Res.drawable.ic_user),
                contentDescription = null,
                tint = MaterialTheme.appColors.onSurfaceMuted,
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PsychicPhotoHeaderPreview() {
    PsychicsPreview {
        Box(Modifier.width(PsychicCardWidth)) {
            PsychicPhotoHeader(
                psychic =
                    PreviewPsychicsUiState.sections
                        .first()
                        .psychics
                        .first(),
            )
        }
    }
}

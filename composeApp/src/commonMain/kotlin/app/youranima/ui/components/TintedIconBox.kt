package app.youranima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.ic_heart
import app.youranima.resources.ic_message_circle
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource

/** Alpha of the tinted fill behind the icon. */
private const val TINT_FILL_ALPHA = 0.1f

/** Icon on a square container filled with its own [tint] at 10 % (reading card chat icon, psychics banner/section badges). */
@Composable
fun TintedIconBox(
    icon: Painter,
    tint: Color,
    size: Dp,
    iconSize: Dp,
    shape: Shape,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(size).background(tint.copy(alpha = TINT_FILL_ALPHA), shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(painter = icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(iconSize))
    }
}

@Preview
@Composable
private fun TintedIconBoxPreview() {
    AppTheme {
        Row(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            TintedIconBox(
                icon = painterResource(Res.drawable.ic_message_circle),
                tint = MaterialTheme.appColors.accentPink,
                size = 42.dp,
                iconSize = 20.dp,
                shape = MaterialTheme.shapes.medium,
                contentDescription = null,
            )
            TintedIconBox(
                icon = painterResource(Res.drawable.ic_heart),
                tint = MaterialTheme.appColors.accentOrange,
                size = 28.dp,
                iconSize = 16.dp,
                shape = RoundedCornerShape(8.dp),
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}

package app.youranima.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

// TODO(theme): card shadow `0 4 2 rgba(0,0,0,0.25)` from the home-feed SPEC has no token yet.
private val CardShadowColor = Color.Black.copy(alpha = 0.25f)

/** Card container from the Figma design: surface fill, 1dp outline, radius 20, drop shadow. */
@Composable
fun Modifier.appCard(padding: PaddingValues): Modifier {
    val shape = MaterialTheme.shapes.large
    return this
        .dropShadow(shape, Shadow(radius = 2.dp, offset = DpOffset(0.dp, 4.dp), color = CardShadowColor))
        .background(MaterialTheme.appColors.surface, shape)
        .border(BorderStroke(1.dp, MaterialTheme.appColors.outline), shape)
        .padding(padding)
}

@Preview
@Composable
private fun AppCardPreview() {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background).padding(16.dp)) {
            Box(Modifier.appCard(PaddingValues(16.dp)).size(120.dp))
        }
    }
}

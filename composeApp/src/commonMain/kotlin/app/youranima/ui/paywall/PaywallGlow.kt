package app.youranima.ui.paywall

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.youranima.ui.theme.appColors

/** Glow centre below the top of the status bar, and its radius. */
private val GlowCenterY = 170.dp
private val GlowRadius = 260.dp

private const val CORE_ALPHA = 0.30f
private const val RING_ALPHA = 0.70f
private const val RING_STOP = 0.45f

/**
 * Hero radial glow behind the top bar and the feature pager: `accentPurple` 30 % core → `cardGlow` 70 % →
 * transparent. Apply to the (edge-to-edge) content column before its status bar padding; it may overflow the bounds.
 */
@Composable
fun Modifier.paywallGlow(): Modifier {
    val colors = MaterialTheme.appColors
    val core = colors.accentPurple.copy(alpha = CORE_ALPHA)
    val ring = colors.cardGlow.copy(alpha = RING_ALPHA)
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    return drawBehind {
        val center = Offset(size.width / 2, (statusBarTop + GlowCenterY).toPx())
        val brush =
            Brush.radialGradient(
                0f to core,
                RING_STOP to ring,
                1f to Color.Transparent,
                center = center,
                radius = GlowRadius.toPx(),
            )
        drawCircle(brush = brush, radius = GlowRadius.toPx(), center = center)
    }
}

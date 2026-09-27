package app.youranima.ui.home

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/** Pixel metrics of [CollapsingProfileHeader] (Figma items 3–4); [top] is the status-bar inset. */
internal class HeaderMetrics(
    density: Density,
    val top: Int,
) {
    val height = with(density) { 56.dp.roundToPx() }
    val edge = with(density) { 20.dp.roundToPx() }
    val gap = with(density) { 12.dp.roundToPx() }
    val buttonSize = with(density) { TopBarButtonSize.roundToPx() }
    val sidePadding = with(density) { 24.dp.roundToPx() }
    val avatarTopPadding = with(density) { 12.dp.roundToPx() }
    val columnGap = with(density) { 16.dp.roundToPx() }
    val bottomPadding = with(density) { 24.dp.roundToPx() }

    /** Top of an item of [itemHeight] centred vertically in the bar. */
    fun centerInBar(itemHeight: Int) = top + (height - itemHeight) / 2
}

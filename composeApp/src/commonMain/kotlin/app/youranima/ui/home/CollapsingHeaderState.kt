package app.youranima.ui.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Scroll-driven state of the home header: the feed's [scrollState] and the collapse [fraction]
 * (0 = expanded profile header, 1 = compact bar) derived from it.
 * [CollapsingProfileHeader] reports its sizes here; [PinnedDateTabs] reads them to stick under the bar.
 * Read [fraction] and [scrollState] only in layout/draw lambdas, so scrolling doesn't recompose.
 */
@Stable
class CollapsingHeaderState(
    val scrollState: ScrollState,
) {
    /** Scroll distance over which the header collapses: expanded height − compact height, px. */
    internal var collapseRangePx by mutableIntStateOf(0)

    val fraction: Float
        get() = if (collapseRangePx <= 0) 0f else (scrollState.value.toFloat() / collapseRangePx).coerceIn(0f, 1f)

    /**
     * How far down an item must move to stay [gapPx] under the compact bar. [topInFeedPx] is its top in the feed
     * column that starts right under the expanded header, i.e. at `collapseRange + compactHeight` in scroll content.
     */
    internal fun pinShift(
        topInFeedPx: Int,
        gapPx: Int,
    ): Int = (scrollState.value - collapseRangePx + gapPx - topInFeedPx).coerceAtLeast(0)
}

@Composable
fun rememberCollapsingHeaderState(scrollState: ScrollState = rememberScrollState()): CollapsingHeaderState =
    remember(scrollState) { CollapsingHeaderState(scrollState) }

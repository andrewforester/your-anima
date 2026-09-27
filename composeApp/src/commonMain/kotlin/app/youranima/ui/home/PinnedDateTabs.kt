package app.youranima.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.youranima.data.home.ForecastPeriod
import app.youranima.ui.theme.appColors
import kotlin.math.roundToInt

/**
 * [DateTabs] that stick under the compact header bar once the feed scrolls them up to it.
 * Must be a direct child of the feed column placed right under [CollapsingProfileHeader], with a `zIndex`
 * above its siblings so the feed scrolls under it. While pinned it gets a solid background and swallows taps.
 */
@Composable
fun PinnedDateTabs(
    selected: ForecastPeriod,
    onSelect: (ForecastPeriod) -> Unit,
    headerState: CollapsingHeaderState,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.appColors.background
    var topInFeed by remember { mutableIntStateOf(0) }
    Box(modifier.fillMaxWidth().onPlaced { topInFeed = it.positionInParent().y.roundToInt() }) {
        Box(
            Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, headerState.pinShift(topInFeed, PinnedTabsPadding.roundToPx())) }
                .drawBehind {
                    if (headerState.pinShift(topInFeed, PinnedTabsPadding.roundToPx()) > 0) {
                        val padding = PinnedTabsPadding.toPx()
                        drawRect(background, Offset(0f, -padding), Size(size.width, size.height + 2 * padding))
                    }
                }.pointerInput(Unit) { awaitPointerEventScope { while (true) awaitPointerEvent() } }
                .padding(horizontal = 16.dp),
        ) {
            DateTabs(selected = selected, onSelect = onSelect)
        }
    }
}

/** Solid background above and below the pinned tabs; the gap between them and the compact bar. */
private val PinnedTabsPadding = 10.dp

@Preview
@Composable
private fun PinnedDateTabsPreview() {
    HomePreview {
        PinnedDateTabs(selected = ForecastPeriod.Today, onSelect = {}, headerState = rememberCollapsingHeaderState())
    }
}

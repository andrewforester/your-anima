package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import app.youranima.resources.Res
import app.youranima.resources.home_hero_background
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

// Hero art in the design: a 402x420 sky inside a 550x480 vector that overflows 70dp to the left
// (Figma inset 0 -19.4% -14.29% -17.41%). Scaled with the screen width.
private const val HERO_FRAME_WIDTH = 402f
private const val HERO_FRAME_HEIGHT = 420f
private const val HERO_ART_WIDTH = 550f
private const val HERO_ART_HEIGHT = 480f
private const val HERO_ART_LEFT = 70f

/** Sky art behind the top of the feed; scrolls away with it. */
@Composable
fun HeroBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.home_hero_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier =
            modifier.layout { measurable, constraints ->
                val width = constraints.maxWidth
                val scale = width / HERO_FRAME_WIDTH
                val placeable =
                    measurable.measure(
                        Constraints.fixed(
                            (HERO_ART_WIDTH * scale).roundToInt(),
                            (HERO_ART_HEIGHT * scale).roundToInt(),
                        ),
                    )
                layout(width, (HERO_FRAME_HEIGHT * scale).roundToInt()) {
                    placeable.place(-(HERO_ART_LEFT * scale).roundToInt(), 0)
                }
            },
    )
}

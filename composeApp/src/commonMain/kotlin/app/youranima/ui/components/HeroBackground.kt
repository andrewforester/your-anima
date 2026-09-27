package app.youranima.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_hero_background
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

// Hero art in the design: a 402x420 sky inside a 550x480 vector that overflows 70dp to the left
// (Figma inset 0 -19.4% -14.29% -17.41%). Scaled with the screen width.
private const val HERO_FRAME_WIDTH = 402f
private const val HERO_FRAME_HEIGHT = 420f
private const val HERO_ART_WIDTH = 550f
private const val HERO_ART_HEIGHT = 480f
private const val HERO_ART_LEFT = 70f

/** Sky art (gradient, moon, sparkles) behind the top of a tab; scrolls away with it. Users: home, Compatibility. */
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

/**
 * Places the element at ([left], [top]) with size [width] x [height], all in the 402-wide hero frame units,
 * scaled with the available width like [HeroBackground]. Use it on a sibling of [HeroBackground] in the same `Box`.
 */
fun Modifier.inHeroFrame(
    left: Float,
    top: Float,
    width: Float,
    height: Float,
): Modifier =
    layout { measurable, constraints ->
        val scale = constraints.maxWidth / HERO_FRAME_WIDTH
        val placeable = measurable.measure(Constraints.fixed((width * scale).roundToInt(), (height * scale).roundToInt()))
        layout(constraints.maxWidth, ((top + height) * scale).roundToInt()) {
            placeable.place((left * scale).roundToInt(), (top * scale).roundToInt())
        }
    }

@Preview
@Composable
private fun HeroBackgroundPreview() {
    AppTheme {
        Box(Modifier.size(402.dp, 500.dp).background(MaterialTheme.appColors.background)) {
            HeroBackground()
        }
    }
}

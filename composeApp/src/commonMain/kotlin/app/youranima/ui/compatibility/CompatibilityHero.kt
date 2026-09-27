package app.youranima.ui.compatibility

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.youranima.resources.Res
import app.youranima.resources.compatibility_comets
import app.youranima.ui.components.HeroBackground
import app.youranima.ui.components.inHeroFrame
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource

/** The shared hero sky with the two comets on top (160x80 at (228, 188) of the hero frame); [content] is drawn over both. */
@Composable
fun CompatibilityHero(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxWidth()) {
        HeroBackground()
        Image(
            painter = painterResource(Res.drawable.compatibility_comets),
            contentDescription = null,
            modifier = Modifier.inHeroFrame(left = 228f, top = 188f, width = 160f, height = 80f),
        )
        content()
    }
}

@Preview
@Composable
private fun CompatibilityHeroPreview() {
    AppTheme {
        CompatibilityHero(Modifier.background(MaterialTheme.appColors.background)) {}
    }
}

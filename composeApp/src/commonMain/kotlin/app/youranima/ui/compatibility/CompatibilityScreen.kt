package app.youranima.ui.compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.compatibility.CompatibilityRepository
import app.youranima.data.compatibility.MockCompatibilityRepository
import app.youranima.resources.Res
import app.youranima.resources.compatibility_headline
import app.youranima.resources.compatibility_subtitle
import app.youranima.resources.compatibility_title
import app.youranima.ui.components.ScreenTopBar
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

/** Height of the shared [ScreenTopBar]. */
private val TopBarHeight = 56.dp

/** Height of the shared bottom bar drawn by [app.youranima.ui.navigation.AppShell] (without the navigation bar inset). */
private val BottomBarHeight = 84.dp

private const val GLOW_ALPHA = 0.5f
private const val GLOW_CENTER_X = 0.55f
private const val GLOW_RADIUS = 0.8f

/** Glow centre above the top edge of the bottom bar. */
private val GlowLift = 40.dp

/** Stateful entry point: loads the pair once; [onAddPartner] is where the add-partner flow will plug in. */
@Composable
fun CompatibilityScreen(
    modifier: Modifier = Modifier,
    repository: CompatibilityRepository = MockCompatibilityRepository,
    onAddPartner: () -> Unit = {},
) {
    val state = remember(repository) { repository.compatibilityPair().toUiState() }
    CompatibilityScreen(state = state, onAddPartner = onAddPartner, modifier = modifier)
}

/**
 * Hero sky with comets, top bar, then the headline and the You + Partner row, centred between the top bar and the
 * bottom bar. The page scrolls only when that doesn't fit; the page fill and the bottom glow stay put.
 */
@Composable
fun CompatibilityScreen(
    state: CompatibilityUiState,
    onAddPartner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(colors.background)
            .bottomGlow(colors.cardGlow.copy(alpha = GLOW_ALPHA))
            .testTag(CompatibilityScreenTags.SCREEN),
    ) {
        val statusBar = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val navigationBar = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val bodyMinHeight = (maxHeight - statusBar - TopBarHeight - BottomBarHeight - navigationBar).coerceAtLeast(0.dp)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            CompatibilityHero {
                Column(Modifier.statusBarsPadding()) {
                    ScreenTopBar(
                        title = stringResource(Res.string.compatibility_title),
                        titleModifier = Modifier.testTag(CompatibilityScreenTags.TITLE),
                    )
                    Box(Modifier.fillMaxWidth().heightIn(min = bodyMinHeight), contentAlignment = Alignment.Center) {
                        CompatibilityBody(state = state, onAddPartner = onAddPartner)
                    }
                    Spacer(Modifier.height(BottomBarHeight).navigationBarsPadding())
                }
            }
        }
    }
}

@Composable
private fun CompatibilityBody(
    state: CompatibilityUiState,
    onAddPartner: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val typography = MaterialTheme.appTypography
        val colors = MaterialTheme.appColors
        IntroText(stringResource(Res.string.compatibility_headline), typography.cardTitle, colors.onSurface)
        IntroText(
            text = stringResource(Res.string.compatibility_subtitle),
            style = typography.body,
            color = colors.accentLavender,
            modifier = Modifier.padding(top = 8.dp),
        )
        PairRow(
            user = state.user,
            partner = state.partner,
            onAddPartner = onAddPartner,
            modifier = Modifier.padding(top = 40.dp),
        )
    }
}

@Composable
private fun IntroText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = style,
        color = color,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(horizontal = 24.dp),
    )
}

/** Radial [color] → transparent glow just above the bottom bar, fixed to the screen. */
private fun Modifier.bottomGlow(color: Color): Modifier =
    drawBehind {
        drawRect(
            Brush.radialGradient(
                colors = listOf(color, color.copy(alpha = 0f)),
                center = Offset(size.width * GLOW_CENTER_X, size.height - (BottomBarHeight + GlowLift).toPx()),
                radius = size.width * GLOW_RADIUS,
            ),
        )
    }

@Preview
@Composable
private fun CompatibilityScreenPreview() {
    AppTheme { CompatibilityScreen(state = PreviewCompatibilityUiState, onAddPartner = {}) }
}

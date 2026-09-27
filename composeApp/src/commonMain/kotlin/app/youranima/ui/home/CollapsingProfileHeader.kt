package app.youranima.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.lerp
import app.youranima.data.home.UserProfile
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography

/**
 * Top bar + profile header that collapse into one compact bar as the feed scrolls.
 * One layout for both states: [CollapsingHeaderState.fraction] (0→1) interpolates the avatar (100→36, centre→left),
 * the name (24→16, under the avatar→right of it) and the zodiac row (→ subtitle line). Add-story, the avatar thumb
 * and Birth Chart fade out. The node keeps its expanded height so the feed below doesn't move; the caller pins it to
 * the top of the viewport. Only the visible part gets the background (transparent → solid) and swallows taps.
 */
@Composable
fun CollapsingProfileHeader(
    user: UserProfile,
    state: CollapsingHeaderState,
    modifier: Modifier = Modifier,
    onAddStoryClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBirthChartClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    val statusBarTop = WindowInsets.statusBars.getTop(LocalDensity.current)
    val nameCompactScale = typography.cardTitle.fontSize.value / typography.name.fontSize.value
    Layout(
        content = {
            Box(
                Modifier
                    .layoutId(HeaderPart.Background)
                    .drawBehind { drawRect(colors.background.copy(alpha = state.fraction)) }
                    .pointerInput(Unit) { awaitPointerEventScope { while (true) awaitPointerEvent() } },
            )
            AddStoryButton(onClick = onAddStoryClick, modifier = Modifier.layoutId(HeaderPart.AddStory))
            AvatarThumb(onClick = onAvatarClick, modifier = Modifier.layoutId(HeaderPart.Thumb))
            SettingsButton(onClick = onSettingsClick, modifier = Modifier.layoutId(HeaderPart.Settings))
            BirthChartPill(onClick = onBirthChartClick, modifier = Modifier.layoutId(HeaderPart.Pill))
            HomeProfileAvatar(Modifier.layoutId(HeaderPart.Avatar))
            ProfileName(name = user.name, modifier = Modifier.layoutId(HeaderPart.Name))
            ZodiacRow(
                user = user,
                modifier = Modifier.layoutId(HeaderPart.Zodiac),
                labelColor = { lerp(colors.accentLavender, colors.onSurfaceMuted, state.fraction) },
            )
        },
        modifier = modifier,
    ) { measurables, constraints ->
        fun part(id: HeaderPart): Measurable = measurables.first { it.layoutId == id }
        val f = state.fraction
        val width = constraints.maxWidth
        val loose = Constraints(maxWidth = width)
        val bar = HeaderMetrics(this, statusBarTop)
        val settings = part(HeaderPart.Settings).measure(loose)
        val addStory = part(HeaderPart.AddStory).measure(loose)
        val thumb = part(HeaderPart.Thumb).measure(loose)
        val pill = part(HeaderPart.Pill).measure(loose)
        val avatar = part(HeaderPart.Avatar).measure(loose)
        val textStart = bar.edge + bar.buttonSize + bar.gap
        val expandedTextWidth = width - 2 * bar.sidePadding
        val compactTextWidth = width - textStart - bar.gap - settings.width - bar.edge
        val name = part(HeaderPart.Name).measure(loose.textWidth(lerp(expandedTextWidth, (compactTextWidth / nameCompactScale).toInt(), f)))
        val zodiac = part(HeaderPart.Zodiac).measure(loose.textWidth(lerp(expandedTextWidth, compactTextWidth, f)))

        // Expanded: bar row, then avatar / name / zodiac / pill centred in a column (Figma items 3–4).
        val avatarTop = bar.top + bar.height + bar.avatarTopPadding
        val nameTop = avatarTop + avatar.height + bar.columnGap
        val zodiacTop = nameTop + name.height + bar.columnGap
        val pillTop = zodiacTop + zodiac.height + bar.columnGap
        val expandedHeight = pillTop + pill.height + bar.bottomPadding
        val compactHeight = bar.top + bar.height
        state.collapseRangePx = expandedHeight - compactHeight

        // Compact: avatar at the left of the bar, name over the zodiac subtitle to its right.
        val compactNameHeight = (name.height * nameCompactScale).toInt()
        val compactTextTop = bar.top + (bar.height - compactNameHeight - zodiac.height) / 2
        val visibleHeight = lerp(expandedHeight, compactHeight, f)
        val fade = (1f - f / FADE_END).coerceIn(0f, 1f)
        val zodiacY = lerp(zodiacTop, compactTextTop + compactNameHeight, f)
        val background = part(HeaderPart.Background).measure(Constraints.fixed(width, visibleHeight))

        layout(width, expandedHeight) {
            background.place(0, 0)
            if (fade > 0f) {
                addStory.placeWithLayer(bar.edge, bar.centerInBar(addStory.height)) { alpha = fade }
                thumb.placeWithLayer(bar.edge + bar.buttonSize + bar.gap, bar.centerInBar(thumb.height)) { alpha = fade }
                // Follows the zodiac row while it fades.
                pill.placeWithLayer((width - pill.width) / 2, zodiacY + zodiac.height + bar.columnGap) { alpha = fade }
            }
            settings.place(width - bar.edge - settings.width, bar.centerInBar(settings.height))
            // Eased out: the avatar shrinks ahead of the name, so the name never slides under it.
            val avatarF = 1f - (1f - f) * (1f - f)
            val avatarScale = lerp(1f, bar.buttonSize.toFloat() / avatar.width, avatarF)
            avatar.placeWithLayer(
                x = lerp((width - avatar.width) / 2, bar.edge, avatarF),
                y = lerp(avatarTop, bar.centerInBar(bar.buttonSize), avatarF),
            ) {
                scaleX = avatarScale
                scaleY = avatarScale
                transformOrigin = TransformOrigin(0f, 0f)
            }
            val nameScale = lerp(1f, nameCompactScale, f)
            name.placeWithLayer(
                x = lerp((width - name.width) / 2, textStart, f),
                y = lerp(nameTop, compactTextTop, f),
            ) {
                scaleX = nameScale
                scaleY = nameScale
                transformOrigin = TransformOrigin(0f, 0f)
            }
            zodiac.place(x = lerp((width - zodiac.width) / 2, textStart, f), y = zodiacY)
        }
    }
}

private enum class HeaderPart { Background, AddStory, Thumb, Settings, Pill, Avatar, Name, Zodiac }

/** Collapse fraction at which the disappearing elements are fully transparent. */
private const val FADE_END = 0.3f

private fun Constraints.textWidth(maxWidth: Int) = copy(maxWidth = maxWidth.coerceAtLeast(0))

@Preview
@Composable
private fun CollapsingProfileHeaderPreview() {
    AppTheme {
        CollapsingProfileHeader(user = PreviewHomeUiState.user, state = rememberCollapsingHeaderState())
    }
}

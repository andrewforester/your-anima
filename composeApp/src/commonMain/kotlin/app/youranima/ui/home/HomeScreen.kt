package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.HomeRepository
import app.youranima.data.home.MockHomeRepository
import app.youranima.data.home.ReadingOffer
import app.youranima.ui.components.HeroBackground
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/**
 * Stateful entry point: loads the data and keeps the date-tab selection. A tap on a locked category card calls
 * [onLockedClick] (the paywall); unlocked cards do nothing yet.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    repository: HomeRepository = MockHomeRepository,
    onLockedClick: () -> Unit = {},
) {
    val data = remember(repository) { repository.homeData() }
    var period by rememberSaveable { mutableStateOf(ForecastPeriod.Today) }
    HomeScreen(
        state = data.toUiState(period),
        onPeriodSelect = { period = it },
        modifier = modifier,
        onCategoryClick = { id -> if (data.categories.any { it.id == id && it.isLocked }) onLockedClick() },
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onPeriodSelect: (ForecastPeriod) -> Unit,
    modifier: Modifier = Modifier,
    headerState: CollapsingHeaderState = rememberCollapsingHeaderState(),
    onAddStoryClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBirthChartClick: () -> Unit = {},
    onAskClick: (ReadingOffer) -> Unit = {},
    onMoodInfoClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onTarotClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(HomeScreenTags.SCREEN)
                .verticalScroll(headerState.scrollState),
    ) {
        Box {
            HeroBackground()
            Column {
                // Pinned to the top of the viewport; above the feed, which scrolls under it.
                CollapsingProfileHeader(
                    user = state.user,
                    state = headerState,
                    modifier = Modifier.zIndex(2f).offset { IntOffset(0, headerState.scrollState.value) },
                    onAddStoryClick = onAddStoryClick,
                    onAvatarClick = onAvatarClick,
                    onSettingsClick = onSettingsClick,
                    onBirthChartClick = onBirthChartClick,
                )
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = ContentBottomPadding)
                            .padding(WindowInsets.navigationBars.asPaddingValues()),
                    verticalArrangement = Arrangement.spacedBy(ContentGap),
                ) {
                    state.readings.forEach { offer ->
                        ReadingCard(offer = offer, onAskClick = { onAskClick(offer) }, modifier = CardPadding)
                    }
                    PinnedDateTabs(
                        selected = state.selectedPeriod,
                        onSelect = onPeriodSelect,
                        headerState = headerState,
                        modifier = Modifier.zIndex(1f),
                    )
                    FocusMoodCard(scores = state.mood, onInfoClick = onMoodInfoClick, modifier = CardPadding)
                    // Full width: the row scrolls under the screen edges.
                    CategoryRow(categories = state.categories, onCategoryClick = onCategoryClick)
                    TipCard(tip = state.tipOfTheDay, modifier = CardPadding)
                    YesNoCard(yes = state.yesForToday, no = state.noForToday, modifier = CardPadding)
                    TarotCard(onClick = onTarotClick, modifier = CardPadding)
                }
            }
        }
    }
}

/** Gap between the blocks of the feed. */
private val ContentGap = 20.dp

/** Side margins of the feed cards. */
private val CardPadding = Modifier.padding(horizontal = 16.dp)

/** Clearance under the scrollable content for the shared bottom bar drawn by [app.youranima.ui.navigation.AppShell]. */
private val ContentBottomPadding = 100.dp

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(state = PreviewHomeUiState, onPeriodSelect = {})
    }
}

/** Compact state: scrolled to the end, header collapsed, date tabs pinned. */
@Preview
@Composable
private fun HomeScreenCompactPreview() {
    AppTheme {
        HomeScreen(
            state = PreviewHomeUiState,
            onPeriodSelect = {},
            headerState = rememberCollapsingHeaderState(rememberScrollState(Int.MAX_VALUE)),
        )
    }
}

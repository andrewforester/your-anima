package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.HomeRepository
import app.youranima.data.home.MockHomeRepository
import app.youranima.data.home.ReadingOffer
import app.youranima.resources.Res
import app.youranima.resources.home_hero_background
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

/** Stateful entry point: loads the data and keeps the tab / nav selection. */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    repository: HomeRepository = MockHomeRepository,
) {
    val data = remember(repository) { repository.homeData() }
    var period by rememberSaveable { mutableStateOf(ForecastPeriod.Today) }
    var navItem by rememberSaveable { mutableStateOf(HomeNavItem.Today) }
    HomeScreen(
        state = data.toUiState(period, navItem),
        onPeriodSelect = { period = it },
        onNavSelect = { navItem = it },
        modifier = modifier,
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onPeriodSelect: (ForecastPeriod) -> Unit,
    onNavSelect: (HomeNavItem) -> Unit,
    modifier: Modifier = Modifier,
    onAddStoryClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBirthChartClick: () -> Unit = {},
    onAskClick: (ReadingOffer) -> Unit = {},
    onMoodInfoClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onTarotClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(HomeScreenTags.SCREEN),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            Box {
                HeroBackground()
                Column(Modifier.statusBarsPadding()) {
                    TopBar(
                        onAddStoryClick = onAddStoryClick,
                        onAvatarClick = onAvatarClick,
                        onSettingsClick = onSettingsClick,
                    )
                    ProfileHeader(user = state.user, onBirthChartClick = onBirthChartClick)
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = ContentBottomPadding)
                                .padding(WindowInsets.navigationBars.asPaddingValues()),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            state.readings.forEach { offer ->
                                ReadingCard(offer = offer, onAskClick = { onAskClick(offer) })
                            }
                            DateTabs(selected = state.selectedPeriod, onSelect = onPeriodSelect)
                            FocusMoodCard(scores = state.mood, onInfoClick = onMoodInfoClick)
                        }
                        Spacer(Modifier.height(32.dp))
                        // Full width: the row scrolls under the screen edges.
                        CategoryRow(categories = state.categories, onCategoryClick = onCategoryClick)
                        Spacer(Modifier.height(32.dp))
                        TipCard(tip = state.tipOfTheDay, modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(Modifier.height(36.dp))
                        YesNoBlock(kind = YesNoKind.Yes, items = state.yesForToday)
                        Spacer(Modifier.height(32.dp))
                        YesNoBlock(kind = YesNoKind.No, items = state.noForToday)
                        Spacer(Modifier.height(32.dp))
                        TarotCard(onClick = onTarotClick, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
        HomeBottomBar(
            selected = state.selectedNavItem,
            badges = state.badges,
            onSelect = onNavSelect,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

/** Clearance under the scrollable content for the bottom bar. */
private val ContentBottomPadding = 100.dp

// Hero art in the design: a 402x420 sky inside a 550x480 vector that overflows 70dp to the left
// (Figma inset 0 -19.4% -14.29% -17.41%). Scaled with the screen width.
private const val HERO_FRAME_WIDTH = 402f
private const val HERO_FRAME_HEIGHT = 420f
private const val HERO_ART_WIDTH = 550f
private const val HERO_ART_HEIGHT = 480f
private const val HERO_ART_LEFT = 70f

@Composable
private fun HeroBackground() {
    Image(
        painter = painterResource(Res.drawable.home_hero_background),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier =
            Modifier.layout { measurable, constraints ->
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

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(state = PreviewHomeUiState, onPeriodSelect = {}, onNavSelect = {})
    }
}

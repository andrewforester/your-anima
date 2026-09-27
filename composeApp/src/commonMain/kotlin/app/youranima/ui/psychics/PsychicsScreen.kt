package app.youranima.ui.psychics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.MockPsychicsRepository
import app.youranima.data.psychics.PsychicsRepository
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/** Gap between the blocks of the content column (as the home feed). */
private val ContentGap = 20.dp

/** Clearance under the content for the shared bottom bar drawn by [app.youranima.ui.navigation.AppShell]. */
private val ContentBottomPadding = 100.dp

/** Stateful entry point: loads the data. The filter is static this round ("All"), so there is no state to keep yet. */
@Composable
fun PsychicsScreen(
    modifier: Modifier = Modifier,
    repository: PsychicsRepository = MockPsychicsRepository,
) {
    val state = remember(repository) { repository.psychicsData().toUiState() }
    PsychicsScreen(state = state, modifier = modifier)
}

@Composable
fun PsychicsScreen(
    state: PsychicsUiState,
    modifier: Modifier = Modifier,
    onFavouritesClick: () -> Unit = {},
    onFilterSelect: (PsychicFilter) -> Unit = {},
    onViewAllClick: (sectionId: String) -> Unit = {},
    onCallClick: (psychicId: String) -> Unit = {},
    onChatClick: (psychicId: String) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(PsychicsScreenTags.SCREEN)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding(),
    ) {
        PsychicsTopBar(onFavouritesClick = onFavouritesClick)
        Column(
            modifier =
                Modifier
                    .padding(top = 4.dp, bottom = ContentBottomPadding)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
            verticalArrangement = Arrangement.spacedBy(ContentGap),
        ) {
            PromoBanner(promo = state.promo, modifier = Modifier.padding(horizontal = ScreenPadding))
            PsychicFilterRow(
                selected = state.selectedFilter,
                onSelect = onFilterSelect,
                modifier = Modifier.padding(horizontal = ScreenPadding),
            )
            state.sections.forEach { section ->
                PsychicsSection(
                    section = section,
                    onViewAllClick = { onViewAllClick(section.id) },
                    onCallClick = onCallClick,
                    onChatClick = onChatClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PsychicsScreenPreview() {
    AppTheme { PsychicsScreen(state = PreviewPsychicsUiState) }
}

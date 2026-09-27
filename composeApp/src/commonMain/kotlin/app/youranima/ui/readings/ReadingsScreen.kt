package app.youranima.ui.readings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.readings.MockReadingsRepository
import app.youranima.data.readings.ReadingsRepository
import app.youranima.resources.Res
import app.youranima.resources.readings_title
import app.youranima.ui.components.ScreenTopBar
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

/** Gap between the sections (as the home feed). */
private val ContentGap = 20.dp

/** Clearance under the content for the shared bottom bar drawn by [app.youranima.ui.navigation.AppShell]. */
private val ContentBottomPadding = 100.dp

/** Stateful entry point: loads the data once. Nothing is interactive this round (#42), so there is no state to keep. */
@Composable
fun ReadingsScreen(
    modifier: Modifier = Modifier,
    repository: ReadingsRepository = MockReadingsRepository,
) {
    val state = remember(repository) { repository.readingsContent().toUiState() }
    ReadingsScreen(state = state, modifier = modifier)
}

/** Top bar, the challenges carousel and one carousel per quiz section; visual only, no callbacks yet. */
@Composable
fun ReadingsScreen(
    state: ReadingsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(ReadingsScreenTags.SCREEN)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding(),
    ) {
        ScreenTopBar(
            title = stringResource(Res.string.readings_title),
            titleModifier = Modifier.testTag(ReadingsScreenTags.TITLE),
        )
        Column(
            modifier =
                Modifier
                    .padding(top = 4.dp, bottom = ContentBottomPadding)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
            verticalArrangement = Arrangement.spacedBy(ContentGap),
        ) {
            val challenges = state.challengeSection
            ReadingsSection(id = challenges.id, title = challenges.title) {
                items(challenges.challenges, key = { it.id }) { ChallengeCard(it) }
            }
            state.quizSections.forEach { section ->
                ReadingsSection(id = section.id, title = section.title) {
                    items(section.quizzes, key = { it.id }) { QuizCard(it) }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReadingsScreenPreview() {
    AppTheme { ReadingsScreen(state = PreviewReadingsUiState) }
}

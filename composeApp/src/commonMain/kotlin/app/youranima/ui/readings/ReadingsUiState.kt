package app.youranima.ui.readings

import androidx.compose.runtime.Immutable
import app.youranima.data.readings.ChallengeSection
import app.youranima.data.readings.MockReadingsRepository
import app.youranima.data.readings.QuizSection
import app.youranima.data.readings.ReadingsContent

/** What the Readings tab renders. Visual only this round (#42): no selection or loading state yet. */
@Immutable
data class ReadingsUiState(
    val challengeSection: ChallengeSection,
    val quizSections: List<QuizSection>,
)

fun ReadingsContent.toUiState() =
    ReadingsUiState(
        challengeSection = challengeSection,
        quizSections = quizSections,
    )

internal val PreviewReadingsUiState = MockReadingsRepository.readingsContent().toUiState()

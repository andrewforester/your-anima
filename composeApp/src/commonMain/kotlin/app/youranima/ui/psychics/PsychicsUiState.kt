package app.youranima.ui.psychics

import androidx.compose.runtime.Immutable
import app.youranima.data.psychics.FreeMinutesPromo
import app.youranima.data.psychics.MockPsychicsRepository
import app.youranima.data.psychics.PsychicSection
import app.youranima.data.psychics.PsychicsData

/** All / Call / Chat filter. Static this round: only [All] is ever selected and nothing is filtered (Issue #36). */
enum class PsychicFilter { All, Call, Chat }

@Immutable
data class PsychicsUiState(
    val promo: FreeMinutesPromo,
    val selectedFilter: PsychicFilter,
    val sections: List<PsychicSection>,
)

fun PsychicsData.toUiState(selectedFilter: PsychicFilter = PsychicFilter.All) =
    PsychicsUiState(
        promo = promo,
        selectedFilter = selectedFilter,
        sections = sections,
    )

internal val PreviewPsychicsUiState = MockPsychicsRepository.psychicsData().toUiState()

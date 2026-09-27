package app.youranima.ui.compatibility

import androidx.compose.runtime.Immutable
import app.youranima.data.compatibility.CompatibilityPair
import app.youranima.data.compatibility.CompatibilityPerson
import app.youranima.data.compatibility.MockCompatibilityRepository

/** What the Compatibility tab renders. [partner] `null` = empty slot with the "+" button. */
@Immutable
data class CompatibilityUiState(
    val user: CompatibilityPerson,
    val partner: CompatibilityPerson?,
)

fun CompatibilityPair.toUiState() = CompatibilityUiState(user = user, partner = partner)

internal val PreviewCompatibilityUiState = MockCompatibilityRepository.compatibilityPair().toUiState()

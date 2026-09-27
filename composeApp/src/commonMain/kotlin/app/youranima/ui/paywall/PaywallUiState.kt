package app.youranima.ui.paywall

import androidx.compose.runtime.Immutable
import app.youranima.data.paywall.MockPaywallRepository
import app.youranima.data.paywall.PaywallData
import app.youranima.data.paywall.PremiumFeature
import app.youranima.data.paywall.SubscriptionPlan

/** What the paywall renders: the feature slides, the plans and which plan is selected. */
@Immutable
data class PaywallUiState(
    val features: List<PremiumFeature>,
    val plans: List<SubscriptionPlan>,
    val selectedPlanId: String,
) {
    val selectedPlan: SubscriptionPlan
        get() = plans.first { it.id == selectedPlanId }
}

fun PaywallData.toUiState(selectedPlanId: String = defaultPlanId) =
    PaywallUiState(
        features = features,
        plans = plans,
        selectedPlanId = selectedPlanId,
    )

internal val PreviewPaywallUiState = MockPaywallRepository.paywallData().toUiState()

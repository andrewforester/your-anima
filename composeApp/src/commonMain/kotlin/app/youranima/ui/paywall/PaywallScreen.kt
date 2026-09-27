package app.youranima.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.paywall.MockPaywallRepository
import app.youranima.data.paywall.PaywallRepository
import app.youranima.resources.Res
import app.youranima.resources.paywall_cancel_anytime
import app.youranima.resources.paywall_subscribe
import app.youranima.ui.components.PrimaryButton
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

private val SubscribeHeight = 48.dp

/** Stateful entry point: loads the offer and keeps the selected plan (default from the repository). */
@Composable
fun PaywallScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    repository: PaywallRepository = MockPaywallRepository,
) {
    val data = remember(repository) { repository.paywallData() }
    var selectedPlanId by rememberSaveable { mutableStateOf(data.defaultPlanId) }
    PaywallScreen(
        state = data.toUiState(selectedPlanId),
        onPlanSelect = { selectedPlanId = it },
        onClose = onClose,
        modifier = modifier,
    )
}

/** Full-screen "Anima Premium" paywall. Subscribe, Restore and the links are hoisted (no-ops for now). */
@Composable
fun PaywallScreen(
    state: PaywallUiState,
    onPlanSelect: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    onRestore: () -> Unit = {},
    onSubscribe: (String) -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onSubscriptionTermsClick: () -> Unit = {},
) {
    val pagerState = rememberPagerState { state.features.size }
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(PaywallScreenTags.SCREEN)
                .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .paywallGlow()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PaywallTopBar(onClose = onClose, onRestore = onRestore)
            FeaturePager(features = state.features, pagerState = pagerState)
            Spacer(Modifier.height(16.dp))
            PageDots(count = state.features.size, current = pagerState.currentPage)
            Spacer(Modifier.height(32.dp))
            PlanRow(plans = state.plans, selectedPlanId = state.selectedPlanId, onPlanSelect = onPlanSelect)
            Spacer(Modifier.height(20.dp))
            LegalText(plan = state.selectedPlan)
            Spacer(Modifier.height(20.dp))
            PaywallFooter(
                onSubscribe = { onSubscribe(state.selectedPlanId) },
                onTermsClick = onTermsClick,
                onPrivacyClick = onPrivacyClick,
                onSubscriptionTermsClick = onSubscriptionTermsClick,
            )
        }
    }
}

/** Subscribe CTA, "Cancel anytime. Recurring billing." and the legal links. */
@Composable
private fun PaywallFooter(
    onSubscribe: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSubscriptionTermsClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PrimaryButton(
            label = stringResource(Res.string.paywall_subscribe),
            onClick = onSubscribe,
            height = SubscribeHeight,
            modifier = Modifier.fillMaxWidth().padding(horizontal = ScreenPadding).testTag(PaywallScreenTags.SUBSCRIBE),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.paywall_cancel_anytime),
            style = MaterialTheme.appTypography.body,
            color = MaterialTheme.appColors.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Spacer(Modifier.height(4.dp))
        LegalLinks(
            onTermsClick = onTermsClick,
            onPrivacyClick = onPrivacyClick,
            onSubscriptionTermsClick = onSubscriptionTermsClick,
        )
    }
}

@Preview
@Composable
private fun PaywallScreenPreview() {
    AppTheme {
        PaywallScreen(state = PreviewPaywallUiState, onPlanSelect = {}, onClose = {})
    }
}

package app.youranima.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.paywall.PlanBadge
import app.youranima.data.paywall.SubscriptionPlan
import app.youranima.resources.Res
import app.youranima.resources.paywall_hot_deal
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

/** Visible height of the badge tab above a card; every card is pushed down by it so all three share one top. */
private val TabHeight = 20.dp
private val TabShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)

/** The three plan cards side by side (one selected, radio group); a plan with a badge gets its tab above the card. */
@Composable
fun PlanRow(
    plans: List<SubscriptionPlan>,
    selectedPlanId: String,
    onPlanSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = ScreenPadding).selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        plans.forEach { plan ->
            Box(Modifier.weight(1f)) {
                if (plan.badge == PlanBadge.HotDeal) HotDealTab()
                PlanCard(
                    plan = plan,
                    selected = plan.id == selectedPlanId,
                    onClick = { onPlanSelect(plan.id) },
                    modifier = Modifier.fillMaxWidth().padding(top = TabHeight),
                )
            }
        }
    }
}

/** Orange tab behind the top of a card: its lower half is covered by the card, the label sits in the visible top. */
@Composable
private fun HotDealTab(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(TabHeight * 2)
                .background(MaterialTheme.appColors.accentOrange, TabShape)
                .testTag(PaywallScreenTags.HOT_DEAL),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(Modifier.height(TabHeight), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(Res.string.paywall_hot_deal),
                style = MaterialTheme.appTypography.caption,
                color = MaterialTheme.appColors.onSurface,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
private fun PlanRowPreview() {
    PaywallPreview {
        PlanRow(plans = PreviewPaywallUiState.plans, selectedPlanId = PreviewPaywallUiState.selectedPlanId, onPlanSelect = {})
    }
}

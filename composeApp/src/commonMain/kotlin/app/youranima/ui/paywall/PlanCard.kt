package app.youranima.ui.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.paywall.SubscriptionPlan
import app.youranima.resources.Res
import app.youranima.resources.paywall_save
import app.youranima.ui.components.TagChip
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

internal val PlanCardHeight = 132.dp

/** Alphas of the `primary` gradient over the selected card (top → bottom). */
private const val SELECTED_TOP_ALPHA = 0.35f
private const val SELECTED_BOTTOM_ALPHA = 0.10f

/**
 * One plan (radio semantics): 2-line title, price and an optional "Save N%" chip at the bottom. Selected: `primary`
 * gradient over the card fill and a 2dp `primary` border.
 */
@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Column(
        modifier =
            modifier
                .height(PlanCardHeight)
                .testTag(PaywallScreenTags.plan(plan.id))
                .planCardBackground(selected)
                .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = plan.title,
            style = typography.cardTitle,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        Text(text = plan.price, style = typography.button, color = colors.onSurface, maxLines = 1, softWrap = false)
        Spacer(Modifier.weight(1f))
        plan.savingsPercent?.let { percent ->
            TagChip(label = stringResource(Res.string.paywall_save, percent), fill = colors.accentOrange)
        }
    }
}

/** The shared card container; selected adds the `primary` gradient and a 2dp `primary` border over its 1dp outline. */
@Composable
private fun Modifier.planCardBackground(selected: Boolean): Modifier {
    val shape = MaterialTheme.shapes.large
    val primary = MaterialTheme.appColors.primary
    val card = appCard(PaddingValues())
    val highlighted =
        if (selected) {
            val gradient =
                Brush.verticalGradient(
                    listOf(primary.copy(alpha = SELECTED_TOP_ALPHA), primary.copy(alpha = SELECTED_BOTTOM_ALPHA)),
                )
            card.background(gradient, shape).border(2.dp, primary, shape)
        } else {
            card
        }
    return highlighted.clip(shape)
}

@Preview
@Composable
private fun PlanCardPreview() {
    PaywallPreview {
        Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PreviewPaywallUiState.plans.forEach { plan ->
                Box(Modifier.width(118.dp)) {
                    PlanCard(plan = plan, selected = plan.id == "quarterly", onClick = {}, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

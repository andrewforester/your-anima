package app.youranima.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.home.ForecastPeriod
import app.youranima.resources.Res
import app.youranima.resources.home_tab_today
import app.youranima.resources.home_tab_tomorrow
import app.youranima.resources.home_tab_week
import app.youranima.resources.home_tab_yesterday
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateTabs(
    selected: ForecastPeriod,
    onSelect: (ForecastPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ForecastPeriod.entries.forEach { period ->
            DateTab(
                label = period.label,
                selected = period == selected,
                onClick = { onSelect(period) },
                modifier = Modifier.testTag(HomeScreenTags.tab(period)),
            )
        }
    }
}

@Composable
private fun DateTab(
    label: StringResource,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Column(
        modifier = modifier.width(80.dp).selectable(selected = selected, onClick = onClick, role = Role.Tab),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.appTypography.tab,
            color = if (selected) colors.onSurface else colors.onSurfaceMuted,
        )
        Box(
            Modifier
                .size(width = 28.dp, height = 2.dp)
                .background(if (selected) colors.primary else Color.Transparent, RoundedCornerShape(1.dp)),
        )
    }
}

private val ForecastPeriod.label: StringResource
    get() =
        when (this) {
            ForecastPeriod.Yesterday -> Res.string.home_tab_yesterday
            ForecastPeriod.Today -> Res.string.home_tab_today
            ForecastPeriod.Tomorrow -> Res.string.home_tab_tomorrow
            ForecastPeriod.Week -> Res.string.home_tab_week
        }

@Preview
@Composable
private fun DateTabsPreview() {
    HomePreview { DateTabs(selected = ForecastPeriod.Today, onSelect = {}) }
}

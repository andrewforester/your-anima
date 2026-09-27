package app.youranima.ui.psychics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.ic_message_circle
import app.youranima.resources.psychics_filter_all
import app.youranima.resources.psychics_filter_call
import app.youranima.resources.psychics_filter_chat
import app.youranima.resources.psychics_ic_phone
import app.youranima.ui.components.GlassPill
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val PsychicFilter.label: StringResource
    get() =
        when (this) {
            PsychicFilter.All -> Res.string.psychics_filter_all
            PsychicFilter.Call -> Res.string.psychics_filter_call
            PsychicFilter.Chat -> Res.string.psychics_filter_chat
        }

private val PsychicFilter.icon: DrawableResource?
    get() =
        when (this) {
            PsychicFilter.All -> null
            PsychicFilter.Call -> Res.drawable.psychics_ic_phone
            PsychicFilter.Chat -> Res.drawable.ic_message_circle
        }

/** Three equal glass pills (All / Call / Chat) acting as tabs; the selected one is filled with `primary`. */
@Composable
fun PsychicFilterRow(
    selected: PsychicFilter,
    onSelect: (PsychicFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().selectableGroup().testTag(PsychicsScreenTags.FILTER),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PsychicFilter.entries.forEach { filter ->
            val icon = filter.icon
            GlassPill(
                label = stringResource(filter.label),
                onClick = { onSelect(filter) },
                selected = filter == selected,
                modifier = Modifier.weight(1f).testTag(PsychicsScreenTags.filter(filter)),
                icon =
                    icon?.let {
                        {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = null,
                                tint = MaterialTheme.appColors.onSurface,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    },
            )
        }
    }
}

@Preview
@Composable
private fun PsychicFilterRowPreview() {
    PsychicsPreview { PsychicFilterRow(selected = PsychicFilter.All, onSelect = {}) }
}

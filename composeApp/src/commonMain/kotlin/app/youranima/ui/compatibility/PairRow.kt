package app.youranima.ui.compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.compatibility.CompatibilityPerson
import app.youranima.resources.Res
import app.youranima.resources.compatibility_ic_plus_thin
import app.youranima.resources.compatibility_partner
import app.youranima.resources.compatibility_you
import app.youranima.resources.compatibility_your_avatar
import app.youranima.ui.components.ProfileAvatar
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val CircleSize = 140.dp
private val PlusSize = 24.dp
private val ItemGap = 16.dp
private val LabelGap = 12.dp

/** "You" avatar · thin "+" · "Partner" slot (or the partner's avatar once one is added). */
@Composable
fun PairRow(
    user: CompatibilityPerson,
    partner: CompatibilityPerson?,
    onAddPartner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ItemGap),
        verticalAlignment = Alignment.Top,
    ) {
        PersonColumn(label = stringResource(Res.string.compatibility_you)) {
            ProfileAvatar(
                painter = painterResource(user.avatar.drawable),
                size = CircleSize,
                contentDescription = stringResource(Res.string.compatibility_your_avatar),
                modifier = Modifier.testTag(CompatibilityScreenTags.USER_AVATAR),
            )
        }
        Icon(
            painter = painterResource(Res.drawable.compatibility_ic_plus_thin),
            contentDescription = null,
            tint = MaterialTheme.appColors.onSurfaceMuted,
            modifier = Modifier.padding(top = (CircleSize - PlusSize) / 2).size(PlusSize),
        )
        PersonColumn(label = stringResource(Res.string.compatibility_partner)) {
            if (partner == null) {
                PartnerSlot(size = CircleSize, onAddPartner = onAddPartner)
            } else {
                ProfileAvatar(painter = painterResource(partner.avatar.drawable), size = CircleSize, contentDescription = partner.name)
            }
        }
    }
}

@Composable
private fun PersonColumn(
    label: String,
    circle: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LabelGap),
    ) {
        circle()
        Text(
            text = label,
            style = MaterialTheme.appTypography.tab,
            color = MaterialTheme.appColors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun PairRowPreview() {
    AppTheme {
        Box(Modifier.background(MaterialTheme.appColors.background)) {
            PairRow(user = PreviewCompatibilityUiState.user, partner = null, onAddPartner = {})
        }
    }
}

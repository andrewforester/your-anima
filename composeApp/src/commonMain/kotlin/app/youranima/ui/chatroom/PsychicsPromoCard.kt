package app.youranima.ui.chatroom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.chatroom.PsychicsPromo
import app.youranima.resources.Res
import app.youranima.resources.chatroom_promo_subtitle
import app.youranima.resources.chatroom_promo_title
import app.youranima.resources.chatroom_see_psychics
import app.youranima.ui.components.PrimaryButton
import app.youranima.ui.components.RatingRow
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

/** "Check our Psychics" card: avatar stack + title, subtitle and rating, then the full-width "See Psychics" button. */
@Composable
fun PsychicsPromoCard(
    promo: PsychicsPromo,
    onSeePsychicsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().appCard(PaddingValues(16.dp)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            PromoAvatarStack(promo.avatars)
            PromoText(promo, Modifier.weight(1f))
        }
        PrimaryButton(
            label = stringResource(Res.string.chatroom_see_psychics),
            onClick = onSeePsychicsClick,
            modifier = Modifier.fillMaxWidth().testTag(ChatroomScreenTags.SEE_PSYCHICS),
        )
    }
}

@Composable
private fun PromoText(
    promo: PsychicsPromo,
    modifier: Modifier,
) {
    val colors = MaterialTheme.appColors
    val typography = MaterialTheme.appTypography
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = stringResource(Res.string.chatroom_promo_title),
            style = typography.cardTitle,
            color = colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = stringResource(Res.string.chatroom_promo_subtitle, promo.psychicsCount),
            style = typography.body,
            color = colors.accentLavender,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(2.dp))
        RatingRow(rating = promo.rating, reviewCount = promo.reviewCount)
    }
}

@Preview
@Composable
private fun PsychicsPromoCardPreview() {
    ChatroomPreview { PsychicsPromoCard(PreviewChatroomUiState.promo, onSeePsychicsClick = {}) }
}

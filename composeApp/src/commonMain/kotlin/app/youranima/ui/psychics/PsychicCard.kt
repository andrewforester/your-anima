package app.youranima.ui.psychics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.psychics.Psychic
import app.youranima.data.psychics.PsychicStatus
import app.youranima.resources.Res
import app.youranima.resources.ic_message_circle
import app.youranima.resources.psychics_action_call
import app.youranima.resources.psychics_action_chat
import app.youranima.resources.psychics_experience
import app.youranima.resources.psychics_free_minutes
import app.youranima.resources.psychics_ic_phone
import app.youranima.resources.psychics_price_per_minute
import app.youranima.ui.components.PrimaryButton
import app.youranima.ui.components.RatingRow
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/** Carousel card width: two 68dp buttons + gap + padding (the feed's 144 is too narrow for them). */
internal val PsychicCardWidth = 168.dp

/** Photo with name and status on top, then experience, rating, Call/Chat buttons and price. Not clickable itself. */
@Composable
fun PsychicCard(
    psychic: Psychic,
    modifier: Modifier = Modifier,
    onCallClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .width(PsychicCardWidth)
                .testTag(PsychicsScreenTags.card(psychic.id))
                .appCard(PaddingValues())
                .clip(MaterialTheme.shapes.large),
    ) {
        PsychicPhotoHeader(psychic)
        Column(Modifier.fillMaxWidth().padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 12.dp)) {
            Text(
                text = pluralStringResource(Res.plurals.psychics_experience, psychic.yearsOfExperience, psychic.yearsOfExperience),
                style = MaterialTheme.appTypography.body,
                color = MaterialTheme.appColors.accentLavender,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            RatingRow(rating = psychic.rating, reviewCount = psychic.reviewCount)
            Spacer(Modifier.height(12.dp))
            ChannelButtons(psychic = psychic, onCallClick = onCallClick, onChatClick = onChatClick)
            Spacer(Modifier.height(8.dp))
            Price(freeMinutes = psychic.freeMinutes, pricePerMinute = psychic.pricePerMinute)
        }
    }
}

@Composable
private fun ChannelButtons(
    psychic: Psychic,
    onCallClick: () -> Unit,
    onChatClick: () -> Unit,
) {
    val online = psychic.status == PsychicStatus.Online
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PrimaryButton(
            label = stringResource(Res.string.psychics_action_call),
            icon = Res.drawable.psychics_ic_phone,
            enabled = online && psychic.canCall,
            onClick = onCallClick,
            modifier = Modifier.weight(1f).testTag(PsychicsScreenTags.callButton(psychic.id)),
        )
        PrimaryButton(
            label = stringResource(Res.string.psychics_action_chat),
            icon = Res.drawable.ic_message_circle,
            enabled = online && psychic.canChat,
            onClick = onChatClick,
            modifier = Modifier.weight(1f).testTag(PsychicsScreenTags.chatButton(psychic.id)),
        )
    }
}

@Composable
private fun Price(
    freeMinutes: Int,
    pricePerMinute: String,
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.psychics_free_minutes, freeMinutes),
            style = MaterialTheme.appTypography.pill,
            color = MaterialTheme.appColors.accentGold,
            maxLines = 1,
        )
        Text(
            text = stringResource(Res.string.psychics_price_per_minute, pricePerMinute),
            style = MaterialTheme.appTypography.caption,
            color = MaterialTheme.appColors.accentLavender,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun PsychicCardPreview() {
    PsychicsPreview { PsychicCard(psychic = PreviewPsychicsUiState.sections.first().psychics[1]) }
}

@Preview
@Composable
private fun PsychicCardBusyPlaceholderPreview() {
    PsychicsPreview { PsychicCard(psychic = PreviewPsychicsUiState.sections[1].psychics.first()) }
}

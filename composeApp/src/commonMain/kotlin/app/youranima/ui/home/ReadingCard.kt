package app.youranima.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.data.home.ReadingOffer
import app.youranima.data.home.ReadingType
import app.youranima.resources.Res
import app.youranima.resources.home_ask
import app.youranima.resources.home_reading_chat
import app.youranima.resources.home_reading_free
import app.youranima.resources.home_reading_lead
import app.youranima.resources.home_reading_paid
import app.youranima.resources.ic_message_circle
import app.youranima.ui.components.TintedIconBox
import app.youranima.ui.components.appCard
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val PLACEHOLDER = "%1\$s"

@Composable
fun ReadingCard(
    offer: ReadingOffer,
    modifier: Modifier = Modifier,
    onAskClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag(HomeScreenTags.readingCard(offer.id))
                .appCard(PaddingValues(16.dp)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = leadText(offer.type),
            style = MaterialTheme.appTypography.cardLead,
            color = MaterialTheme.appColors.textSoft,
        )
        QuestionRow(question = offer.suggestedQuestion)
        AskButton(onClick = onAskClick, modifier = Modifier.testTag(HomeScreenTags.askButton(offer.id)))
    }
}

/** "You have " + highlighted offer name. The highlight is inserted into the localized template. */
@Composable
private fun leadText(type: ReadingType): AnnotatedString {
    val template = stringResource(Res.string.home_reading_lead)
    val highlight =
        stringResource(
            when (type) {
                ReadingType.Free -> Res.string.home_reading_free
                ReadingType.Paid -> Res.string.home_reading_paid
            },
        )
    val highlightStyle = SpanStyle(color = MaterialTheme.appColors.accentPurple, fontWeight = FontWeight.Bold)
    val at = template.indexOf(PLACEHOLDER)
    return buildAnnotatedString {
        if (at < 0) {
            append(template)
            withStyle(highlightStyle) { append(highlight) }
        } else {
            append(template.substring(0, at))
            withStyle(highlightStyle) { append(highlight) }
            append(template.substring(at + PLACEHOLDER.length))
        }
    }
}

@Composable
private fun QuestionRow(question: String) {
    val colors = MaterialTheme.appColors
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        TintedIconBox(
            icon = painterResource(Res.drawable.ic_message_circle),
            tint = colors.accentPink,
            size = 42.dp,
            iconSize = 20.dp,
            shape = MaterialTheme.shapes.medium,
            contentDescription = stringResource(Res.string.home_reading_chat),
        )
        val fieldShape = RoundedCornerShape(21.dp)
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(42.dp)
                    .background(colors.background, fieldShape)
                    .border(BorderStroke(1.dp, colors.outline), fieldShape)
                    .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = question,
                style = MaterialTheme.appTypography.input,
                color = colors.onSurfaceMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AskButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(shape)
                .background(colors.glassFill, shape)
                .border(BorderStroke(1.dp, colors.glassBorder), shape)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.home_ask),
            style = MaterialTheme.appTypography.button,
            color = colors.onSurface,
        )
    }
}

@Preview
@Composable
private fun ReadingCardPreview() {
    HomePreview { ReadingCard(offer = PreviewHomeUiState.readings.first()) }
}

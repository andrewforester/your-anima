package app.youranima.ui.psychics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.ic_heart
import app.youranima.resources.psychics_cd_favourites
import app.youranima.resources.psychics_title
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val IconSize = 22.dp

/** Heart (favourites) on the left, centred "Psychics" title; a spacer mirrors the heart so the title stays centred. */
@Composable
fun PsychicsTopBar(
    modifier: Modifier = Modifier,
    onFavouritesClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_heart),
            contentDescription = stringResource(Res.string.psychics_cd_favourites),
            tint = colors.onSurface,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onFavouritesClick)
                    .testTag(PsychicsScreenTags.FAVOURITES)
                    .size(IconSize),
        )
        Text(
            text = stringResource(Res.string.psychics_title),
            style = MaterialTheme.appTypography.cardTitle,
            color = colors.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).testTag(PsychicsScreenTags.TITLE),
        )
        Spacer(Modifier.size(IconSize))
    }
}

@Preview
@Composable
private fun PsychicsTopBarPreview() {
    PsychicsPreview { PsychicsTopBar() }
}

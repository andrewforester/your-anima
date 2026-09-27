package app.youranima.ui.psychics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import app.youranima.resources.Res
import app.youranima.resources.ic_heart
import app.youranima.resources.psychics_cd_favourites
import app.youranima.resources.psychics_title
import app.youranima.ui.components.ScreenTopBar
import app.youranima.ui.components.ScreenTopBarIconSize
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Shared [ScreenTopBar] with the heart (favourites) on the left and the centred "Psychics" title. */
@Composable
fun PsychicsTopBar(
    modifier: Modifier = Modifier,
    onFavouritesClick: () -> Unit = {},
) {
    ScreenTopBar(
        title = stringResource(Res.string.psychics_title),
        modifier = modifier,
        titleModifier = Modifier.testTag(PsychicsScreenTags.TITLE),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_heart),
            contentDescription = stringResource(Res.string.psychics_cd_favourites),
            tint = MaterialTheme.appColors.onSurface,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onFavouritesClick)
                    .testTag(PsychicsScreenTags.FAVOURITES)
                    .size(ScreenTopBarIconSize),
        )
    }
}

@Preview
@Composable
private fun PsychicsTopBarPreview() {
    PsychicsPreview { PsychicsTopBar() }
}

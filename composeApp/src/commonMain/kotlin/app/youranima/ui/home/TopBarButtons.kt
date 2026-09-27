package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.home_add_story
import app.youranima.resources.home_avatar
import app.youranima.resources.home_avatar_thumb
import app.youranima.resources.home_ic_plus
import app.youranima.resources.home_ic_settings
import app.youranima.resources.home_settings
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Top-bar buttons (Figma item 3). [CollapsingProfileHeader] places them; add-story and the thumb fade out on collapse. */
@Composable
fun AddStoryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Box(
        modifier =
            modifier
                .size(TopBarButtonSize)
                .clip(CircleShape)
                .border(1.5.dp, colors.primary, CircleShape)
                .clickable(onClick = onClick)
                .testTag(HomeScreenTags.ADD_STORY),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.home_ic_plus),
            contentDescription = stringResource(Res.string.home_add_story),
            tint = colors.primary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
fun AvatarThumb(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(Res.drawable.home_avatar_thumb),
        contentDescription = stringResource(Res.string.home_avatar),
        contentScale = ContentScale.Crop,
        modifier =
            modifier
                .size(TopBarButtonSize)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.appColors.accentOrange, CircleShape)
                .clickable(onClick = onClick),
    )
}

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(Res.drawable.home_ic_settings),
        contentDescription = stringResource(Res.string.home_settings),
        tint = MaterialTheme.appColors.onSurface,
        modifier =
            modifier
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .testTag(HomeScreenTags.SETTINGS)
                .size(22.dp),
    )
}

/** Size of the add-story button and the avatar thumb; also the compact size of the profile avatar. */
internal val TopBarButtonSize = 36.dp

@Preview
@Composable
private fun TopBarButtonsPreview() {
    HomePreview {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AddStoryButton(onClick = {})
            AvatarThumb(onClick = {})
            SettingsButton(onClick = {})
        }
    }
}

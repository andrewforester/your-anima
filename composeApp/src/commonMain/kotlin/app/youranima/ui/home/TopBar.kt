package app.youranima.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onAddStoryClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, colors.primary, CircleShape)
                        .clickable(onClick = onAddStoryClick)
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
            Image(
                painter = painterResource(Res.drawable.home_avatar_thumb),
                contentDescription = stringResource(Res.string.home_avatar),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(2.dp, colors.accentOrange, CircleShape)
                        .clickable(onClick = onAvatarClick),
            )
        }
        Icon(
            painter = painterResource(Res.drawable.home_ic_settings),
            contentDescription = stringResource(Res.string.home_settings),
            tint = colors.onSurface,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onSettingsClick)
                    .testTag(HomeScreenTags.SETTINGS)
                    .size(22.dp),
        )
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    HomePreview { TopBar() }
}

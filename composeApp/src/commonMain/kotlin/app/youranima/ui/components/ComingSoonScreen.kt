package app.youranima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.coming_soon_subtitle
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.stringResource

object ComingSoonScreenTags {
    const val SCREEN = "coming_soon_screen"
}

/** Placeholder shown for tabs ([app.youranima.ui.components.AppTab]) that don't have a real screen yet. */
@Composable
fun ComingSoonScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.appColors.background)
                .testTag(ComingSoonScreenTags.SCREEN),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.appTypography.name,
                color = MaterialTheme.appColors.onSurface,
            )
            Text(
                text = stringResource(Res.string.coming_soon_subtitle),
                style = MaterialTheme.appTypography.body,
                color = MaterialTheme.appColors.onSurfaceMuted,
            )
        }
    }
}

@Preview
@Composable
private fun ComingSoonScreenPreview() {
    AppTheme {
        ComingSoonScreen(title = "Psychics")
    }
}

package app.youranima.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.ic_heart
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource

/** Width reserved for [ScreenTopBar]'s leading icon, mirrored on the right so the title stays centred. */
val ScreenTopBarIconSize = 22.dp

/**
 * Tab-screen top bar (56 high, padding 20): centred `cardTitle` [title], optional [leading] icon (at most
 * [ScreenTopBarIconSize] wide) on the left. [titleModifier] is applied to the title text (e.g. a test tag).
 */
@Composable
fun ScreenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
) {
    val titleInset = if (leading != null) ScreenTopBarIconSize else 0.dp
    Box(
        modifier = modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        leading?.invoke()
        Text(
            text = title,
            style = MaterialTheme.appTypography.cardTitle,
            color = MaterialTheme.appColors.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = titleModifier.fillMaxWidth().padding(horizontal = titleInset),
        )
    }
}

@Preview
@Composable
private fun ScreenTopBarPreview() {
    AppTheme {
        Column(Modifier.background(MaterialTheme.appColors.background)) {
            ScreenTopBar(title = "Readings")
            ScreenTopBar(title = "Psychics") {
                Icon(
                    painter = painterResource(Res.drawable.ic_heart),
                    contentDescription = null,
                    tint = MaterialTheme.appColors.onSurface,
                    modifier = Modifier.size(ScreenTopBarIconSize),
                )
            }
        }
    }
}

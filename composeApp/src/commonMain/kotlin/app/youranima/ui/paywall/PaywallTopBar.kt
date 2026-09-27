package app.youranima.ui.paywall

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.youranima.resources.Res
import app.youranima.resources.paywall_cd_close
import app.youranima.resources.paywall_ic_close
import app.youranima.resources.paywall_restore
import app.youranima.resources.paywall_title
import app.youranima.ui.components.ScreenTopBar
import app.youranima.ui.components.ScreenTopBarIconSize
import app.youranima.ui.theme.appColors
import app.youranima.ui.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Minimum touch target of the top bar actions. */
private val TouchTarget = 44.dp

/** Shared [ScreenTopBar] with the close X on the left and the "Restore" text button on the right. */
@Composable
fun PaywallTopBar(
    onClose: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenTopBar(
        title = stringResource(Res.string.paywall_title),
        modifier = modifier,
        leading = { CloseButton(onClick = onClose) },
        trailing = { RestoreButton(onClick = onRestore) },
    )
}

/** 22dp X in the top bar's icon slot; the 44dp touch area overflows the slot, centred on the icon. */
@Composable
private fun CloseButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .size(ScreenTopBarIconSize)
                .requiredSize(TouchTarget)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onClick)
                .testTag(PaywallScreenTags.CLOSE),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.paywall_ic_close),
            contentDescription = stringResource(Res.string.paywall_cd_close),
            tint = MaterialTheme.appColors.onSurface,
            modifier = Modifier.size(ScreenTopBarIconSize),
        )
    }
}

@Composable
private fun RestoreButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .height(TouchTarget)
                .clickable(role = Role.Button, onClick = onClick)
                .testTag(PaywallScreenTags.RESTORE),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.paywall_restore),
            style = MaterialTheme.appTypography.pill,
            color = MaterialTheme.appColors.primary,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun PaywallTopBarPreview() {
    PaywallPreview { PaywallTopBar(onClose = {}, onRestore = {}) }
}

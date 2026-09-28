package app.youranima.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/**
 * Pull-to-refresh that reloads the app: a pull past the threshold at the very top of [content] shows the
 * M3 indicator and calls [onReload] once (the indicator keeps spinning until the page is gone).
 * With `onReload == null` (native targets, see [appReloader]) [content] is emitted as is, with no wrapper.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToReload(
    onReload: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (onReload == null) {
        content()
        return
    }
    var isReloading by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isReloading,
        onRefresh = {
            if (!isReloading) {
                isReloading = true
                onReload()
            }
        },
        modifier = modifier.fillMaxSize().testTag(AppShellTags.PULL_TO_RELOAD),
        state = state,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = isReloading,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = MaterialTheme.appColors.surface,
                color = MaterialTheme.appColors.primary,
            )
        },
    ) {
        content()
    }
}

@Preview
@Composable
private fun PullToReloadPreview() {
    AppTheme {
        PullToReload(onReload = {}) { Box(Modifier.fillMaxSize()) }
    }
}

package app.youranima.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.youranima.data.home.HomeRepository
import app.youranima.data.home.MockHomeRepository
import app.youranima.ui.chatroom.ChatroomScreen
import app.youranima.ui.compatibility.CompatibilityScreen
import app.youranima.ui.components.AppBottomBar
import app.youranima.ui.components.AppTab
import app.youranima.ui.home.HomeScreen
import app.youranima.ui.psychics.PsychicsScreen
import app.youranima.ui.readings.ReadingsScreen
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/**
 * The [AppRoutes.MAIN] destination: the selected [AppTab]'s content under the shared [AppBottomBar].
 * Tabs are local saved state, not back-stack entries. The tab content (not the bar) is wrapped in
 * [PullToReload] where the platform can reload.
 */
@Composable
fun MainTabs(
    onLockedClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeRepository: HomeRepository = MockHomeRepository,
    onReload: (() -> Unit)? = appReloader,
) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Today) }
    val badges = remember(homeRepository) { homeRepository.navBadges() }
    Box(modifier.fillMaxSize().background(MaterialTheme.appColors.background)) {
        PullToReload(onReload = onReload) {
            when (selectedTab) {
                AppTab.Today -> HomeScreen(onLockedClick = onLockedClick)
                AppTab.Psychics -> PsychicsScreen()
                AppTab.Compatibility -> CompatibilityScreen()
                AppTab.Chatroom -> ChatroomScreen()
                AppTab.Readings -> ReadingsScreen()
            }
        }
        AppBottomBar(
            selected = selectedTab,
            badges = badges,
            onSelect = { selectedTab = it },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Preview
@Composable
private fun MainTabsPreview() {
    AppTheme {
        MainTabs(onLockedClick = {})
    }
}

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
import app.youranima.ui.paywall.PaywallScreen
import app.youranima.ui.psychics.PsychicsScreen
import app.youranima.ui.readings.ReadingsScreen
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/**
 * App-level shell: holds the selected [AppTab] and draws its content under the shared [AppBottomBar]. A tap on a
 * locked element opens the [PaywallScreen] over everything (the bottom bar is hidden while it is shown).
 * The tab content (not the bar or the paywall) is wrapped in [PullToReload] where the platform can reload.
 */
@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    homeRepository: HomeRepository = MockHomeRepository,
    onReload: (() -> Unit)? = appReloader,
) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Today) }
    var showPaywall by rememberSaveable { mutableStateOf(false) }
    val badges = remember(homeRepository) { homeRepository.navBadges() }
    Box(modifier.fillMaxSize().background(MaterialTheme.appColors.background)) {
        PullToReload(onReload = onReload) {
            when (selectedTab) {
                AppTab.Today -> HomeScreen(onLockedClick = { showPaywall = true })
                AppTab.Psychics -> PsychicsScreen()
                AppTab.Compatibility -> CompatibilityScreen()
                AppTab.Chatroom -> ChatroomScreen()
                AppTab.Readings -> ReadingsScreen()
            }
        }
        if (showPaywall) {
            PaywallScreen(onClose = { showPaywall = false })
        } else {
            AppBottomBar(
                selected = selectedTab,
                badges = badges,
                onSelect = { selectedTab = it },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview
@Composable
private fun AppShellPreview() {
    AppTheme {
        AppShell()
    }
}

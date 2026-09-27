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
import app.youranima.resources.Res
import app.youranima.resources.nav_chatroom
import app.youranima.resources.nav_compatibility
import app.youranima.ui.components.AppBottomBar
import app.youranima.ui.components.AppTab
import app.youranima.ui.components.ComingSoonScreen
import app.youranima.ui.home.HomeScreen
import app.youranima.ui.psychics.PsychicsScreen
import app.youranima.ui.readings.ReadingsScreen
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors
import org.jetbrains.compose.resources.stringResource

/** App-level shell: holds the selected [AppTab] and draws its content under the shared [AppBottomBar]. */
@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    homeRepository: HomeRepository = MockHomeRepository,
) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Today) }
    val badges = remember(homeRepository) { homeRepository.navBadges() }
    Box(modifier.fillMaxSize().background(MaterialTheme.appColors.background)) {
        when (selectedTab) {
            AppTab.Today -> HomeScreen()
            AppTab.Psychics -> PsychicsScreen()
            AppTab.Compatibility -> ComingSoonScreen(title = stringResource(Res.string.nav_compatibility))
            AppTab.Chatroom -> ComingSoonScreen(title = stringResource(Res.string.nav_chatroom))
            AppTab.Readings -> ReadingsScreen()
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
private fun AppShellPreview() {
    AppTheme {
        AppShell()
    }
}

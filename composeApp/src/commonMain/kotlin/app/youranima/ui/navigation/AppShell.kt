package app.youranima.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.youranima.data.home.HomeRepository
import app.youranima.data.home.MockHomeRepository
import app.youranima.ui.paywall.PaywallScreen
import app.youranima.ui.theme.AppTheme
import app.youranima.ui.theme.appColors

/**
 * App-level navigation: a [NavHost] with [AppRoutes.MAIN] ([MainTabs]) and [AppRoutes.PAYWALL] (full-screen
 * [PaywallScreen], no bottom bar). A locked element pushes the paywall; its X or the platform's back pops it.
 * [onNavHostReady] gets the controller once the graph is set (web binds it to the browser history).
 */
@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeRepository: HomeRepository = MockHomeRepository,
    onReload: (() -> Unit)? = appReloader,
    onNavHostReady: suspend (NavHostController) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.MAIN,
        modifier = modifier.background(MaterialTheme.appColors.background),
    ) {
        composable(AppRoutes.MAIN) {
            MainTabs(
                onLockedClick = { navController.navigate(AppRoutes.PAYWALL) { launchSingleTop = true } },
                homeRepository = homeRepository,
                onReload = onReload,
            )
        }
        composable(AppRoutes.PAYWALL) {
            PaywallScreen(onClose = { navController.popBackStack(AppRoutes.MAIN, inclusive = false) })
        }
    }
    LaunchedEffect(navController) { onNavHostReady(navController) }
}

@Preview
@Composable
private fun AppShellPreview() {
    AppTheme {
        AppShell()
    }
}

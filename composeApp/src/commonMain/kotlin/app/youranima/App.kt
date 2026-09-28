package app.youranima

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme

/**
 * Root composable shared by Android, iOS and Web. [onNavHostReady] receives the app's nav controller once its
 * graph is set (the web entry binds it to the browser history). [onCloseScreen], when set, closes a screen from its
 * X instead of `popBackStack` (the web entry goes through `history.back()`).
 */
@Composable
fun App(
    onNavHostReady: suspend (NavHostController) -> Unit = {},
    onCloseScreen: (() -> Unit)? = null,
) {
    AppTheme {
        AppShell(onNavHostReady = onNavHostReady, onCloseScreen = onCloseScreen)
    }
}

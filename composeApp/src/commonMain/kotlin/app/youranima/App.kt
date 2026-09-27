package app.youranima

import androidx.compose.runtime.Composable
import app.youranima.ui.navigation.AppShell
import app.youranima.ui.theme.AppTheme

/** Root composable shared by Android, iOS and Web. */
@Composable
fun App() {
    AppTheme {
        AppShell()
    }
}

package app.youranima

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.youranima.ui.home.HomeScreen

/**
 * Root composable shared by Android, iOS and Web.
 *
 * Integration point with the UI layer (see docs/COORDINATION.md): `AppTheme` and `HomeScreen`
 * below are temporary stubs. The UI session replaces them with imports from `app.youranima.ui.*`.
 */
@Composable
fun App() {
    AppTheme {
        HomeScreen()
    }
}

// TODO(ui): replace with app.youranima.ui.theme.AppTheme
@Composable
private fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

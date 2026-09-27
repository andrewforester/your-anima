package app.youranima

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.youranima.ui.theme.AppTheme

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

// TODO(ui): replace with app.youranima.ui.home.HomeScreen
@Composable
private fun HomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            Text("Your Anima")
        }
    }
}

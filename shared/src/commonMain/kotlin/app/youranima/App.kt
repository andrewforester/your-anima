package app.youranima

import androidx.compose.runtime.Composable
import app.youranima.ui.home.HomeScreen
import app.youranima.ui.theme.AnimaTheme

/** Root composable shared by Android, iOS and Web. */
@Composable
fun App() {
    AnimaTheme {
        HomeScreen()
    }
}

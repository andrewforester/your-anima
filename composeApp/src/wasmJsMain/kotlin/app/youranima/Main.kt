package app.youranima

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LocalResourceReader

private const val COMPOSE_TARGET = "composeTarget"

/**
 * Reads the first screen's resources (fonts, avatars, icons, strings), which index.html has been downloading in
 * parallel with the wasm, into memory while the skiko runtime is still downloading, then mounts `App()` on an
 * [InMemoryResourceReader]: the first composition finds them all ready, and the HTML/CSS loader in index.html is
 * hidden once that fully styled frame is drawn. Once shown, the app reloads itself if the browser drops its WebGL
 * context ([reloadOnWebGlContextLoss]). The app's nav controller is bound to the browser history: a new screen adds
 * a history entry (`#route`), browser back pops it. A screen's X goes through `history.back()` too, so it removes
 * that entry instead of adding one.
 */
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalResourceApi::class,
    InternalResourceApi::class,
    ExperimentalBrowserHistoryApi::class,
)
fun main() {
    MainScope().launch {
        val preloaded = loadPreloadedResources()
        ComposeViewport(viewportContainerId = COMPOSE_TARGET) {
            val defaultReader = LocalResourceReader.current
            val reader = remember { InMemoryResourceReader(defaultReader, preloaded) }
            CompositionLocalProvider(LocalResourceReader provides reader) {
                App(
                    onNavHostReady = { it.bindToBrowserNavigation() },
                    onCloseScreen = { window.history.back() },
                )
            }
            LaunchedEffect(Unit) {
                withFrameNanos { }
                hideLoader()
                reloadOnWebGlContextLoss(COMPOSE_TARGET)
            }
        }
    }
}

private fun hideLoader() {
    document.getElementById("loader")?.classList?.add("hidden")
    document.getElementById(COMPOSE_TARGET)?.classList?.add("ready")
}

package app.youranima

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import app.youranima.resources.Res
import app.youranima.resources.geist_bold
import app.youranima.resources.geist_medium
import app.youranima.resources.geist_regular
import app.youranima.resources.geist_semibold
import app.youranima.resources.home_avatar_character
import app.youranima.resources.home_avatar_thumb
import kotlinx.browser.document
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont
import org.jetbrains.compose.resources.preloadImageBitmap

/**
 * Warms the Geist weights and the first-screen avatars before `App()` mounts, so the HTML/CSS
 * loader in index.html is the only thing visible until the UI can render fully styled.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    ComposeViewport(viewportContainerId = "composeTarget") {
        val geistRegular by preloadFont(Res.font.geist_regular)
        val geistMedium by preloadFont(Res.font.geist_medium)
        val geistSemibold by preloadFont(Res.font.geist_semibold)
        val geistBold by preloadFont(Res.font.geist_bold)
        val avatarThumb by preloadImageBitmap(Res.drawable.home_avatar_thumb)
        val avatarCharacter by preloadImageBitmap(Res.drawable.home_avatar_character)

        val ready =
            geistRegular != null && geistMedium != null && geistSemibold != null && geistBold != null &&
                avatarThumb != null && avatarCharacter != null

        if (ready) {
            LaunchedEffect(Unit) { hideLoader() }
            App()
        }
    }
}

private fun hideLoader() {
    document.getElementById("loader")?.classList?.add("hidden")
    document.getElementById("composeTarget")?.classList?.add("ready")
}

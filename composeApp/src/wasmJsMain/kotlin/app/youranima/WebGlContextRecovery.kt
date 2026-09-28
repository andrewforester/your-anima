package app.youranima

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ParentNode

private const val CANVAS_POLL_MS = 100L
private const val CANVAS_POLL_ATTEMPTS = 100

/**
 * Chrome on Android drops the WebGL context of tabs left in the background, and skiko never gets it back
 * (the canvas stays blank and dead). Waits for the Compose canvas under [containerId] (possibly inside a
 * shadow root) and, when its context is lost, shows the HTML loader and reloads the page once: right away
 * if the tab is visible, otherwise as soon as it becomes visible again.
 */
suspend fun reloadOnWebGlContextLoss(containerId: String) {
    repeat(CANVAS_POLL_ATTEMPTS) {
        val canvas = document.getElementById(containerId)?.let(::findCanvas)
        if (canvas != null) {
            canvas.addEventListener("webglcontextlost", { reloadWhenVisible() })
            return
        }
        delay(CANVAS_POLL_MS)
    }
}

private fun findCanvas(root: ParentNode): HTMLCanvasElement? {
    root.querySelector("canvas")?.let { return it as HTMLCanvasElement }
    val elements = root.querySelectorAll("*")
    for (i in 0 until elements.length) {
        val shadowRoot = (elements.item(i) as? Element)?.shadowRoot ?: continue
        findCanvas(shadowRoot)?.let { return it }
    }
    return null
}

private var reloadRequested = false

private fun reloadWhenVisible() {
    if (reloadRequested) return
    reloadRequested = true
    showLoader()
    if (isVisible()) {
        window.location.reload()
    } else {
        document.addEventListener("visibilitychange", { if (isVisible()) window.location.reload() })
    }
}

// Kotlin/Wasm's DOM bindings have no `Document.visibilityState`.
private fun isVisible(): Boolean = js("document.visibilityState === 'visible'")

private fun showLoader() {
    document.getElementById("loader")?.classList?.remove("hidden")
}

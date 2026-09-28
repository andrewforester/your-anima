package app.youranima.ui.navigation

import kotlinx.browser.window

actual val appReloader: (() -> Unit)? = { window.location.reload() }

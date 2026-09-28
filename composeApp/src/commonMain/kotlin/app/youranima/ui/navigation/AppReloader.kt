package app.youranima.ui.navigation

/**
 * Reloads the whole app when the user pulls down at the top of a tab ([PullToReload]).
 * Web: `window.location.reload()`. Native targets: `null`, so no pull-to-refresh is added there
 * (they get a real data refresh once a backend exists).
 */
expect val appReloader: (() -> Unit)?

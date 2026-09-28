# app.youranima (wasmJsMain) — web entry point

- `Main.kt`: `ComposeViewport("composeTarget")` → `App()`. Preloads the Geist weights and the first-screen avatars, then hides the HTML/CSS loader from `resources/index.html` (`#loader`, styles in `styles.css`). Binds the app's nav controller to the browser history (`App(onNavHostReady = { it.bindToBrowserNavigation() })`, #66): the paywall gets `#paywall`, browser back closes it.
- `WebGlContextRecovery.kt` (#65): Chrome on Android drops the WebGL context of background tabs and skiko never restores it (blank, dead canvas). `reloadOnWebGlContextLoss` polls for the Compose canvas under `#composeTarget` (also inside shadow roots, up to 10 s), listens for `webglcontextlost`, shows the loader again and reloads the page once: immediately if the tab is visible, else on the next `visibilitychange` to visible.
- `ui/navigation/AppReloader.wasmJs.kt`: the web `actual` of `appReloader` (pull-to-reload, see `commonMain/.../ui/navigation/agents.md`).

A killed renderer (sad-tab, memory pressure) can't be handled from code; the page just loads fresh next time.

# ui/navigation

App-level shell, new in #35 so a second screen (Psychics, #36) can share the bottom nav bar instead of each screen owning its own.

## AppShell (`AppShell.kt`) — navigation (#66)

The root composable rendered by `App()` (`AppTheme { AppShell() }`): a JetBrains `navigation-compose` `NavHost` with string routes (`AppRoutes.kt`):
- `main` → `MainTabs` (`MainTabs.kt`): holds the selected `AppTab` (`rememberSaveable`, tabs are **not** back-stack entries) and lays out the tab content (`Today` → `ui/home/HomeScreen`, `Psychics` → `ui/psychics/PsychicsScreen`, `Readings` → `ui/readings/ReadingsScreen`, `Chatroom` → `ui/chatroom/ChatroomScreen`, `Compatibility` → `ui/compatibility/CompatibilityScreen`) under `ui/components/AppBottomBar` (badges from `HomeRepository.navBadges()`, a mock).
- `paywall` → `ui/paywall/PaywallScreen`, full screen, no bottom bar. Pushed by `HomeScreen(onLockedClick = …)` (locked category cards, `launchSingleTop`); X pops back to `main` (`popBackStack(MAIN, false)`, so a double tap never empties the stack).

Back: the platform's back pops the paywall and shows `main` with the same tab and Home scroll (NavHost keeps each entry's saveable state; Home's `rememberScrollState` is saveable). Back on `main` is the platform default (Android leaves the app, web goes to the previous page).
- Android: system back / gesture through NavHost, nothing in `MainActivity`.
- iOS: NavHost's iOS default transitions include the edge-swipe back gesture (not run in the container).
- Web: `App(onNavHostReady)` → `AppShell(onNavHostReady)` hands the controller to `wasmJsMain/Main.kt`, which calls `bindToBrowserNavigation()`: the URL hash follows the route (`#main`, `#paywall`), browser back pops. Closing with X also pushes a history entry (library behaviour), so browser back after X reopens the paywall. Reload on `#paywall` opens the paywall.

Transitions are the NavHost defaults. No deep links, no other destinations. Tests: `commonTest/.../ui/navigation/AppShellTest.kt`, `PaywallNavigationTest.kt`.

## Stubs

Badge data is mocked; see `data/home/agents.md`.

## Pull to reload (#65, web only)

The tab content (not the bottom bar, not the paywall) is wrapped in `PullToReload(onReload)` (`PullToReload.kt`): M3 `PullToRefreshBox` with the indicator in theme colours (`appColors.surface` / `primary`). A pull past the threshold at the very top of the tab (Home: feed at scroll 0, header fully expanded — Home is one `verticalScroll`, so its leftover overscroll reaches the box) calls `onReload` once and keeps spinning until the page goes away. `AppShell(onReload = appReloader)`:
- `appReloader` (`AppReloader.kt`, `expect`): wasmJs → `window.location.reload()`; Android, iOS, JVM → `null`, and with `null` `PullToReload` emits the content with no wrapper (native apps get a real data refresh once a backend exists).
- Test tag: `AppShellTags.PULL_TO_RELOAD`. Tests: `commonTest/.../ui/navigation/PullToReloadTest.kt`.

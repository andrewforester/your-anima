# ui/navigation

App-level shell, new in #35 so a second screen (Psychics, #36) can share the bottom nav bar instead of each screen owning its own.

## AppShell (`AppShell.kt`)

The root composable rendered by `App()` (`AppTheme { AppShell() }`). Holds the selected `AppTab` (`rememberSaveable`) and lays out:
- the current tab's content (`when (selectedTab)`): `Today` → `ui/home/HomeScreen`, `Psychics` → `ui/psychics/PsychicsScreen` (#36), `Readings` → `ui/readings/ReadingsScreen` (#42), `Chatroom` → `ui/chatroom/ChatroomScreen` (#50), `Compatibility` → `ui/compatibility/CompatibilityScreen` (#51);
- `ui/components/AppBottomBar` pinned to the bottom, receiving `NavBadges` from `HomeRepository.navBadges()` (the existing Today-screen mock — badges are not hardcoded here).

## Paywall overlay (#59)

`showPaywall` (`rememberSaveable`): when `true`, `ui/paywall/PaywallScreen` is drawn over the tab content **instead of** the bottom bar (the tab stays composed underneath, so its scroll position survives). Opened by `HomeScreen(onLockedClick = …)` (locked category cards), closed by the paywall's X (`onClose`). No enter/exit animation, no system back handling (`BackHandler` would need a new dependency).

No navigation library, back stack or deep links (out of scope for #35) — just a `when` over an enum and one piece of saved state.

## Stubs

Badge data is mocked; see `data/home/agents.md`.

## Pull to reload (#65, web only)

The tab content (not the bottom bar, not the paywall) is wrapped in `PullToReload(onReload)` (`PullToReload.kt`): M3 `PullToRefreshBox` with the indicator in theme colours (`appColors.surface` / `primary`). A pull past the threshold at the very top of the tab (Home: feed at scroll 0, header fully expanded — Home is one `verticalScroll`, so its leftover overscroll reaches the box) calls `onReload` once and keeps spinning until the page goes away. `AppShell(onReload = appReloader)`:
- `appReloader` (`AppReloader.kt`, `expect`): wasmJs → `window.location.reload()`; Android, iOS, JVM → `null`, and with `null` `PullToReload` emits the content with no wrapper (native apps get a real data refresh once a backend exists).
- Test tag: `AppShellTags.PULL_TO_RELOAD`. Tests: `commonTest/.../ui/navigation/PullToReloadTest.kt`.

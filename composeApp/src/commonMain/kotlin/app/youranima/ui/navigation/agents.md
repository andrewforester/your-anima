# ui/navigation

App-level shell, new in #35 so a second screen (Psychics, #36) can share the bottom nav bar instead of each screen owning its own.

## AppShell (`AppShell.kt`)

The root composable rendered by `App()` (`AppTheme { AppShell() }`). Holds the selected `AppTab` (`rememberSaveable`) and lays out:
- the current tab's content (`when (selectedTab)`): `Today` → `ui/home/HomeScreen`, `Psychics` → `ui/psychics/PsychicsScreen` (#36), `Readings` → `ui/readings/ReadingsScreen` (#42), `Chatroom` → `ui/chatroom/ChatroomScreen` (#50), everything else → `ui/components/ComingSoonScreen(title)`;
- `ui/components/AppBottomBar` pinned to the bottom, receiving `NavBadges` from `HomeRepository.navBadges()` (the existing Today-screen mock — badges are not hardcoded here).

No navigation library, back stack or deep links (out of scope for #35) — just a `when` over an enum and one piece of saved state.

## Stubs

Badge data and the placeholder tab (Compatibility) are mocked; see `data/home/agents.md` and `ui/components/agents.md`.

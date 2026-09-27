# ui/components

Shared UI pieces used by more than one screen/tab (as opposed to `ui/<screen>/`, which is screen-local).

## AppTab (`AppTab.kt`)

`enum class AppTab { Today, Psychics, Compatibility, Chatroom, Readings }` — the app's top-level sections. Renamed from `HomeNavItem` and moved out of `ui/home/` (#35) so more than one screen can reference it; `ui/navigation/AppShell` switches on it to pick the visible content.

## AppBottomBar (`AppBottomBar.kt`, `AppBottomBarTags.kt`)

The bottom nav bar, shown once by `AppShell` under whichever tab is selected (previously duplicated per screen as `HomeBottomBar`). Stateless: `AppBottomBar(selected: AppTab, badges: NavBadges, onSelect: (AppTab) -> Unit)`. Badge counts (`NavBadges`, from `data/navigation/`) come from the caller, not hardcoded here. Icons stay under their original `home_ic_*` resource names (only the bar uses them); labels and the "Free" badge text moved to the shared `composeResources/values/strings.xml` as `nav_*` keys.

## ComingSoonScreen (`ComingSoonScreen.kt`)

Placeholder for tabs without a real screen yet (Psychics, Compatibility, Chatroom, Readings — until #36 and later Issues build them): centered title + "Coming soon" subtitle, theme tokens only. `ComingSoonScreen(title: String)` — the title string is passed in by `AppShell` per tab.

## Stubs

Psychics, Compatibility, Chatroom and Readings render `ComingSoonScreen` until their own screens exist; #36 replaces the Psychics placeholder with `PsychicsScreen`.

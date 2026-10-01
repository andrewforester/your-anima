# ui/compatibility

The Compatibility tab (#51, design `docs/design/compatibility/`), shown by `ui/navigation/AppShell` for `AppTab.Compatibility`. Empty state inviting the user to check love compatibility: hero sky with two comets, "Compatibility" top bar, headline + subtitle, and the user's avatar next to an empty dashed "Partner" circle with a "+" button.

## Entry point

- `CompatibilityScreen(repository, onAddPartner)` (stateful): loads `CompatibilityRepository.compatibilityPair()` once and maps it to `CompatibilityUiState` (`toUiState()`).
- `CompatibilityScreen(state, onAddPartner)` (stateless): `BoxWithConstraints` with the page fill and a fixed `cardGlow` @ 50 % radial glow above the bottom bar (`bottomGlow`); a `verticalScroll` column with `CompatibilityHero` holding the `ScreenTopBar` and the body. The body box has a min height of viewport − status bar − 56 (top bar) − 84 (bottom bar) − navigation bar, so it is vertically centred and the page scrolls only on short screens.

## Components (one per file)

- `CompatibilityHero`: shared `HeroBackground` + `compatibility_comets` vector placed with `Modifier.inHeroFrame(228, 188, 160, 80)`; content slot on top.
- `PairRow`: "You" (`ProfileAvatar` 140 from `ui/components`) · thin "+" (`compatibility_ic_plus_thin`, `onSurfaceMuted`) · "Partner" (`PartnerSlot`, or the partner's avatar when `partner != null`, not reachable yet). Labels `tab`.
- `PartnerSlot`: 140 box, 4 dp inset, `surface` circle with a dashed 1.5 `onSurfaceMuted` border (8/5), white 24 button with `home_ic_plus` → `onAddPartner`.
- `CompatibilityResources.kt`: `PersonAvatar.drawable`. `CompatibilityScreenTags`: test tags.

## Data

`data/compatibility/` (`MockCompatibilityRepository`: home user, no partner).

## Stubs

`onAddPartner` is an empty callback hoisted to `AppShell`'s default; no add-partner flow, results or sign pickers yet. `TopBarHeight` (56) and `BottomBarHeight` (84) mirror the shared bars' sizes as private constants (the shared components don't expose them).

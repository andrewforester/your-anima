# ui/psychics

The "Psychics" tab (`AppTab.Psychics` in `ui/navigation/AppShell`): browse psychics grouped into themed carousels. Visual only in #36: nothing filters, navigates or starts a call/chat yet.

## What the user sees (top → bottom)

`PsychicsTopBar` (heart = favourites, centred title) → `PromoBanner` ("You have 3 minutes FREE / with 3 psychics") → `PsychicFilterRow` (All / Call / Chat `GlassPill`s, All selected) → one `PsychicsSection` per data section: badge + title + "View All", subtitle, full-width `LazyRow` of `PsychicCard`s. The shared bottom bar is drawn by `AppShell`, the screen only leaves 100dp clearance.

## Components (one per file)

- `PsychicCard` (168dp wide): `PsychicPhotoHeader` (photo or `ic_user` placeholder, bottom fade, `StatusChip`, name) + experience line, `RatingRow`, two `ChannelButton`s (Call/Chat; disabled when busy or the channel is unavailable), price ("3 free minutes / then $3,99/min").
- `PsychicsResources.kt`: `SectionIcon` → icon/tint, `PsychicPhoto` → drawable, `ScreenPadding`, `PsychicsPreview` helper.
- Reuses `ui/components/`: `appCard`, `GlassPill`, `TintedIconBox`.

## State & events

- `PsychicsScreen()` (stateful) loads `PsychicsRepository.psychicsData()` once and maps it with `toUiState()`.
- `PsychicsScreen(state, …)` (stateless) takes `PsychicsUiState(promo, selectedFilter, sections)` and callbacks `onFavouritesClick`, `onFilterSelect`, `onViewAllClick(sectionId)`, `onCallClick(psychicId)`, `onChatClick(psychicId)` — all no-ops for now.
- `PsychicFilter` (All/Call/Chat) is hoisted but static: always `All`, nothing is filtered.
- `PsychicsScreenTags`: test tags.

## Data

`data/psychics/` (`MockPsychicsRepository`). Photos are two bundled placeholders (`drawable/psychics_photo_*`).

## Stubs / TODO

Real filtering, favourites, View All screens, psychic profile, call/chat flows are out of scope (#36).

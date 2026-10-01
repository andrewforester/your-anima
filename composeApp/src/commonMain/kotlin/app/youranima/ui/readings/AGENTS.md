# ui/readings

The "Readings" tab (`AppTab.Readings` in `ui/navigation/AppShell`): a catalogue of self-development content — multi-day challenges and short quizzes in horizontal carousels. Visual only in #42: nothing is clickable, there are no callbacks.

## What the user sees (top → bottom)

Shared `ScreenTopBar` ("Readings", no icon) → "Improve yourself" (`ChallengeCard`s) → "Explore your potential" and "Attract love into your life" (`QuizCard`s). The page scrolls vertically, each carousel horizontally. The shared bottom bar is drawn by `AppShell`; the screen leaves 100dp clearance.

## Components (one per file)

- `ReadingsSection(id, title, items: LazyListScope.() -> Unit)`: title + full-width `LazyRow` (content padding 16, spacing 12).
- `ChallengeCard` (296×176, `appCard`): artwork on top (or a `cardGlow` radial glow when there is none), `surface` scrim over the bottom 112dp, clock chip "N-day challenge", 1-line title, 2-line `accentLavender` description.
- `QuizCard` (144 wide): square `appCard` tile with the artwork (112dp image) or a tinted 56dp line icon, 2-line title, "Quiz" + "N min" chips.
- `ReadingsChip`: shared `ui/components/TagChip` with a `primary` @ 20 % fill, optional clock icon (`drawable/readings_ic_clock`).
- `ReadingsResources.kt`: `ReadingArt` → `ArtVisual` (`Image` placeholder crop or `TintedIcon` drawable + tint), `ScreenPadding`, `ReadingsPreview` helper.

## State

- `ReadingsScreen()` (stateful) loads `ReadingsRepository.readingsContent()` once and maps it with `toUiState()`.
- `ReadingsScreen(state)` (stateless) renders `ReadingsUiState(challengeSection, quizSections)`.
- `ReadingsScreenTags`: test tags (screen, title, section/carousel/challenge/quiz by id).

## Data

`data/readings/` (`MockReadingsRepository`). Strings in `values/strings_readings.xml`.

## Stubs / TODO

- Artwork: four placeholder crops from the original screenshot (`drawable/readings_*`); others use existing line icons, including `home_ic_moon` / `home_ic_star` (rename to `ic_*` is a later Theme task, SPEC Decision 13).
- Challenge/quiz detail screens and any tap handling are out of scope (#42).

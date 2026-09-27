# ui/home

The "Today" tab: horoscope feed shown when `AppTab.Today` is selected in `AppShell` (`ui/navigation/`).

## Entry point

- `HomeScreen()` (stateful): loads `HomeRepository.homeData()` once, keeps the selected `ForecastPeriod` (`rememberSaveable`), maps to `HomeUiState` via `HomeData.toUiState()`. No longer owns nav/tab state or the bottom bar — those moved to `ui/components/AppBottomBar.kt` and `ui/navigation/AppShell.kt` (#35).
- `HomeScreen(state, onPeriodSelect, headerState, ...)` (stateless): one `verticalScroll` column — `HeroBackground` (scrolls away), `CollapsingProfileHeader`, then the feed: reading cards, `PinnedDateTabs`, `FocusMoodCard`, `CategoryRow`, `TipCard`, `YesNoCard`, `TarotCard`. `ContentBottomPadding` (100dp) reserves space so the last card isn't hidden under the shared bottom bar drawn by `AppShell`.

## Collapsing header (#47)

- `CollapsingHeaderState` (`rememberCollapsingHeaderState()`): holds the feed `ScrollState`; `fraction` 0 (expanded) → 1 (compact) = scroll / collapse range. The header writes the range (expanded − compact height) during its measure. Read `fraction`/scroll only in layout/draw lambdas: scrolling doesn't recompose.
- `CollapsingProfileHeader`: one custom `Layout` for both states. Avatar 100→36 (centre → left of the 56dp bar, eased out so it clears the name), name 24→16 (graphicsLayer scale, → right of the avatar), zodiac row → subtitle line (label colour lavender → muted via `ColorProducer`). Add-story, avatar thumb and Birth Chart fade out by `FADE_END` and are then not placed. Settings stays put. The node keeps its expanded height (the feed never moves); `HomeScreen` pins it with `offset { scroll }` + `zIndex(2)`. Only the visible part gets the background (alpha = fraction) and swallows taps. Pixel metrics: `HeaderMetrics`.
- `PinnedDateTabs`: wraps `DateTabs`; once the tabs reach the compact bar (+10dp) they are offset to stay there, get a solid background and swallow taps; `zIndex(1)` so the feed scrolls under them. Must be a direct child of the feed column right under the header (`pinShift` assumes that).

## Components

One file each: `TopBarButtons` (`AddStoryButton`, `AvatarThumb`, `SettingsButton`), `ProfileParts` (`ProfileAvatar`, `ProfileName`, `ZodiacRow`, `BirthChartPill`), `HeroBackground`, `ReadingCard`, `DateTabs`, `FocusMoodCard`, `CategoryCard` (+ `CategoryRow`), `TipCard`, `YesNoBlock`, `TarotCard`. `HomeResources.kt` holds shared label/color mappings (`ZodiacSign.label`, `MoodCategory.label/color`) and the preview helper `HomePreview`. Cards use the shared `appCard` modifier, the Birth Chart pill `GlassPill` and the reading-card chat icon `TintedIconBox`, all in `ui/components/` (moved there in #36).

## State & tags

- `HomeUiState` (`HomeUiState.kt`): immutable UI state for the feed only (user, readings, mood, categories, tip, yes/no). No longer carries nav selection or badges — see `ui/navigation/AppShell.kt` and `data/navigation/NavBadges.kt`.
- `HomeScreenTags`: test tags for this screen's own content.

## Data

Reads from `data/home/` (`HomeRepository`, mock `MockHomeRepository`). No stubs beyond the mock repository itself.

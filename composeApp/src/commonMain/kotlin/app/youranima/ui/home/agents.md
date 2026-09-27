# ui/home

The "Today" tab: horoscope feed shown when `AppTab.Today` is selected in `AppShell` (`ui/navigation/`).

## Entry point

- `HomeScreen()` (stateful): loads `HomeRepository.homeData()` once, keeps the selected `ForecastPeriod` (`rememberSaveable`), maps to `HomeUiState` via `HomeData.toUiState()`. No longer owns nav/tab state or the bottom bar — those moved to `ui/components/AppBottomBar.kt` and `ui/navigation/AppShell.kt` (#35).
- `HomeScreen(state, onPeriodSelect, ...)` (stateless): renders the scrollable feed — hero image, `TopBar`, `ProfileHeader`, reading cards, `DateTabs`, `FocusMoodCard`, `CategoryRow`, `TipCard`, `YesNoCard`, `TarotCard`. `ContentBottomPadding` (100dp) reserves space so the last card isn't hidden under the shared bottom bar drawn by `AppShell`.

## Components

One file each: `TopBar`, `ProfileHeader`, `ReadingCard`, `DateTabs`, `FocusMoodCard`, `CategoryCard` (+ `CategoryRow`), `TipCard`, `YesNoBlock`, `TarotCard`. `HomeResources.kt` holds shared label/color mappings (`ZodiacSign.label`, `MoodCategory.label/color`) and preview helpers (`HomePreview`, `homeCard` modifier).

## State & tags

- `HomeUiState` (`HomeUiState.kt`): immutable UI state for the feed only (user, readings, mood, categories, tip, yes/no). No longer carries nav selection or badges — see `ui/navigation/AppShell.kt` and `data/navigation/NavBadges.kt`.
- `HomeScreenTags`: test tags for this screen's own content.

## Data

Reads from `data/home/` (`HomeRepository`, mock `MockHomeRepository`). No stubs beyond the mock repository itself.

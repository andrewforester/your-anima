# data/readings

Mock data source for the Readings tab (`ui/readings/ReadingsScreen`), #42.

## Types (`ReadingsModels.kt`)

- `ReadingsContent`: aggregate returned by the repository — one `challengeSection` + `quizSections`.
- `ChallengeSection(id, title, challenges)` / `Challenge(id, days, title, description, illustration: ReadingArt?)`: multi-day challenges ("Improve yourself"). `illustration = null` → the card draws a glow.
- `QuizSection(id, title, quizzes)` / `Quiz(id, title, durationMinutes, illustration: ReadingArt)`: short quizzes.
- `ReadingArt`: artwork key (`Compass`, `WitchHat`, `Flame`, `HandsHeart` = bundled images; `Moon`, `Star`, `Heart`, `Spirit` = tinted line icons). The UI (`ui/readings/ReadingsResources.kt`) does the mapping.

## Repository

`ReadingsRepository.readingsContent()` is the interface a backend would implement. `MockReadingsRepository` is the only implementation: 4 challenges, "Explore your potential" (5 quizzes), "Attract love into your life" (3 quizzes). Items from the original screenshot plus invented ones (SPEC → Data).

## Stubs

- `ReadingArt` stands in for image URLs; the four images are placeholder crops of the original screenshot (`drawable/readings_*`).
- Titles and descriptions are plain mock strings (a backend would localize them).

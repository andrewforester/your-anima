# data/psychics

Mock data source for the Psychics tab (`ui/psychics/PsychicsScreen`).

## Types (`PsychicsModels.kt`)

- `PsychicsData`: aggregate returned by the repository — `promo` + `sections`.
- `FreeMinutesPromo(freeMinutes, psychicsCount)`: the "You have 3 minutes FREE / with 3 psychics" banner.
- `PsychicSection(id, title, subtitle, icon: SectionIcon, psychics)`: one themed carousel. `SectionIcon` (`Accurate`, `Love`, `Career`) is mapped to an icon + tint in the UI.
- `Psychic`: `name`, `photo: PsychicPhoto?` (null = placeholder), `status` (`Online`/`Busy`), `yearsOfExperience`, `rating` (0–5 Double), `reviewCount`, `canCall`/`canChat`, `freeMinutes`, `pricePerMinute` (preformatted string, e.g. "$3,99").

## Repository

`PsychicsRepository.psychicsData()` is the interface a backend would implement. `MockPsychicsRepository` is the only implementation: three sections × four psychics (Most Accurate and Best in Love Readings from the original screenshot, Career & Money invented).

## Stubs

- `PsychicPhoto` is an enum of two bundled placeholder photos (`drawable/psychics_photo_*`); a backend would replace it with an image URL.
- Section titles/subtitles and names are plain strings from the mock (a backend would localize them).

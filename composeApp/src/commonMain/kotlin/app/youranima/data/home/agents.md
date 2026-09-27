# data/home

Mock data source for the "Today" tab (`ui/home/HomeScreen`).

## Types (`HomeModels.kt`)

`UserProfile`, `ZodiacSign`, `ReadingOffer`/`ReadingType`, `ForecastPeriod`, `MoodScore`/`MoodCategory`, `CategoryForecast`/`ForecastCategory`, and `HomeData` (the aggregate returned by the repository). `HomeData` no longer holds nav badge counts — those moved to `NavBadges` in `data/navigation/` since the bottom bar is shared across tabs (#35).

## Repository (`HomeRepository.kt`, `MockHomeRepository.kt`)

`HomeRepository` is the interface a real backend would implement:
- `homeData(): HomeData` — the Today feed.
- `navBadges(): NavBadges` — badge counts for the shared bottom bar (psychics "Free" pill, chatroom unread count). Kept on this repository (rather than a new one) because it's the same mock/backend source; `AppShell` (`ui/navigation/`) is the only caller outside this screen.

`MockHomeRepository` is the only implementation: a single static `HomeData` plus a static `NavBadges(psychicsFree = true, unreadChats = 3)`. No backend yet — everything is hardcoded mock data.

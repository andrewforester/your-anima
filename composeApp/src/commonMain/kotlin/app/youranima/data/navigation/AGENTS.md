# data/navigation

`NavBadges(psychicsFree: Boolean, unreadChats: Int)`: badge counts shown on the shared bottom nav bar (`ui/components/AppBottomBar`) — the "Free" pill on Psychics and the unread count on Chatroom.

Split out of `data/home/` (#35) because the bar and its badges are shown for every tab (`ui/navigation/AppShell`), not just the Today screen. The values themselves still come from the existing mock: `MockHomeRepository.navBadges()` in `data/home/` returns this type. No separate repository for it — one mock source is enough until there's a backend.

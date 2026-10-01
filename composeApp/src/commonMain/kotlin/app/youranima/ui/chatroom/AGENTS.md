# ui/chatroom

The "Chatroom" tab (`AppTab.Chatroom` in `ui/navigation/AppShell`): the user's conversations with psychics under a promo card that points to the Psychics tab. Visual only in #50: nothing navigates, opens a chat or changes state.

## What the user sees (top → bottom)

Shared `ScreenTopBar` ("Chatroom", no icon) → `PsychicsPromoCard` ("Check our Psychics", "More than 900 psychics", rating, "See Psychics") → `ChatRow`s separated by 1dp `outline` dividers, on the page background. The whole page is one `LazyColumn`; the shared bottom bar is drawn by `AppShell`, the screen leaves 100dp clearance.

## Components (one per file)

- `PsychicsPromoCard`: `appCard` with `PromoAvatarStack` + title/subtitle/shared `RatingRow`, then a full-width shared `PrimaryButton` (no icon).
- `PromoAvatarStack`: three overlapping round photos (28/28/40dp in a 58×56 box) with 2dp `surface` rings.
- `ChatRow`: `StatusAvatar`, name (+ `CallBadge` when `canCall`), preformatted time, 2-line `accentLavender` preview, `UnreadBadge` when `unreadCount > 0`. One merged semantics node; not clickable.
- `StatusAvatar`: 48dp `ChatAvatarImage` with a status dot (online `accentTeal`, offline `onSurfaceMuted`) in a 12dp `background` ring.
- `ChatAvatarImage`: circle-clipped shared `PhotoOrPlaceholder` (icon at half the size).
- `CallBadge`: 16dp `accentLavender` circle with `psychics_ic_phone` tinted `background`.
- `UnreadBadge`: 18dp `accentOrange` pill (the nav `CountBadge` colour), "99+" above 99. Kept local: the nav badge is 14dp with another text style (SPEC Decision 14).
- `ChatroomResources.kt`: `ChatAvatar` → drawable, `ScreenPadding`, `ChatroomPreview` helper.

## State & events

- `ChatroomScreen()` (stateful) loads `ChatroomRepository.chatroomData()` once and maps it with `toUiState()`.
- `ChatroomScreen(state, onSeePsychicsClick)` (stateless) renders `ChatroomUiState(promo, chats)`; the callback is a no-op for now.
- `ChatroomScreenTags`: test tags (screen, title, promo, See Psychics, chat by id).

## Data

`data/chatroom/` (`MockChatroomRepository`). Strings in `values/strings_chatroom.xml`. Photos: `drawable/chatroom_*` placeholder crops, plus the Psychics photos Luna / Whimsy Lou.

## Stubs / TODO

- Conversation screen, sending messages, presence, navigation from "See Psychics" are out of scope (#50).
- `psychics_ic_phone` is used here under its Psychics name; renaming to `ic_phone` is a Theme task (SPEC Decision 14).
- The nav bar's Chatroom badge is not synced with the rows' unread counts.

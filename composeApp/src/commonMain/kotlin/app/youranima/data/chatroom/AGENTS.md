# data/chatroom

Mock data source for the Chatroom tab (`ui/chatroom/ChatroomScreen`), #50.

## Types (`ChatroomModels.kt`)

- `ChatroomData`: aggregate returned by the repository — the `promo` card + the `chats` list.
- `PsychicsPromo(psychicsCount, rating, reviewCount, avatars)`: "Check our Psychics" card; `avatars` = back-left, back-right, front (`null` = placeholder).
- `ChatPreview(id, psychicName, avatar, online, canCall, timeLabel, lastMessage, unreadCount)`: one conversation row. `canCall` drives the phone badge; `timeLabel` is a preformatted string.
- `ChatAvatar`: photo key (six chat crops, the promo front face, and the Psychics photos Luna / Whimsy Lou). The UI (`ui/chatroom/ChatroomResources.kt`) maps it to drawables.

## Repository

`ChatroomRepository.chatroomData()` is the interface a backend would implement. `MockChatroomRepository` is the only implementation: promo (900 psychics, 4.4, 1 000 324 reviews) and eight chats — six from the original screenshot (all offline, 1 unread, only Chandra can call) plus "Mystic Orion" (online, can call, read, placeholder avatar) and "Luna" (read) so the list scrolls and other states show.

## Stubs

- Timestamps are preformatted strings; there is no date library.
- `ChatAvatar` stands in for image URLs; the chat photos are placeholder crops of the original screenshot (`drawable/chatroom_*`).
- Online status and unread counts are static; nothing marks a chat read.

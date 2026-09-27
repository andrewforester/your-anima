# chatroom — design spec

The **Chatroom** screen: the tab "Chatroom" of the bottom nav bar. A promo card that sends the user to the psychics ("Check our Psychics") above the list of the user's chats with psychics (avatar, name, time, last message, unread count). New screen package `ui/chatroom/`.

**Out of scope** (don't build): the chat conversation screen, sending messages, online presence, navigation from SEE PSYCHICS, a bottom bar redesign. Visual only in this round: nothing on this screen changes state or navigates (Issue #48).

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the **original app** (not our Figma), received 2026-09-27 (Issue #48). No status bar drawn (top ≈60 dp empty); the bottom 50 px is the system navigation bar. The sixth chat (Lyrienne) is cut by the bottom bar.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp** (as Psychics and Readings). Screenshot measurements below are dp at that scale.
- **The screenshot sets content only.** Every size, colour, text style, radius and spacing below is the Figma/feed treatment (`docs/design/astrology-home/`, `docs/design/home-feed/SPEC.md`, `docs/design/psychics/SPEC.md`, `docs/design/readings/SPEC.md`, code in `ui/components/`, `ui/psychics/`). The original's text is ≈15–40 % larger (title ≈20 Bold, names ≈19, previews ≈17); it is deliberately scaled down to the feed's roles. Differences are listed in *Screenshot vs Figma*.
- Colours sampled with PIL (flat areas, brightest glyph pixels for text) only to identify roles.
- `assets/`: avatar crops (placeholders) and `ref-*.png` reference crops of each block (reference only, don't ship them).

## Colours

All existing `AppColors` tokens. **No new colour tokens.**

| Token | Hex | Status | Used for |
|---|---|---|---|
| `background` | `#0D0F2B` | existing | page background (original `#010827`), 2 dp ring around the status dot |
| `surface` | `#1A1D42` | existing | promo card (via `appCard`; original `#1D244B`), 2 dp ring around the promo avatars |
| `outline` | `#262954` | existing | card border (via `appCard`), row dividers (original `#242C50`), empty rating star, rating divider, avatar placeholder fill |
| `primary` | `#4D7CFF` | existing | "See Psychics" button fill (original `#5E66FD`) — the Psychics Call/Chat enabled fill |
| `onSurface` | `#FFFFFF` | existing | page title, promo title, button label, psychic names, unread-count digits |
| `accentLavender` | `#A1A5DB` | existing | promo subtitle (original `#90A2FF`), review count, message preview (original `#666C85`), call badge fill (original `#A9A1E8`) |
| `onSurfaceMuted` | `#797C9B` | existing | timestamps (original `#666C85`), offline status dot (original `#666C85`), placeholder icon |
| `accentTeal` | `#26D0CE` | existing | online status dot (as Psychics `StatusChip`) |
| `accentGold` | `#FFB84D` | existing | filled rating stars (original `#FFED4C`) |
| `accentOrange` | `#FF6B4A` | existing | unread-count badge fill (original `#FF5050`) — **the nav `CountBadge` colour**, so the row badge and the Chatroom tab badge match |

Card shadow: as `appCard` (`0 4 2 rgba(0,0,0,0.25)`).

## Typography

Only existing `appTypography` styles. **No new type tokens.**

| Style | Size / weight | Where | Original (≈) |
|---|---|---|---|
| `cardTitle` | 16 SemiBold | page title "Chatroom", promo title, psychic name | 20 Bold / 18 Medium / 19 Medium |
| `pill` | 13 SemiBold | "See Psychics" button label (as Call/Chat) | 16 Bold caps |
| `body` | 13 Medium | promo subtitle, message preview | 17 Regular / 17 Regular |
| `caption` | 11 Medium | review count, timestamp | 15 Medium / 13 Regular |
| `caption` + `FontWeight.SemiBold` | 11 SemiBold | unread-count digits (same override the nav bar uses for the selected label) | 13 Medium |

## Layout (top → bottom), width 402

Screen = `Box` with the flat `background` fill and one `LazyColumn` (the chat list can be long); the bottom bar comes from the app shell (see 6). Edge-to-edge: pad the top bar with `WindowInsets.statusBars`. `LazyColumn` content padding: bottom **100** (nav bar clearance, as home/Psychics/Readings).

### 1. Top bar
- Shared `ScreenTopBar(title = "Chatroom")`, no leading icon (exactly like Readings). Height 56, padding 20, `cardTitle` `onSurface`, centred.
- First item of the `LazyColumn`: scrolls with the content (no sticky header, as Psychics/Readings).

### 2. Promo card "Check our Psychics"
Item with padding horizontal 16, top 4 (feed column top padding). Full width 370, `appCard(PaddingValues(16.dp))`. Column, gap **12**: info row → button. Height ≈ 16 + 56 + 12 + 32 + 16 = **132** (original 139).

- **Info row**, vertically centred, gap 12:
  - **Avatar stack** — `Box` 58 × 56 (fixed), three circular photos, each with a 2 dp `surface` border (separates the overlaps; original has a faint dark ring). Drawn in this order (later on top), offsets from the box's top-left:
    1. back-left: 28 dp at (0, 12);
    2. back-right: 28 dp at (30, 0);
    3. front: 40 dp at (18, 16).
    Measured from the original: 26 / 28 / 39 dp at (0, 14) / (33, 0) / (18, 17) → rounded to the grid. Photos: `ContentScale.Crop`, `Alignment.TopCenter`, clipped to `CircleShape`; no photo → placeholder (see *Avatar*).
  - **Text column**, `weight(1f)`, gap 2:
    - "Check our Psychics" `cardTitle` `onSurface`, 1 line, ellipsis.
    - "More than 900 psychics" `body` `accentLavender`, 1 line, ellipsis (same pair as the Psychics `PromoBanner`).
    - Gap +2 (4 total). **Rating row**: the Psychics `RatingRow` (5 stars 12 dp gap 2, filled `accentGold` / empty `outline`, gap 6, divider 1 × 10 `outline`, gap 6, count `caption` `accentLavender`). Rating 4.4 → 4 filled + 1 empty (the original shows 4 full + 1 outline star). Count shown **"1 000 324"** — digits grouped by 3 with a space (see Decisions 5).
- **Button** "See Psychics": full width (338), the Psychics Call/Chat button in its enabled style **without an icon**: height 32, radius 16, fill `primary`, label `pill` `onSurface` centred, `role = Role.Button`, clickable no-op.
- **Zodiac-wheel decoration**: **omitted** (Decisions 3).
- The card itself is not clickable.

### 3. Chat list
Items directly under the promo card, **gap 8** between the card and the first row (original ≈4 + row padding). Rows are full screen width (402) on the page `background` (no card), each followed by a divider except the last.

#### Chat row
`Row`, padding horizontal 16, vertical 14, gap 12, `verticalAlignment = CenterVertically`. 16 + 48 + 12 + text (310) + 16 = 402 ✓. Height ≈ 14 + 57 + 14 ≈ **85** (original 91). Not clickable this round (hoist `onChatClick(chatId)` as a no-op, see Behaviour).

- **Avatar** 48 × 48 (original 47), circle, `ContentScale.Crop`, `Alignment.TopCenter`; no photo → placeholder. **Status dot** at the avatar's bottom-end: a 12 dp `background` circle (the ring) with a 8 dp dot centred in it, placed with its bottom-end on the avatar's bottom-end (original dot ≈9 dp with ≈2 dp ring, at the same spot). Dot colour: online `accentTeal`, offline `onSurfaceMuted`. All six chats in the screenshot are **offline**.
- **Text column**, `weight(1f)`, gap 4:
  - **Header row**, vertically centred:
    - Name `cardTitle` `onSurface`, 1 line, ellipsis, `weight(1f, fill = false)`.
    - Optional **call badge** (Chandra): gap 6, 16 dp circle `accentLavender`, `psychics_ic_phone` 10 dp tinted `background`, centred. `contentDescription` "Available for calls".
    - `Spacer(weight(1f))`, then gap ≥ 8 and the **timestamp** `caption` `onSurfaceMuted`, 1 line, no wrap ("02:54 pm", "Sept 25, 01:13 pm").
  - **Preview row**, vertically centred, gap 12:
    - Message preview `body` `accentLavender`, **max 2 lines**, ellipsis, `weight(1f)`.
    - **Unread badge** (only when `unreadCount > 0`): 18 dp high, `widthIn(min = 18.dp)`, horizontal padding 5, `CircleShape`/fully rounded, fill `accentOrange`, digits `caption` SemiBold `onSurface`, centred. "99+" above 99. When 0: no badge and no reserved space (the preview takes the width).
- **Divider**: 1 dp `outline`, full width (0 → 402, as in the original), between rows.

### 4. Scrolling
The whole page (top bar, promo card, rows) is one `LazyColumn`. The last row ends 100 dp above the screen bottom so it clears the bar.

### 5. Avatar (shared photo handling)
The Psychics photo handling (`PhotoOrPlaceholder` in `PsychicPhotoHeader`): image with `ContentScale.Crop`, `Alignment.TopCenter`; no photo → fill `outline` + `ic_user` centred `onSurfaceMuted`, icon = 50 % of the avatar size (24 dp in a 48 row avatar, 14 / 20 dp in the stack). Here always clipped to `CircleShape`.

### 6. Bottom nav bar
Not part of this screen: the app shell draws the shared `AppBottomBar` with `AppTab.Chatroom` selected and its existing count badge from `NavBadges.unreadChats`. **Not re-specified.** (The screenshot's bar differs: white selected label, lavender icons; ignore it.)

## Texts

New `values/strings_chatroom.xml`, prefix `chatroom_`:

| Key | Text |
|---|---|
| `chatroom_title` | Chatroom |
| `chatroom_promo_title` | Check our Psychics |
| `chatroom_promo_subtitle` | More than %1$d psychics |
| `chatroom_see_psychics` | See Psychics |
| `chatroom_cd_call_badge` | Available for calls |
| `chatroom_cd_online` | Online |
| `chatroom_cd_offline` | Offline |
| `chatroom_cd_unread` (plurals) | one: %1$d unread message · other: %1$d unread messages |
| `chatroom_unread_overflow` | 99+ |

The rating content description reuses `psychics_cd_rating` ("Rating %1$d of 5, %2$d reviews"); it moves with `RatingRow` (see *Shared components*).

Names, timestamps and message previews are **mock data** (see *Data*). From the screenshot (the part after "…" is invented to complete the truncated preview):

| Name | Time | Preview |
|---|---|---|
| Chandra (call badge) | 02:54 pm | hey, you're online and so am I. want a quick read on why this keeps pulling *you back? I'm free right now.* |
| Esther Eclipse | Sept 25, 01:13 pm | hi, I came across your profile and caught this sense that you've been *carrying something heavy lately. want to talk about it?* |
| Ana | Sept 24, 05:40 pm | Hi. I saw you join and I'm getting a clear "boundary test" vibe. Not nece*ssarily a bad thing, but someone is checking how far they can go. Want me to look closer?* |
| Miss Sheyna | Sept 24, 12:40 pm | hi, I saw you pop online and I pulled the Eight of Wands. this usually sho*ws news arriving fast. curious where it's coming from?* |
| Madam Sarah | Sept 23, 06:11 pm | Hi. This feels like a situation where you're doing everything right and it'*s still not moving. Let's see what's blocking it.* |
| Lyrienne | Sept 23, 06:00 pm | Hello. One quick pull. Page of Swords. This gives "watching, checking, rere*ading messages" energy. Is someone on your mind?* |

Casing kept verbatim (some psychics write lowercase). All six: unread 1, offline.

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Call badge icon | existing `psychics_ic_phone` | 10 dp, tint `background`, in a 16 dp `accentLavender` circle (move to `ic_phone`, see Shared) |
| Rating star | existing `psychics_ic_star_filled` | via `RatingRow` |
| Avatar placeholder | existing `ic_user` | `onSurfaceMuted` on `outline` |
| Status dot, unread badge | drawn in code | circles |
| Chat avatars | `assets/chat-chandra.png`, `chat-esther-eclipse.png`, `chat-ana.png`, `chat-miss-sheyna.png`, `chat-madam-sarah.png`, `chat-lyrienne.png` (100×100 each) | **placeholders** cropped at full resolution around each circle; the corners outside the circle and the original grey dot (bottom-right) are covered by the circle clip and our own dot. Ship as `drawable/chatroom_avatar_<name>.png` |
| Promo front avatar | `assets/promo-avatar-front.png` (84×84) | placeholder crop, ship as `drawable/chatroom_promo_avatar.png` |
| Promo back avatars | **reused** `psychics_photo_luna` (back-left) and `psychics_photo_whimsy_lou` (back-right) | the original back photos are half hidden and 26–28 dp (≈60 px), too poor to crop; reusing Psychics faces also ties the promo to the Psychics tab. No face in `docs/design/psychics/assets/` matches a chat psychic, so the chat avatars are all new crops |
| Reference crops | `assets/ref-promo-card.png`, `ref-chat-row.png`, `ref-bottom-bar.png` | reference only |

## States and behaviour

- **See Psychics**: clickable no-op, hoisted `onSeePsychicsClick` (navigation to the Psychics tab is out of scope).
- **Chat rows**: hoisted `onChatClick(chatId)` no-op; don't add a ripple-only `clickable` if the callback does nothing — either is fine, the developer's call (Psychics cards aren't clickable).
- **Avatars, call badge**: not clickable.
- **Unread badge**: shown when `unreadCount > 0`; nothing marks a chat read this round.
- **Status dot**: static from the mock (`online: Boolean`); no presence updates.
- **Nav badge**: stays the existing `NavBadges.unreadChats` mock; **not** synced with the rows' unread counts.
- **Scrolling**: vertical only; content is never hidden under the bottom bar (bottom padding 100).
- **Empty list** (not shown, not mocked): only the promo card. No empty-state text this round.
- **Semantics**: each row is one merged node (`mergeDescendants`): name, (call badge), time, preview, status, unread count.

## Data

Mock in `data/chatroom/` behind `ChatroomRepository` (+ `MockChatroomRepository`), plain Kotlin:

- `promo: PsychicsPromo`: `psychicsCount = 900`, `rating = 4.4`, `reviewCount = 1_000_324`, `avatars: List<ChatAvatar?>` (3: Luna, Whimsy Lou, promo front).
- `chats: List<ChatPreview>`: `id`, `psychicName`, `avatar` key or null, `online: Boolean`, `canCall: Boolean` (drives the call badge), `timeLabel: String` (**preformatted** "02:54 pm" / "Sept 25, 01:13 pm", like the Psychics price; a backend would send a timestamp and the UI format it), `lastMessage`, `unreadCount: Int`.
- The six chats of *Texts* (only Chandra `canCall`), plus **two invented** below them so the list scrolls and the other states are exercised:
  - "Mystic Orion" — online, `canCall`, "Sept 22, 09:15 pm", "Thanks for the chat today. Remember: the Moon in your sign makes this week great for new starts.", unread 0, placeholder avatar.
  - "Luna" — offline, "Sept 20, 11:02 am", "Your cards are ready whenever you are. Come back and we'll look at them together.", unread 0, avatar `psychics_photo_luna`.
- Avatar keys: an enum like `PsychicPhoto` (`ChatAvatar { Chandra, EstherEclipse, Ana, MissSheyna, MadamSarah, Lyrienne, PromoFront, Luna, WhimsyLou }`), mapped to drawables in `ui/chatroom`.

## Screenshot vs Figma (for information)

| Element | Original | Here |
|---|---|---|
| Page title | ≈20 Bold | `cardTitle` 16 SemiBold (`ScreenTopBar`) |
| Promo card | flat `#1D244B`, radius ≈20, no border, zodiac wheel | `appCard`, no decoration |
| Promo title / subtitle | ≈18 Medium / ≈17 `#90A2FF` | `cardTitle` / `body` `accentLavender` |
| Rating | stars 10 dp `#FFED4C`, count ≈15 white | `RatingRow` (12 dp gold stars, `caption` lavender count) |
| Button | 339 × 31, radius 16, `#5E66FD`, 16 Bold UPPERCASE | 338 × 32, radius 16, `primary`, `pill` "See Psychics" |
| Row | 91 high, avatar 47, name ≈19, preview ≈17 `#666C85` | ≈85, avatar 48, `cardTitle` / `body` `accentLavender` |
| Timestamp | ≈13 `#666C85` | `caption` `onSurfaceMuted` |
| Unread badge | 15 dp `#FF5050` | 18 dp `accentOrange` (nav badge colour) |
| Call badge | ≈17 dp `#A9A1E8` with white phone | 16 dp `accentLavender`, phone tinted `background` |
| Divider | 1 dp `#242C50`, full width | 1 dp `outline`, full width |

## Shared components

| Piece | Now in | What to do |
|---|---|---|
| `ScreenTopBar` | `ui/components` | reuse as is |
| `Modifier.appCard` | `ui/components` | reuse as is |
| `TagChip` | `ui/components` | **not used** (no chip on this screen; the unread badge is a count badge, not a chip) |
| `ChannelButton` | `ui/psychics` | **move** to `ui/components` as e.g. `PrimaryButton(label, onClick, modifier, icon: DrawableResource? = null, enabled = true)`; Psychics passes the icon, Chatroom doesn't |
| `RatingRow` (+ `psychics_cd_rating`) | `ui/psychics` | **move** to `ui/components`; add digit grouping of the count ("1 000 324", "1 204") in one shared formatter |
| `PhotoOrPlaceholder` | `ui/psychics/PsychicPhotoHeader.kt` (private) | **extract** to `ui/components` as `PhotoOrPlaceholder(painter: Painter?, iconSize, modifier)`; Psychics uses it full-bleed, Chatroom clipped to a circle |
| `CountBadge` | `ui/components/AppBottomBar.kt` (private, 14 dp) | optional: extract as `CountBadge(count, size)` shared by the nav bar (14) and the rows (18); or keep the row badge local — developer's call |
| `psychics_ic_phone` | drawable | rename to shared `ic_phone` (used by two screens) |
| Avatar with status dot | new | lives in `ui/chatroom` (Psychics shows status as a chip, not a dot on an avatar); move to `ui/components` when a second screen needs it |

## New tokens needed

None. The unread badge is the nav badge's `accentOrange`; everything else maps to existing colours, type styles and shapes. Radii 16 (button) and fully rounded circles are literals as in Psychics.

## Decisions (defaults, may be changed by the orchestrator)

1. **Type/control sizes**: every text maps to an existing feed style (table above); the original's larger type is scaled down.
2. **Unread badge colour** = `accentOrange`, the existing nav `CountBadge` colour, instead of the original's pure red; no new token.
3. **Zodiac-wheel decoration omitted**: no asset exists, a faint wheel adds a vector for little value, and no other `appCard` has a background decoration. If wanted later: a new single-colour `chatroom_zodiac_wheel.xml`, `onSurface` @ 5 %, 200 dp, anchored top-end and clipped by the card.
4. **Button label "See Psychics"** in title case (Psychics decision 3: no uppercase buttons; title case matches "View All").
5. **Review count grouping** with a space ("1 000 324") as in the original; implemented once in the shared `RatingRow`, so Psychics shows "1 204" the same way.
6. **Promo subtitle colour** `accentLavender`, matching the Psychics promo banner (the original's accent blue has no separate Figma role for subtitles).
7. **Message preview** `accentLavender` and **timestamp** `onSurfaceMuted`: two levels of secondary text (the original uses one grey for both).
8. **Rows on the page background with full-width dividers** (as in the original), not inside a card: a list of conversations, not a feed block.
9. **Call badge** = "can call this psychic" (`canCall`), not "verified". Only Chandra has it.
10. **Promo back avatars** reuse the Psychics photos Luna and Whimsy Lou; the front one and all chat avatars are placeholder crops.
11. **Timestamps preformatted** in the mock; no date formatting logic this round.
12. **Two extra mock chats** (Mystic Orion online/read, Luna read) so the list scrolls and online/no-badge states are visible.
13. **Behaviour visual only** (Issue): button, rows and avatars do nothing; nav badge not synced with unread counts.

## Open questions

1. Is the phone badge next to Chandra "available for calls" (chosen) or "verified"?
2. Should the unread badge be the original's red (new token `unreadBadge`) instead of `accentOrange`?
3. Keep or drop the zodiac-wheel decoration on the promo card?
4. Should the nav Chatroom badge equal the sum of unread counts once there is state?

# ui/components

Shared UI pieces used by more than one screen/tab (as opposed to `ui/<screen>/`, which is screen-local).

## AppTab (`AppTab.kt`)

`enum class AppTab { Today, Psychics, Compatibility, Chatroom, Readings }` — the app's top-level sections. Renamed from `HomeNavItem` and moved out of `ui/home/` (#35) so more than one screen can reference it; `ui/navigation/AppShell` switches on it to pick the visible content.

## AppBottomBar (`AppBottomBar.kt`, `AppBottomBarTags.kt`)

The bottom nav bar, shown once by `AppShell` under whichever tab is selected (previously duplicated per screen as `HomeBottomBar`). Stateless: `AppBottomBar(selected: AppTab, badges: NavBadges, onSelect: (AppTab) -> Unit)`. Badge counts (`NavBadges`, from `data/navigation/`) come from the caller, not hardcoded here. Icons: shared `ic_user`/`ic_heart`/`ic_message_circle` plus bar-only `home_ic_*`; labels and the "Free" badge text moved to the shared `composeResources/values/strings.xml` as `nav_*` keys.

## ComingSoonScreen (`ComingSoonScreen.kt`)

Placeholder for tabs without a real screen yet (no tab uses it since Compatibility got its screen in #51): centered title + "Coming soon" subtitle, theme tokens only. `ComingSoonScreen(title: String)` — the title string is passed in by `AppShell` per tab.

## appCard (`AppCard.kt`)

`Modifier.appCard(padding)`: the Figma card container (surface fill, 1dp `outline` border, radius 20, drop shadow). Was `homeCard` in `ui/home/`; moved in #36 because the Psychics banner and cards use it too.

## GlassPill (`GlassPill.kt`)

Glass pill (radius 20, glass fill/border, optional icon + `pill` label, content centred). `selected = null` → plain button (home "Birth Chart"); `true`/`false` → selectable tab, `true` drawn with the `primary` fill (Psychics All/Call/Chat filter). Moved from home's `BirthChartPill` in #36.

## TintedIconBox (`TintedIconBox.kt`)

Icon on a square container filled with the icon's tint at 10 % (`size`, `iconSize`, `shape` are parameters): home reading-card chat icon (42/12), Psychics promo gift (42/12) and section badges (28/8). Moved in #36.

## ScreenTopBar (`ScreenTopBar.kt`)

Tab-screen top bar: 56 high, padding 20, centred `cardTitle` title, optional `leading` icon (≤ `ScreenTopBarIconSize` = 22dp, the title is inset by the same width on both sides so it stays centred). `titleModifier` goes on the title text (test tags). Users: Psychics (heart, via `PsychicsTopBar`), Readings (no icon). Moved from `ui/psychics/PsychicsTopBar` in #42.

## TagChip (`TagChip.kt`)

Non-interactive chip: 20 high, radius 10, padding 8, gap 4, optional `leading` + `caption` label on a `fill` colour. Users: Psychics `StatusChip` (`backgroundDeep` @ 80 % + status dot), Readings `ReadingsChip` (`primary` @ 20 %, optional clock). Moved from `ui/psychics/StatusChip` in #42.

## PrimaryButton (`PrimaryButton.kt`)

32-high button, radius 16, `pill` label, optional 14dp leading `icon`: `primary` fill when `enabled`, glass + muted content (no clicks) when not. Users: Psychics Call/Chat (with icons), Chatroom "See Psychics" (no icon). Moved from `ui/psychics/ChannelButton` in #50.

## RatingRow (`RatingRow.kt`)

Five 12dp stars (rating rounded; `accentGold` / `outline`), a 1×10 divider and the review count grouped by thousands with a space (`groupedDigits()`: "1 000 324", "1 204"); read out as one phrase (`psychics_cd_rating`). Users: Psychics cards, Chatroom promo. Moved from `ui/psychics` in #50.

## PhotoOrPlaceholder (`PhotoOrPlaceholder.kt`)

A psychic photo (crop, top-aligned) filling its bounds, or an `outline` fill with a centred `ic_user` of `iconSize`. The caller clips it: Psychics card header full-bleed, Chatroom avatars circle. Extracted from `ui/psychics/PsychicPhotoHeader` in #50.

## HeroBackground (`HeroBackground.kt`)

The 402×420 hero sky (`home_hero_background`: gradient, moon, sparkles), scaled with the width, overflowing 70 left. `Modifier.inHeroFrame(left, top, width, height)` places a sibling in the same frame units (Compatibility comets). Users: home, Compatibility. Moved from `ui/home/` in #51.

## ProfileAvatar (`ProfileAvatar.kt`)

`ProfileAvatar(painter, size, contentDescription)`: circular avatar, `ProfileAvatarInset` (4 dp) transparent ring, `background` fill, crop. Users: home header (100, via `HomeProfileAvatar`), Compatibility "You" (140). Extracted from `ui/home/ProfileParts` in #51.

## Shared icons

`drawable/ic_heart`, `ic_message_circle`, `ic_user`, `ic_briefcase` (were `home_ic_*` / `home_ic_category_career`) are used by home, the bottom bar and Psychics; Theme owns `ic_*`.

## Stubs

`ComingSoonScreen` is currently unused (Psychics got its screen in #36, Readings in #42, Chatroom in #50, Compatibility in #51); kept until the orchestrator decides to drop it. `RatingRow` still uses the Psychics-named `psychics_cd_rating` / `psychics_ic_star_filled` (renaming is a Theme task).

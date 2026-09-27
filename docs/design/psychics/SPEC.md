# psychics — design spec

The **Psychics** screen: the tab "Psychics" of the bottom nav bar. A list of psychics grouped into themed sections, each a horizontal carousel of psychic cards, under a free-minutes promo and an All / Call / Chat filter. New screen package `ui/psychics/`.

**Out of scope** (don't build): "View All" screens, psychic profile, call/chat flows, a bottom bar redesign. Visual only in this round: nothing on this screen changes state or navigates (Issue #34).

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the **original app** (not our Figma), received 2026-09-27 (Issue #34). No status bar drawn (top ≈60 dp empty); the bottom 50 px is the system navigation bar.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp**. Screenshot measurements below are dp at that scale.
- **The screenshot sets content only.** Every size, colour, text style, radius and spacing below is the Figma/feed treatment (`docs/design/astrology-home/`, `docs/design/home-feed/SPEC.md` → *Figma style*, code in `ui/home/`). The original's text and controls are ≈20–40 % larger; they are deliberately scaled down to the feed's roles. Differences are listed in *Screenshot vs Figma*.
- Colours sampled with PIL (flat areas, brightest glyph pixels for text) only to identify roles.
- `assets/`: photo crops (placeholders) and `ref-*.png` reference crops of each block (reference only, don't ship them).

## Colours

All existing `AppColors` tokens. **No new colour tokens.**

| Token | Hex | Status | Used for |
|---|---|---|---|
| `background` | `#0D0F2B` | existing | page background |
| `surface` | `#1A1D42` | existing | promo banner, psychic cards (via `homeCard`), photo bottom gradient end |
| `outline` | `#262954` | existing | card borders (via `homeCard`), rating divider, empty rating stars, photo placeholder fill |
| `backgroundDeep` | `#0A0B21` | existing | status chip fill at 80 % alpha; bottom bar (existing) |
| `primary` | `#4D7CFF` | existing | selected filter pill fill, "View All", enabled Call/Chat button fill, "Most Accurate"… section icon (see Sections) |
| `onSurface` | `#FFFFFF` | existing | page title, banner title, section titles, psychic name, pill/button labels, heart icon |
| `accentLavender` | `#A1A5DB` | existing | banner subtitle, section subtitles, experience line, review count, "then $3,99/min" |
| `onSurfaceMuted` | `#797C9B` | existing | disabled Call/Chat label + icon, photo placeholder icon |
| `accentGold` | `#FFB84D` | existing | filled rating stars, "3 free minutes", busy status dot |
| `accentTeal` | `#26D0CE` | existing | online status dot |
| `accentPink` | `#FF52A3` | existing | gift icon + its 10 % container |
| `accentOrange` | `#FF6B4A` | existing | "Best in Love Readings" section icon (Love = orange, as in Focus & Mood and the category cards) |
| `accentPurple` | `#B388FF` | existing | "Most Accurate" section icon |
| `glassFill` / `glassBorder` | white 7 % / 10 % | existing | unselected filter pills, disabled Call/Chat buttons |

Card shadow: as `homeCard` (`0 4 2 rgba(0,0,0,0.25)`).

## Typography

Only existing `appTypography` styles. **No new type tokens.**

| Style | Size / weight | Where | Original (≈) |
|---|---|---|---|
| `cardTitle` | 16 SemiBold | page title "Psychics", banner title, section titles, psychic name | 20 / 18 / 18 / 16 Bold |
| `pill` | 13 SemiBold | filter pill labels, Call/Chat button labels, "View All", "3 free minutes" | 15 / 14 Bold caps / 16 / 13 |
| `body` | 13 Medium | banner subtitle, section subtitles, "N years of experience" | 15 / 16 / 13 |
| `caption` | 11 Medium | status chip label, review count, "then $3,99/min" | 13 / 13 / 13 |

## Layout (top → bottom), width 402

Screen = `Box` with the flat `background` fill and a scrolling content column; the bottom bar comes from the app shell (see 7). Edge-to-edge: pad the top bar with `WindowInsets.statusBars`.

### 1. Top bar
- Height 56, horizontal padding 20 (same as home `TopBar`).
- Left: `home_ic_heart` 22 dp, `onSurface`, clickable no-op (favourites). A 22 dp spacer on the right keeps the title centred.
- Centre: "Psychics", `cardTitle` `onSurface`, 1 line.
- The top bar scrolls with the content (like home; no sticky header in this round).

### 2. Content column
`verticalScroll`, horizontal padding 16, **gap 20** (feed column), top padding 4, bottom padding 100 (nav bar clearance, as home). Carousels break out of the 16 dp padding (full-width `LazyRow` with content padding 16, like `CategoryRow`).

### 3. Promo banner
- Full width (370), `homeCard(PaddingValues(horizontal = 16.dp, vertical = 12.dp))`, row, gap 12, vertically centred. Height = content (≈66).
- Icon container 42×42, `shapes.medium` (12), fill `accentPink` @ 10 %, gift icon 20 dp `accentPink` — exactly the reading card's chat-icon container.
- Text column, gap 2: "You have 3 minutes FREE" `cardTitle` `onSurface`; "with 3 psychics" `body` `accentLavender`. 1 line each, ellipsis.
- Not clickable.

### 4. Filter (All / Call / Chat)
- Row of 3 equal pills (`weight(1f)`), gap 8: 16 + 3 × ≈118.7 + 2 × 8 + 16 = 402 ✓.
- Pill = the Birth Chart pill style: radius 20, padding vertical 8, content centred, row gap 6 (icon 14 + label). Height ≈33.
  - Selected ("All"): fill `primary`, no border, label `onSurface`.
  - Unselected: `glassFill` + 1 dp `glassBorder`, label and icon `onSurface`.
- Labels `pill`: "All" (no icon), "Call" with `psychics_ic_phone` 14, "Chat" with `home_ic_message_circle` 14 (tinted).
- Semantics: `selectableGroup`, each pill `selectable(role = Role.Tab)`. All is selected; taps are no-ops this round (state hoisted, see Behaviour).

### 5. Section (×3, same component)
Column: header row → gap 4 → subtitle → gap 12 → carousel.
- **Header row**, vertically centred:
  - Icon badge 28×28, radius 8, fill = section tint @ 10 %, icon 16 dp tinted (same pattern as the banner's icon container, scaled down).
  - Gap 8, title `cardTitle` `onSurface`, `weight(1f)`, 1 line, ellipsis.
  - "View All" `pill` `primary`, clickable no-op (min touch height 32 via padding, no visual change).
- **Subtitle**: `body` `accentLavender`, max 2 lines, ellipsis. Full width.
- **Carousel**: `LazyRow`, full screen width, content padding horizontal 16, item spacing **12** (feed carousel). 16 + 168 + 12 + 168 + 12 = 376 → the third card peeks 26 dp, telling the user it scrolls (the original also cuts the third card).

### 6. Psychic card
Width **168** (feed cards are 144; two buttons don't fit in 144, see Decisions), height = content (all cards in a row the same height: every line is always laid out).
`homeCard(PaddingValues())` + `clip(shapes.large)`; the photo is full-bleed at the top, clipped by the card's 20 dp top corners.

- **Photo** 168 × 152, `ContentScale.Crop`, `Alignment.TopCenter`.
  - Bottom gradient: vertical `transparent` → `surface`, over the bottom 56 dp, so the photo melts into the card body.
  - **Name** over the gradient: `cardTitle` `onSurface`, bottom-start, padding start/end 12, bottom 8, 1 line, ellipsis.
  - **Status chip** top-start, offset 8/8: height 20, radius 10, fill `backgroundDeep` @ 80 %, padding horizontal 8, row gap 4: dot 6 dp (online `accentTeal`, busy `accentGold`) + label `caption` `onSurface` ("online" / "busy").
  - No photo → placeholder: fill `outline`, `home_ic_user` 48 dp `onSurfaceMuted` centred (name, chip and gradient unchanged).
- **Body**: column, padding horizontal 12, top 8, bottom 12.
  - "4 years of experience": `body` `accentLavender`, 1 line, ellipsis.
  - Gap 4. **Rating row**, vertically centred: 5 stars 12 dp, gap 2 (filled `accentGold`, empty `outline`; round the rating to whole stars), gap 6, divider 1 × 10 `outline`, gap 6, review count `caption` `accentLavender`.
  - Gap 12. **Buttons** row: Call and Chat, `weight(1f)` each, gap 8 (12 + 68 + 8 + 68 + 12 = 168 ✓). Height 32, radius 16, row gap 4 centred: icon 14 + label `pill`.
    - Enabled: fill `primary`, icon + label `onSurface`.
    - Disabled (psychic busy, or that channel unavailable): `glassFill` + 1 dp `glassBorder`, icon + label `onSurfaceMuted`, `enabled = false`.
    - Icons: Call `psychics_ic_phone`, Chat `home_ic_message_circle`.
  - Gap 8. Price, centred, gap 0: "3 free minutes" `pill` `accentGold`; "then $3,99/min" `caption` `accentLavender`. 1 line each.
- Card height ≈ 152 + 8 + 16 + 4 + 14 + 12 + 32 + 8 + 17 + 14 + 12 ≈ 289.
- The card itself is not clickable this round (profile is out of scope).

### 7. Bottom nav bar
Not part of this screen: the app shell (`ui/navigation/AppShell`, #35) draws the shared `ui/components/AppBottomBar` over every tab and selects `AppTab.Psychics`. The screen only keeps its content clear of the bar (bottom padding, as `HomeScreen`). **Not re-specified.** (The screenshot's bar differs; ignore it.)

## Texts

New `values/strings_psychics.xml`, prefix `psychics_`:

| Key | Text |
|---|---|
| `psychics_title` | Psychics |
| `psychics_promo_title` | You have %1$d minutes FREE |
| `psychics_promo_subtitle` | with %1$d psychics |
| `psychics_filter_all` | All |
| `psychics_filter_call` | Call |
| `psychics_filter_chat` | Chat |
| `psychics_view_all` | View All |
| `psychics_status_online` | online |
| `psychics_status_busy` | busy |
| `psychics_experience` (plurals) | one: %1$d year of experience · other: %1$d years of experience |
| `psychics_free_minutes` | %1$d free minutes |
| `psychics_price_per_minute` | then %1$s/min |
| `psychics_action_call` | Call |
| `psychics_action_chat` | Chat |
| `psychics_cd_favourites` | Favourites (content description of the heart) |
| `psychics_cd_rating` | Rating %1$d of 5, %2$d reviews (content description of the rating row) |

The filter and action labels are the same words; keep two keys (they can diverge), or reuse one — the developer's call.

Section titles/subtitles, names and prices are **mock data** (see *Data*). Verbatim from the screenshot:
- "Most Accurate" — "Psychics recognized for their exceptional accuracy in predictions and guidance"
- "Best in Love Readings" — "Psychics who have earned their reputation as experts in love questions and relationship mending"
- Names: "Luna" (partial: "…s Luna", use "Luna"), "Whimsy Lou", "Shenay" (partial, "Shenay…" — use "Shenay").
- "4 years of experience", "9 years of experience", ratings with counts 45 and 436, "3 free minutes", "then $3,99/min" (comma decimal kept verbatim; the price is a preformatted mock string).

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Heart (favourites) | existing `home_ic_heart` | tint `onSurface`, 22 dp |
| Gift | **new** `psychics_ic_gift.xml` | 24 viewport line icon, stroke 2, round caps: box body 16×10 at the bottom, lid 18×4 on top, vertical ribbon through the middle, two bow loops above the lid. Single colour (tinted `accentPink`). Reference `assets/ref-gift.png` (original is a pink/yellow filled gift) |
| Phone | **new** `psychics_ic_phone.xml` | 24 viewport, the Lucide/Feather `phone` handset outline, stroke 2, round caps — same family as the home icons |
| Chat | existing `home_ic_message_circle` | tinted |
| Target ("Most Accurate") | **new** `psychics_ic_target.xml` | 24 viewport: two concentric circles r 10 / r 6 + a dot r 2, stroke 2, and a diagonal arrow from the centre to the top-right with a small flight. Reference `assets/ref-badge-accurate.png` |
| Heart ("Best in Love") | existing `home_ic_heart` | tinted `accentOrange`, 16 dp |
| Briefcase (extra section) | existing `home_ic_category_career` | tinted `primary`, 16 dp |
| Rating star | **new** `psychics_ic_star_filled.xml` | the path of `home_ic_star` as a **fill** (no stroke), 20 viewport; tinted gold / `outline` |
| Status dot | drawn in code | 6 dp circle |
| Photo placeholder | existing `home_ic_user` | 48 dp `onSurfaceMuted` on `outline` |
| Photos | `assets/psychic-luna.png` (300×235), `assets/psychic-whimsy-lou.png` (230×241) | **placeholders** cropped from the screenshot at full resolution, below the status chip and above the baked-in name. Ship as `drawable/psychics_photo_luna.png`, `psychics_photo_whimsy_lou.png`. `assets/psychic-shenay-partial.png` is only 65 dp wide: reference only, **not shipped** (see Decisions) |
| Reference crops | `assets/ref-*.png` | banner, filter, section header, card, disabled buttons, busy chip, section badges — reference only |

All vectors: plain single-colour paths (`docs/COORDINATION.md`: no `aapt:attr` gradients), tinted in code.

## States and behaviour

- **Filter**: static this round — "All" selected, taps do nothing. Hoist it anyway: `selectedFilter: PsychicFilter` in the UI state + `onFilterSelect` no-op callback, so filtering can be added later (Call → only psychics who can call, etc.).
- **Heart, View All, Call, Chat**: clickable no-ops with hoisted callbacks (`onFavouritesClick`, `onViewAllClick(sectionId)`, `onCallClick(psychicId)`, `onChatClick(psychicId)`). Disabled buttons don't fire.
- **Busy**: status chip "busy" with the gold dot, **both** buttons disabled.
- **Online**: each button enabled per channel (`canCall`, `canChat`); in the screenshot an online psychic can have Call disabled and Chat enabled.
- **Scrolling**: the page scrolls vertically; each carousel scrolls horizontally, no snapping.
- **Bottom bar**: Psychics tab selected. Tab navigation between Today and Psychics is a separate infra Issue.

## Data

Mock in `data/psychics/` behind `PsychicsRepository` (+ `MockPsychicsRepository`), plain Kotlin:

- `promo`: `freeMinutes = 3`, `psychicsCount = 3`.
- `sections: List<PsychicSection>`: `id`, `title`, `subtitle`, `icon` key (`Accurate`, `Love`, `Career`), `psychics: List<Psychic>`.
- `Psychic`: `id`, `name`, `photo` key or null (placeholder), `status` (`Online` / `Busy`), `yearsOfExperience`, `rating` (0–5, Double), `reviewCount`, `canCall`, `canChat`, `freeMinutes`, `pricePerMinute` (preformatted String "$3,99").
- Mock sections (≥ 4 psychics each, so every carousel scrolls):
  1. Most Accurate (`accentPurple`, target): Luna (online, 1 yr, 4★, 45, call ✗ chat ✓, photo), Whimsy Lou (online, 4 yrs, 4★, 436, ✓✓, photo), Shenay (online, 9 yrs, 5★, 1 204, call ✗ chat ✓, placeholder), + 1 plausible (busy).
  2. Best in Love Readings (`accentOrange`, heart): 4 plausible psychics, the first busy (as in the screenshot), rest online; reuse the two photos, others placeholder.
  3. **Career & Money** (`primary`, briefcase) — invented: "Advisors who help with work, business and financial decisions". 4 plausible psychics.

## Screenshot vs Figma (for information)

| Element | Original | Here |
|---|---|---|
| Page title | ≈20 Bold | `cardTitle` 16 SemiBold |
| Banner | flat `#1B213D`, radius ≈12, 16/15 Bold/Regular text, multicolour gift | `homeCard`, pink icon container, `cardTitle` + `body` |
| Filter pills | 25 dp high, radius ≈8, selected `#18256B`, unselected outlined | ≈33 high, radius 20, selected `primary`, unselected glass |
| Section title / subtitle / View All | ≈18 Bold / 16 Regular `#666C85` / 16 `#90A2FF` | `cardTitle` / `body` `accentLavender` / `pill` `primary` |
| Section badge | 24 dp, lavender or dark fill | 28 dp, tint @ 10 % |
| Card | 175 × 319, outline `#374278`, no fill, gap 8 | 168 × ≈289, `homeCard`, gap 12 |
| Call / Chat | 72 × 24, `#38B6D2`, 13 Bold UPPERCASE | 68 × 32, `primary`, `pill` "Call"/"Chat" |
| Price | "3 free minutes" 13 `#FFD166`, "then…" 13 `#9FA1A9` | `pill` `accentGold`, `caption` `accentLavender` |
| Experience | "N year on Nebula" / "N years of experience" `#90A2FF` | always "N years of experience", `accentLavender` |
| Busy dot | `#FF8B00` | `accentGold` |

## Shared components

Pieces now needed by two screens; move them to `ui/components/` in a Theme-zone PR **before** (or as part of) the psychics screen Issue, don't copy:

| Now in | Component | Suggested shared name |
|---|---|---|
| `ui/home/HomeResources.kt` | `Modifier.homeCard(padding)` | `Modifier.appCard(padding)` |
| `ui/home/ProfileHeader.kt` `BirthChartPill` | glass pill (radius 20, glass fill/border, padding 16×8, icon + `pill` label) | `GlassPill` (with a `selected` variant = `primary` fill for the filter) |
| `ui/home/ReadingCard.kt` `AskButton` | glass button | fold into the Call/Chat button as its disabled style, or share `GlassButton` |
| `ui/home/ReadingCard.kt` `QuestionRow` icon box | tinted icon container (size, radius, tint @ 10 %) | `TintedIconBox(size, radius, tint, icon)` — banner 42/12, section badge 28/8 |
| `ui/home/CategoryCard.kt` `CategoryRow` | full-width carousel (content padding 16, spacing 12) | `CardCarousel` (optional; it's 5 lines) |
| drawables `home_ic_heart`, `home_ic_message_circle`, `home_ic_user`, `home_ic_category_career` | icons used by both screens | `ic_heart`, `ic_message_circle`, `ic_user`, `ic_briefcase` (Theme owns `ic_*`) |

## New tokens needed

None. Everything maps to existing colours, type styles and shapes (`shapes.medium` 12, `shapes.large` 20, pill radius 20/16/10 as literals like the existing pill). If the theme owner prefers, add dimension tokens for the recurring pill radius 20 and the carousel card width.

## Decisions (defaults, may be changed by the orchestrator)

1. **Type/control sizes**: every text maps to an existing feed style (table above); page title = `cardTitle` 16 (no page title exists in the feed; a new 18–20 `screenTitle` token is the alternative).
2. **Card width 168** instead of the feed's 144: two buttons need ≥ 64 dp each. Spacing/padding are the feed's.
3. **Button labels "Call" / "Chat"** in sentence case (Figma buttons "Ask", "Birth Chart" aren't uppercase).
4. **Enabled button fill `primary`** (the original's cyan has no Figma role; `accentTeal` is a data colour for Health). Disabled = glass + muted.
5. **Experience wording**: always "N years of experience" (plurals); "on Nebula" is another app's brand.
6. **Photos**: full crops for Luna and Whimsy Lou (placeholders); every other psychic, including partial Shenay, uses the neutral placeholder (`outline` + `home_ic_user`). Real photos come with a backend.
7. **Extra section**: one, "Career & Money".
8. **Rating**: whole stars (no half star); the exact rating isn't shown as a number.
9. **Status chip label** is `onSurface`, only the dot is coloured (original colours the text too).
10. **Top bar** scrolls with the content; no sticky header.
11. **Filter** static: All selected, taps are no-ops (Issue).

12. **Orchestrator review (2026-09-27):** all defaults above accepted. Bottom bar already shared by #35 (`AppBottomBar`, `AppTab`, `data/navigation/NavBadges`). The remaining shared pieces (`appCard`, `GlassPill`, `TintedIconBox`, shared `ic_*` icons) are moved to `ui/components/` by the screen Issue #36 itself, home keeps looking identical.

## Open questions

1. Should the page title get its own larger token (18–20 SemiBold) instead of `cardTitle` 16?
2. Call/Chat enabled colour: `primary` (chosen) or a teal/cyan CTA colour like the original?
3. Which sections exist beyond the two in the screenshot?
4. What do Call/Chat do when the promo's free minutes are used up — is "3 free minutes" per psychic or per account?

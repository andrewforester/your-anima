# readings — design spec

The **Readings** screen: the tab "Readings" of the bottom nav bar. A catalogue of self-development content: a carousel of multi-day **challenges** ("Improve yourself") and themed carousels of short **quizzes** ("Explore your potential", "Attract love into your life"). New screen package `ui/readings/`.

**Out of scope** (don't build): challenge and quiz detail screens/flows, a bottom bar redesign, the chat badge. Visual only in this round: no card, chip or section is clickable (Issue #41).

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the **original app** (not our Figma), received 2026-09-27 (Issue #41). No status bar drawn (top ≈60 dp empty); the bottom 50 px is the system navigation bar. The "Attract love into your life" section is cut off by the bottom bar.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp**. Screenshot measurements below are dp at that scale.
- **The screenshot sets content only.** Every size, colour, text style, radius and spacing below is the Figma/feed treatment (`docs/design/astrology-home/`, `docs/design/home-feed/SPEC.md` → *Figma style*, `docs/design/psychics/SPEC.md`, code in `ui/components/`, `ui/home/`, `ui/psychics/`). The original's text is ≈40 % larger (section titles ≈28 Bold); it is deliberately scaled down to the feed's roles. Differences are listed in *Screenshot vs Figma*.
- Colours sampled with PIL (flat areas) only to identify roles.
- `assets/`: illustration crops (placeholders, background keyed out to transparency) and `ref-*.png` reference crops of each block (reference only, don't ship them).

## Colours

All existing `AppColors` tokens. **No new colour tokens.**

| Token | Hex | Status | Used for |
|---|---|---|---|
| `background` | `#0D0F2B` | existing | page background (original `#010827`) |
| `surface` | `#1A1D42` | existing | challenge cards and quiz tiles (via `appCard`), challenge text-scrim gradient end (original cards `#141A52` → `#0B1236`) |
| `outline` | `#262954` | existing | card borders (via `appCard`) |
| `primary` | `#4D7CFF` | existing | tag chip fill at **20 %** alpha (original chips `#333FA5`) |
| `onSurface` | `#FFFFFF` | existing | page title, section titles, card titles, chip labels and clock icon |
| `accentLavender` | `#A1A5DB` | existing | challenge description (original `#4F546D`-ish grey on the scrim) |
| `cardGlow` | `#252B78` | existing | radial glow of a challenge card without illustration (Tarot Insight treatment) |
| `accentPurple` / `accentPink` / `accentGold` | `#B388FF` / `#FF52A3` / `#FFB84D` | existing | tints of the fallback icons on quiz tiles without an illustration (see Icons) |

Card shadow: as `appCard` (`0 4 2 rgba(0,0,0,0.25)`).

## Typography

Only existing `appTypography` styles. **No new type tokens.**

| Style | Size / weight | Where | Original (≈) |
|---|---|---|---|
| `cardTitle` | 16 SemiBold | page title "Readings", section titles, challenge title, quiz title | 22 / 20 / 20 / 16 Bold |
| `body` | 13 Medium | challenge description | 15 Regular |
| `caption` | 11 Medium | tag chip labels ("5-day challenge", "Quiz", "3 min") | 13 / 13 Medium |

## Layout (top → bottom), width 402

Screen = `Box` with the flat `background` fill and a scrolling content column; the bottom bar comes from the app shell (see 6). Edge-to-edge: pad the top bar with `WindowInsets.statusBars`.

### 1. Top bar
- The Psychics top bar **without the heart**: height 56, horizontal padding 20, "Readings" `cardTitle` `onSurface`, centred, 1 line, ellipsis. No icons (no spacers needed; the title is `fillMaxWidth` + `TextAlign.Center`).
- Scrolls with the content (no sticky header, as Psychics).

### 2. Content column
`verticalScroll`, horizontal padding 16, **gap 20** between sections (feed column), top padding 4, bottom padding 100 (nav bar clearance, as home/Psychics). Carousels break out of the 16 dp padding (full-width `LazyRow`, content padding horizontal 16, like `PsychicsSection`).

### 3. Section (×3, same frame)
Column: title → gap 12 → carousel.
- **Title**: `cardTitle` `onSurface`, padding horizontal 16, 1 line, ellipsis. No icon badge, no subtitle, no "View All" (none in the original).
- **Carousel**: `LazyRow`, content padding horizontal 16, item spacing **12**, no snapping.
- Section 1 holds challenge cards (4), sections 2–3 hold quiz cards (5).

### 4. Challenge card ("Improve yourself")
Width **296**, height **176** (original 297 × 178). 16 + 296 + 12 = 324 → the next card peeks 78 dp ✓ (as in the original).
`appCard(PaddingValues())` + `clip(shapes.large)`; layers bottom → top:
1. **Illustration**: top-centre, padding top 8, height 64 (width by aspect ≈ 150), `ContentScale.Fit`. No illustration → a radial glow `cardGlow` → transparent, radius ≈ 160, centred at the card's top-centre (the Tarot Insight glow, flipped).
2. **Scrim**: vertical gradient `surface` @ 0 % → `surface` @ 100 % over the card's bottom 112 dp, so the text never sits on the illustration's colours.
3. **Content**: column aligned bottom-start, padding 16, full width:
   - **Tag chip** (see 5) with the clock icon: "5-day challenge".
   - Gap 8. **Title** `cardTitle` `onSurface`, 1 line, ellipsis.
   - Gap 4. **Description** `body` `accentLavender`, `maxLines = 2`, `minLines = 2`, ellipsis.
   - Height 20 + 8 + 20 + 4 + 34 = 86 → content top at 176 − 16 − 86 = 74, just below the illustration (8 + 64 = 72) ✓.

### 5. Tag chip (shared, see Shared components)
Height 20, radius 10 (fully rounded), padding horizontal 8, row gap 4, vertically centred: optional icon 12 dp `onSurface` + label `caption` `onSurface`, 1 line. Fill `primary` @ 20 %, no border. Same geometry as the Psychics status chip. Not clickable.

### 6. Quiz card ("Explore your potential", "Attract love into your life")
Width **144** (feed carousel card), height = content; column, gap 8. 16 + 144 + 12 + 144 + 12 = 328 → the third card peeks 74 dp ✓.
- **Tile** 144 × 144 (square), `appCard(PaddingValues())` + `clip(shapes.large)`. Content centred:
  - illustration in a 112 × 112 box, `ContentScale.Fit`; or
  - no illustration → fallback line icon 56 dp, tinted (see Icons).
- **Title** `cardTitle` `onSurface`, `maxLines = 2`, `minLines = 2` (all cards the same height), ellipsis, full card width. Title sits **below** the tile, on the page background (as in the original).
- **Chips** row, gap 6: tag chip "Quiz" (no icon) + tag chip "3 min" (no icon).
- Height ≈ 144 + 8 + 40 + 8 + 20 = 220.

### 7. Bottom nav bar
Not part of this screen: the app shell (`ui/navigation/AppShell`) draws the shared `ui/components/AppBottomBar` over every tab with `AppTab.Readings` selected. **Not re-specified.** The screen only keeps its content clear of the bar (bottom padding 100).

## Texts

New `values/strings_readings.xml`, prefix `readings_`:

| Key | Text |
|---|---|
| `readings_title` | Readings |
| `readings_challenge_days` | %1$d-day challenge |
| `readings_quiz` | Quiz |
| `readings_duration_minutes` | %1$d min |

`readings_challenge_days` can be a plain string (the adjective form "5-day" has no plural). Section titles, card titles and descriptions are **mock data** (see *Data*). Verbatim from the screenshot:

- Sections: "Improve yourself", "Explore your potential", "Attract love into your life".
- Challenge 1: "5-day challenge", "Find your purpose", "Ever felt lost, different, or brimming with potential but unsure how to create a lif…" (truncated; full text in *Data*).
- Challenge 2 (cut): "12-da…", "Free y…", "When you… life can fe…" (completed in *Data*).
- Quizzes: "What is your Witch Type?", "What is your Shaman Path?", "What is your Femini…" (completed in *Data*); chips "Quiz", "3 min".

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Clock (challenge chip) | **new** `readings_ic_clock.xml` | 24 viewport, the Lucide/Feather `clock`: circle r 10 at (12,12) + hands polyline 12,6 → 12,12 → 16,14; stroke 2, round caps/joins, single colour (tinted `onSurface`), shown at 12 dp. Reference `assets/ref-challenge-card.png` |
| Find your purpose | `assets/challenge-find-purpose.png` (303×120) | **placeholder**: the compass top half (the rest is under the text in the original), keyed to transparency. Ship as `drawable/readings_challenge_find_purpose.png` |
| Free yourself… | none | not visible → `cardGlow` radial glow (see 4) |
| What is your Witch Type? | `assets/quiz-witch-type.png` (221×212) | **placeholder**, full crop, transparent. Ship as `drawable/readings_quiz_witch_type.png` |
| What is your Shaman Path? | `assets/quiz-shaman-path.png` (173×220) | **placeholder**, full crop (flame), transparent. Ship as `drawable/readings_quiz_shaman_path.png` |
| What is your Feminine Archetype? | existing `home_ic_moon` | only a sliver visible → fallback icon 56 dp tinted `accentPurple` |
| Love, card 1 (heart) | existing `ic_heart` | bottom cut by the bar → fallback icon 56 dp tinted `accentPink` |
| Love, card 2 (hands holding a heart) | `assets/love-hands-heart.png` (214×159) | **placeholder**, crop of the visible part (hands end at the wrists), transparent. Ship as `drawable/readings_love_hands_heart.png` |
| Love, card 3 (star) | existing `home_ic_star` | only a point visible → fallback icon 56 dp tinted `accentGold` |
| Reference crops | `assets/ref-*.png` | top bar, challenge card, quiz card, love row, (original) bottom bar — reference only |

The multicolour gradient crops are placeholders for real artwork from a backend/CMS; the fallback icons follow the feed's single-colour tinted line style. All new vectors: plain single-colour paths (`docs/COORDINATION.md`: no `aapt:attr` gradients), tinted in code.

## States and behaviour

- **Nothing is clickable** this round (Issue): no `clickable` on cards, chips, titles. Don't add no-op callbacks either (YAGNI); they come with the detail-screen Issue.
- **Scrolling**: the page scrolls vertically; each carousel scrolls horizontally, no snapping.
- **Text overflow**: all titles ellipsise; challenge description is always 2 lines tall; quiz titles are always 2 lines tall, so cards in a row keep one height.
- **Missing illustration**: challenge → glow; quiz → tinted fallback icon (driven by the mock's illustration key).
- **Bottom bar**: Readings tab selected (shell).

## Data

Mock in `data/readings/` behind `ReadingsRepository` (+ `MockReadingsRepository`), plain Kotlin:

- `ReadingsContent(challenges: List<Challenge>, quizSections: List<QuizSection>)`, with `challengesTitle` or a section object for "Improve yourself" (developer's call; the title is a mock string).
- `Challenge`: `id`, `days: Int`, `title`, `description`, `illustration` key or null.
- `QuizSection`: `id`, `title`, `quizzes: List<Quiz>`.
- `Quiz`: `id`, `title`, `durationMinutes: Int`, `illustration` key (an image, or a fallback icon + tint key).
- The UI maps illustration keys to drawables/tints (like Psychics `photo` / section `icon` keys).

Mock content (invented items marked *):

1. **Improve yourself**
   - 5 days, "Find your purpose", "Ever felt lost, different, or brimming with potential but unsure how to create a life that truly feels like yours? Discover what drives you, one day at a time." (compass)
   - 12 days, "Free yourself from the past"*, "When you let go of old hurts and patterns, life can feel lighter and new doors open. Release what holds you back." (no illustration)
   - 7 days, "Build daily confidence"*, "Small daily steps to trust yourself, speak up and stop waiting for permission." (no illustration)
   - 10 days, "Open your heart"*, "Heal old wounds and make room for the love you deserve." (no illustration)
2. **Explore your potential** (Quiz)
   - "What is your Witch Type?", 3 min (witch hat)
   - "What is your Shaman Path?", 3 min (flame)
   - "What is your Feminine Archetype?"*, 3 min (moon, purple)
   - "Which element rules you?"*, 2 min (star, gold)
   - "What is your spirit animal?"*, 4 min (user, lavender → `ic_user` `accentLavender`)
3. **Attract love into your life** (Quiz, invented, human's decision)
   - "What is your Love Language?"*, 3 min (heart, pink)
   - "Are you ready for new love?"*, 2 min (hands holding a heart)
   - "Which sign is your soulmate?"*, 3 min (star, gold)

## Screenshot vs Figma (for information)

| Element | Original | Here |
|---|---|---|
| Page title | ≈22 Bold | `cardTitle` 16 SemiBold |
| Section title | ≈20 Bold, gap to cards ≈17 | `cardTitle`, gap 12 |
| Section gap | ≈36 | 20 (feed column) |
| Challenge card | 297 × 178, radius ≈16, no border, gradient `#141A52` → `#0B1236` | 296 × 176, `appCard` (radius 20, border, shadow) + `surface` scrim |
| Challenge chip | 26 high, `#333FA5`, 13 Medium, clock 14 | 20 high, `primary` @ 20 %, `caption`, clock 12 |
| Challenge title / description | 20 Bold / 15 Regular grey | `cardTitle` / `body` `accentLavender` |
| Quiz card | tile 157 × 128, radius ≈16, `#141A52`, gap 12 | tile 144 × 144, `appCard`, gap 12 |
| Quiz title | 16 Bold | `cardTitle` 16 SemiBold |
| Quiz chips | 20 high, radius 10, `#333FA5`, 13 Medium | 20 high, radius 10, `primary` @ 20 %, `caption` |
| Illustrations | multicolour gradients | crops as placeholders; missing ones as tinted line icons |

## Shared components

Reuse:

| Where | Component | Use |
|---|---|---|
| `ui/components/AppCard.kt` | `Modifier.appCard(padding)` | challenge card, quiz tile |
| `ui/components/AppBottomBar` | bottom bar (shell) | Readings selected, not respecified |
| drawables `ic_heart`, `ic_user`, `home_ic_star`, `home_ic_moon` | fallback icons | `home_ic_star` / `home_ic_moon` are now used by two screens: rename to `ic_star` / `ic_moon` (Theme owns `ic_*`) |

Move to `ui/components/` (in the readings screen Issue, Psychics must look identical afterwards), don't copy:

| Now in | Component | Suggested shared name |
|---|---|---|
| `ui/psychics/PsychicsTopBar.kt` | centred-title top bar (56 high, padding 20, `cardTitle`) | `ScreenTopBar(title, modifier, leading: (@Composable () -> Unit)? = null)`; Psychics passes the heart, Readings nothing |
| `ui/psychics/StatusChip.kt` | 20-high chip, radius 10, padding 8, gap 4, `caption` label | `TagChip(label, fill, modifier, leading: (@Composable () -> Unit)? = null)`; Psychics: `backgroundDeep` @ 80 % + status dot, Readings: `primary` @ 20 % + clock / no icon |
| `ui/psychics/PsychicsSection.kt` + `ui/home` `CategoryRow` | full-width carousel (content padding 16, spacing 12) | `CardCarousel` (optional; now 3 users) |

## New tokens needed

None. Colours, type and shapes all map to existing tokens. The chip fill is `primary` at a 20 % alpha literal (like `TintedIconBox`'s 10 %); the theme owner may add a `chipFill` token if preferred.

## Decisions (defaults, may be changed by the orchestrator)

1. **Type/sizes**: every text maps to a feed style; page and section titles are `cardTitle` 16 (as Psychics), no new title token.
2. **Challenge card 296 × 176** (the original's proportions), content at the bottom over a `surface` scrim; illustration at the top.
3. **Quiz card 144 wide with a square 144 tile** (feed carousel width; the Issue asks for square tiles; the original tile was 157 × 128). Title and chips below the tile, on the page background, as in the original.
4. **Chip style** `primary` @ 20 %, no border, `caption` `onSurface`: the original's blue chip in Figma colours; same geometry as the Psychics status chip, merged into one shared `TagChip`.
5. **Second challenge**: "Free yourself from the past", 12 days, invented description; no illustration → `cardGlow` glow.
6. **Third quiz**: "What is your Feminine Archetype?", 3 min.
7. **Love section** (human's decision, invented): same quiz card, 3 cards — "What is your Love Language?" (heart), "Are you ready for new love?" (hands holding a heart), "Which sign is your soulmate?" (star); "Quiz" + 2–3 min chips.
8. **Illustrations**: full crops for witch hat, flame, hands-with-heart; the compass is only its top half (rest is under text in the original); heart, star and the Feminine card use tinted existing line icons (only partly visible in the screenshot).
9. **Extra mock items** beyond the screenshot (so every carousel scrolls): 2 challenges, 2 quizzes in "Explore your potential"; all invented, see *Data*.
10. **No section "View All"**, no badges, no subtitles (none in the original).
11. **Not clickable, no callbacks** (Issue: visual only).
12. **Top bar** scrolls with the content (as Psychics).

## Open questions

1. Should challenges and quizzes eventually open detail screens, and should "Quiz" / "3 min" chips ever be interactive (filters)?
2. Real illustrations: will they come as full-colour images from a backend (then the crops are fine as placeholders) or should they become tinted line icons like the feed categories?
3. Is a stronger page title (18–20 SemiBold) wanted across tab screens? (Same question as Psychics #1.)

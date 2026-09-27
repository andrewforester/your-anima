# home-feed — design spec

The part of the home screen that shows **after scrolling down** past the blocks already specified in `docs/design/astrology-home/` (reading cards, date tabs, Focus & Mood). It extends the same screen (`ui/home/`), not a new one.

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the original app (not our Figma), received 2026-09-27 (Issue #17). No status bar is drawn in it; the bottom 50 px (≈23 dp, `#151821`) is the system navigation bar.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp**. All values below are dp/sp at that scale, rounded to a 2/4 dp grid. "≈" means an estimate (antialiased edge, cut-off element or ±1 dp).
- Colours sampled with PIL (5×5 averages on flat areas; icon colours from the dominant stroke pixels).
- The original app's palette and font differ from our Figma-based theme. **Prefer the existing tokens**; the differences are listed next to each colour so the theme owner can decide.
- **Font:** the screenshot uses a rounded geometric sans (looks like *Maven Pro*), not Geist. Build with Geist (our theme); sizes below are measured from the screenshot's glyphs (cap ≈ 0.7 × size).
- `assets/` holds full-resolution crops (placeholders/reference, background included) and hand-traced SVG approximations; see *Icons and images*.

## Colours

Mapping column: *existing* = use that `AppColors` token as is; *existing ≠* = use the token, the screenshot differs by more than 3 % (flagged); *new* = see "New tokens needed".

| Token | Hex (ours) | Screenshot | Status | Used for |
|---|---|---|---|---|
| `background` | `#0D0F2B` | `#010827` | existing ≠ | page background under the feed |
| `surface` | `#1A1D42` | `#141A52` | existing ≠ | category cards, Focus & Mood card, Tarot card |
| `outline` | `#262954` | `#202874` | existing ≠ | 5 segment bars on the cut-off card (see Layout 2) |
| `backgroundDeep` | `#0A0B21` | `#0B1339` | existing ≠ | bottom nav (unchanged, not respecified) |
| `onSurface` | `#FFFFFF` | `#FFFFFF` | existing | all titles and body text, active tab, settings icon, tab indicator, lock glyph, badge glyphs |
| `onSurfaceMuted` | `#797C9B` | `#646C84` | existing ≠ | inactive tabs, header divider |
| `accentOrange` | `#FF6B4A` | `#F06050` | existing ≠ | Chatroom badge (nav; unchanged) |
| `headerCollapsed` | — | `#18256B` | **new** | collapsed sticky header background (name row + tabs) |
| `lockBadge` | — | `#323996` | **new** | 34 dp lock circle on locked category cards |
| `tipGradientStart` | — | `#051B6F` | **new** | tip banner gradient, left (flat until ≈40 %) |
| `tipGradientEnd` | — | `#36327A` | **new** | tip banner gradient, right edge |
| `yesGradientStart` / `yesGradientEnd` | — | `#26A09B` → `#2478A4` | **new** | Yes badge fill and ✓ list marks (teal top-right → blue bottom-left) |
| `noGradientStart` / `noGradientEnd` | — | `#B24044` → `#6C235F` | **new** | No badge fill and × list marks (red top-left → plum bottom-right) |
| `cardGlow` | — | `#252B78` | **new** | radial glow at the bottom-centre of the Tarot card (fades to `surface`) |

Fallback if the theme owner doesn't want gradients: Yes = `accentTeal`, No = `accentOrange`, tip banner = `surface`.

Illustration colours live inside the vector drawables, not in the theme:

| Drawable | Colours |
|---|---|
| Career briefcase | linear, top-right `#70DCEC` → bottom-left `#2494D4` |
| Love heart + orbit | linear, left `#F84840` → right `#F07450` |
| Health pills | linear `#54D87C` → `#1CD08C` (only the left third is visible) |
| Tip shooting stars | stars `#ECF074`, star "shadow" copies `#8E8C74` ≈70 %, comet tails `#D448BC` (tail tips `#E490A0`) |

No card has a border or a visible shadow.

## Typography

Measured in the screenshot; Geist at the same size will be slightly narrower.

| Style | Size / weight | Line height | Where | Mapping |
|---|---|---|---|---|
| `titleCollapsed` | 20 Bold (≈19.3) | normal | "Andrew" in the collapsed header | **new** (`name` is 24 SemiBold) |
| `tabLarge` | 18 Bold | normal | tabs in the collapsed header (active and inactive have the same weight) | **new**, or keep `tab` 14 Medium: open question 2 |
| `sectionTitle` | 18 Bold | 26 | category card titles, tip text "Let your adventures…", "Yes for today", "No for today" | **new** |
| `preview` | 16 Regular | 23 | category card preview text (2 lines, ellipsis) | **new** (`cardLead` is 16 Medium) |
| `bodyRegular` | 14 Regular | 18 | "Tip for the day", Yes/No list items | **new** (`tab` is 14 Medium) |
| `headline` | 22 Bold | normal | "Tarot Insight" | **new** |
| `caption` | 11 Medium | normal | nav labels | existing (screenshot ≈12; unchanged) |
| `badge` | 9 Bold | normal | nav "5" | existing (screenshot ≈11; unchanged) |

## Layout (top → bottom), width 402

Y positions are from the top of the screenshot (dp). Horizontal sums are checked against 402.

### 1. Collapsed sticky header (y 0–160)

Solid `headerCollapsed`, full width, drawn behind the status bar (edge-to-edge; pad with `WindowInsets.statusBars`). Column:

- Top: status-bar inset, then ≈24 dp spacing (in the screenshot the name row is centred at y ≈ 77).
- **Title row**, height 56, horizontal padding 20: "Andrew" `titleCollapsed` white, centred on the screen width. `settings` icon (existing `home_ic_settings`) 20 dp (glyph ≈18×19), white, aligned right (right edge at 382 → padding 20). No add-story or avatar thumbnail in this state.
- Gap ≈8.
- **Tab row**, height 48, scrollable horizontally (the 4th tab "Week" is cut off at the right edge):
  - Edge padding ≈8 at start; each tab = label + 16 horizontal padding on each side (measured 17). Label widths in the screenshot: Yesterday 89, Today 53, Tomorrow 91, Week (cut).
    Check: 8 + (16+89+16) + (16+53+16) + (16+91+16) + 16 = 353, then "Week" starts at ≈361 and is cut off at 402. ✓
  - Labels `tabLarge`: selected white, others `onSurfaceMuted`.
  - Label centre y ≈140; the indicator sits 10 dp below the label baseline.
  - **Indicator**: 2 dp, white, **full tab width** (87 dp for Today = 53 + 2×17), at the very bottom of the row, over the divider.
  - **Divider**: 1 dp full width, `onSurfaceMuted`, at the bottom of the header (y ≈158).

How it relates to the existing blocks: see *States and behaviour*.

### 2. Cut-off card (y ≈160–176)

The bottom 16 dp of a card peeks out below the header: x 16–386 (16 + 370 + 16 = 402 ✓), `surface`, radius ≈12. Its visible content is a row of 5 bars: 2 dp visible (the rest is under the header), `outline`-ish (`#202874`), each ≈65 wide with ≈5 gaps, inset 12 from the card sides (12 + 5×65 + 4×5 + 12 ≈ 369 ✓), bottom of the bars ≈14 above the card bottom.

In our app this is the **Focus & Mood card** (Issue #17). The original seems to show bars where we draw rings; don't add bars. See open question 3.

Gap to the next block: **32**.

### 3. Category forecast cards (y 208–390)

- `LazyRow`, content padding start **12** (measured; everything else uses 16, see open question 5), item spacing **24**. Card 3 starts at x ≈350 and is cut off: 12 + 144 + 24 + 144 + 24 + 54 visible = 402 ✓.
- **Card**: 144 × 182, `surface`, radius ≈12 (`shapes.medium`), no border, no shadow, padding 12 horizontal.
  - **Illustration** area 80 × 64 at (12, 22) inside the card, artwork centred in it (both icons' centres are at x ≈52, y ≈54 from the card's top-left). Artwork size: briefcase ≈57×47, heart ≈76×44, pills ≈52 high.
  - **Lock badge** (locked cards only): 34 dp circle `lockBadge`, top 16, right 12 (overlaps the illustration area's right side); `ic_lock` glyph 12×15 white, centred (use a 16 dp icon box).
  - **Title**: `sectionTitle` white, cap top ≈99 from the card top (text box top ≈95, i.e. ≈9 below the illustration area).
  - Gap ≈6.
  - **Preview**: `preview` white, max 2 lines, `TextOverflow.Ellipsis`, width 120 (144 − 2×12). Bottom padding ≈14 (≈17 to the descender).
- Gap to the next block: **32**.

### 4. Tip for the day banner (y 422–510)

- x 16–386 (370 × 88), radius ≈12, no border.
- Fill: horizontal linear gradient `tipGradientStart` (0 %–40 %) → `tipGradientEnd` (100 %). Vertical variation is under 2 %.
- Padding 12 vertical, 16 start.
- Text column (start-aligned):
  - "Tip for the day": `bodyRegular` white.
  - Gap ≈2.
  - "Let your adventures unfold naturally.": `sectionTitle` white, wraps to 2 lines (line pitch 26.6). In the screenshot line 1 ends at x 277. Give the text an end padding of ≈72 so the illustration never overlaps it; with Geist the wrap point may differ, which is fine.
- **Illustration** (shooting stars), ≈70 × 88, anchored top-right and bleeding to the card's top/right/bottom edges, clipped by the card shape (`Modifier.clip(shapes.medium)` on the card). Visible glyph extent is x 328–386, y 430–498.
- Gap to the next block: ≈**40** (≈32 + the list's leading).

### 5. Yes / No for today (y ≈550–690)

Two identical blocks stacked with gap ≈**30** (use 32). Each is a `Row` of two equal columns: padding 16, gap 8 (16 + 181 + 8 + 181 + 16 = 402 ✓; measured right column start 204 ≈ 205 ✓).

- **Left column**, vertically centred against the right column: row with gap 4:
  - Badge: 20 dp circle, gradient fill (Yes: `yesGradient*`, top-right → bottom-left; No: `noGradient*`, top-left → bottom-right), white glyph ≈10 dp (✓ stroke 2 or ×), centred.
  - Title: `sectionTitle` white. "Yes for today" / "No for today".
- **Right column**: 3 rows, line pitch 17.5 (`bodyRegular`, line height 18), no extra spacing:
  - Mark 14 dp box (✓ `ic-check-gradient` / × `ic-cross-gradient`), gap ≈3, then text `bodyRegular` white, 1 line. The longest item "Engage in physical activity" measures 159 dp in the screenshot and fits 181 − 17 = 164; use `maxLines = 2` just in case Geist is wider.
- Yes block y ≈550–605, No block y ≈634–689.
- Gap to the next block: ≈**36** (use 32 + list leading).

### 6. Tarot Insight card (y 725 → cut off by the nav at 792)

- x 16–386, `surface`, radius ≈12 at the top (bottom not visible).
- Radial glow: centre ≈(200, bottom of the visible part), colour `cardGlow`, radius ≈200, fading to `surface`. It could also be an image in the original (it's too blurred to tell).
- "Tarot Insight": `headline` white, padding start 12, text box top ≈24 (cap top at y 755).
- **Everything below the title is not visible**: open question 4. Don't invent content; render the title and leave a stub.

### 7. Bottom nav (y 792–869, then 23 dp system bar)

Already specified and built from `astrology-home`. **Don't respec it.** Differences seen here are open question 6. Nothing new to lay out; the feed needs bottom padding ≥ the nav height (≈77 + nav-bar inset), as in `astrology-home`.

## Texts

Keys go to the existing `values/strings_home.xml` (prefix `home_`). Tabs (`home_tab_*`) and `home_settings` already exist; add only these:

| Key (suggested) | Text |
|---|---|
| `home_category_career` | Career |
| `home_category_love` | Love |
| `home_category_health` | Health |
| `home_tip_label` | Tip for the day |
| `home_yes_title` | Yes for today |
| `home_no_title` | No for today |
| `home_tarot_title` | Tarot Insight |
| `home_cd_locked` | Locked (content description for the lock badge) |

Mock data (not strings resources, see *Data*), verbatim where visible:

- Career preview: "Today’s energy enhances your…" (typographic apostrophe; cut by ellipsis, real text unknown).
- Love preview: "In love, the Aries moon may spa…" (cut).
- Health preview: "The A… / encou…" (cut; only "Heal", "The A", "encou" visible).
- Tip: "Let your adventures unfold naturally."
- Yes: "Initiate new projects", "Plan spontaneous outings", "Engage in physical activity".
- No: "Rush decisions", "Ignore others' needs" (straight apostrophe in the screenshot), "Overcommit financially".
- Header: "Andrew" (same user name as `astrology-home`), tabs Yesterday / Today / Tomorrow / Week (same as existing).

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Settings (header) | existing `home_ic_settings` | 20 dp, white |
| Career briefcase | `assets/category-career-briefcase.svg` (traced), crop `category-career-briefcase.png` | outline, stroke ≈2.2, round joins; gradient in the SVG. Convert to `drawable/home_ic_category_career.xml` |
| Love heart with orbit | `assets/category-love-heart.svg` (traced), crop `category-love-heart.png` | heart outline + tilted ellipse orbit (≈−12°) + two 4-point sparkles (top-left, bottom-right). In the original, the orbit passes in front of the heart on the left and behind it on the right; the traced SVG draws it on top everywhere, which is acceptable. `home_ic_category_love.xml` |
| Health pills | crop `category-health-pills-partial.png` only (cut off) | two capsules crossing, outline, green gradient. Draw a vector in the same style: two rounded capsules ≈20×44 rotated ±45°, stroke 2.2, one with a divider line across its middle. `home_ic_category_health.xml`. Open question 1 |
| Lock | `assets/ic-lock.svg`, crop `lock-badge.png` | filled padlock 12×15 with keyhole. `home_ic_lock.xml` (or `ic_lock` via Theme if shared) |
| Tip shooting stars | `assets/tip-shooting-stars.svg` (approximation), crop `tip-shooting-stars.png` (**placeholder**, includes the banner background) | three 7-point yellow stars with offset grey copies, three magenta comet tails going up-right, three tiny stars. Prefer the SVG → `home_tip_stars.xml`; the PNG crop is for visual reference |
| ✓ list mark | `assets/ic-check-gradient.svg`, crop `yes-check.png` | `home_ic_check.xml` |
| × list mark | `assets/ic-cross-gradient.svg`, crop `no-cross.png` | `home_ic_cross.xml` |
| Yes / No badges | crops `yes-badge.png`, `no-badge.png` | draw in code: gradient circle + white ✓/× (reuse the check/cross paths in white) |
| Tarot glow | crop `tarot-card-top.png` | draw with `Brush.radialGradient` |
| Reference crops | `header-collapsed.png`, `cut-card-segments.png`, `tip-banner-full.png`, `nav-bar.png` | reference only |

## States and behaviour

### Sticky / collapsing header

The screenshot is the **scrolled** state of the same screen described in `astrology-home`:

- **Expanded** (scroll at top, `astrology-home`): hero gradient, TopBar (add-story, avatar thumb, settings), ProfileHeader (avatar, name 24, zodiac row, Birth Chart pill), reading cards, then DateTabs inside the scrolling content.
- **Collapsed** (this screenshot): once the ProfileHeader has scrolled out, a solid `headerCollapsed` bar is pinned at the top with the name centred and settings on the right. Add-story, avatar thumb, zodiac and Birth Chart are gone.
- **DateTabs are pinned** directly under the name row once they reach it, and stay pinned while the feed scrolls under them (the Focus & Mood card is visibly passing under the tabs).
- Suggested implementation: one `LazyColumn`. The hero, TopBar and ProfileHeader are ordinary items; DateTabs is a `stickyHeader`. A collapsed-title overlay (name + settings, `headerCollapsed` background) fades/slides in when `firstVisibleItemIndex` is past the ProfileHeader (derived state). The pinned tabs get the same `headerCollapsed` background and the divider only while pinned.
- The transition (instant vs animated fade/elevation) isn't visible in a static screenshot: open question 2.

### Other interactions

- **Tabs**: same hoisted selection state as the existing DateTabs; the tab row scrolls horizontally in this style. Content per tab: open question 7.
- **Category row**: horizontal scroll, no snapping visible. Tapping a card: locked → paywall/premium, unlocked → category detail. Neither exists yet: hoisted no-op `onCategoryClick(id)`.
- **Locked state**: the lock badge is shown when `isLocked`; the preview text is still shown (truncated). Unlocked cards: same card without the badge (assumed).
- **Tip banner, Yes/No**: static, not clickable (assumed).
- **Tarot card**: probably clickable (open question 4); hoisted no-op.
- **Settings** in the collapsed header: same callback as the expanded TopBar settings.

## Data

Mock in `data/home/`: extend `HomeData` / `HomeRepository.homeData()` and `MockHomeRepository`:

- `categories: List<CategoryForecast>`: `id`, `title`, `preview`, `isLocked`, icon key. Mock: Career (locked), Love (locked), Health (locked, preview text unknown, use a plausible one).
- `tipOfTheDay: String`.
- `yesForToday: List<String>` (3), `noForToday: List<String>` (3).
- `tarot`: title only for now (open question 4).
- User name is already in the home mock.

## New tokens needed

For a separate `theme` Issue. Names are suggestions.

| Kind | Name | Value |
|---|---|---|
| colour | `headerCollapsed` | `#18256B` |
| colour | `lockBadge` | `#323996` |
| colour | `tipGradientStart` / `tipGradientEnd` | `#051B6F` / `#36327A` |
| colour | `yesGradientStart` / `yesGradientEnd` | `#26A09B` / `#2478A4` |
| colour | `noGradientStart` / `noGradientEnd` | `#B24044` / `#6C235F` |
| colour | `cardGlow` | `#252B78` |
| type | `titleCollapsed` | 20 Bold |
| type | `tabLarge` | 18 Bold (only if open question 2 says to adopt the original tab style) |
| type | `sectionTitle` | 18 Bold, line height 26 |
| type | `preview` | 16 Regular, line height 23 |
| type | `bodyRegular` | 14 Regular, line height 18 |
| type | `headline` | 22 Bold |
| shape | none | radius ≈12 = existing `shapes.medium` (measured 12–14) |

Palette differences from the existing theme, left as is: page background `#010827` vs `background` `#0D0F2B`, card `#141A52` vs `surface` `#1A1D42`, inactive tab `#646C84` vs `onSurfaceMuted` `#797C9B`, nav `#0B1339` vs `backgroundDeep` `#0A0B21`.

## Open questions

1. **Categories beyond Health**: the row is cut after "Heal…". How many categories are there, and in what order (Family? Friends? Money?), with which icons and preview texts? The Health preview text is not readable.
2. **Collapsed header style**: adopt the original's tab style when pinned (18 Bold, scrollable, full-width white indicator, grey divider, `headerCollapsed` bg) or keep our Figma DateTabs (14 Medium, 4 fixed columns, 28 dp blue indicator)? Is the collapse animated (fade/slide) or instant?
3. **Card under the header**: the original shows 5 horizontal bars at the bottom of the card above the categories. Our Focus & Mood has rings. Is that card something else in the original (e.g. a progress/"energy" card)? We assumed it's Focus & Mood and ignore the bars.
4. **Tarot Insight**: everything below the title is cut off (cards? a CTA? locked?). Need a screenshot scrolled further, including what comes after it.
5. **Category row start padding**: measured 12 vs 16 elsewhere. Could be a slightly scrolled row. Use 16?
6. **Bottom nav differences** (not respecified): original icons are Today = constellation (4 dots joined by lines), Psychics = person with sparkles, Compatibility = two overlapping hearts, Chatroom = chat bubble with "…", Readings = open book. Active item is **white** (ours: blue `primary`); inactive icon/label **`#90A2FF`** (ours: `#797C9B`). Chatroom badge is **"5"** (ours "3") and Psychics has **no FREE badge**. Nav bg `#0B1339`. Should the nav follow the original?
7. **Tab content**: does switching Yesterday/Today/Tomorrow/Week change the whole feed (categories, tip, yes/no) or only some blocks?
8. **Unlocked category card**: what does it look like (badge hidden? full text?) and what does a tap on a locked one open?

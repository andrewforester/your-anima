# home-feed — design spec

The part of the home screen that shows **after scrolling down** past the blocks already specified in `docs/design/astrology-home/` (reading cards, date tabs, Focus & Mood). It extends the same screen (`ui/home/`), not a new one.

**Out of scope** (separate tasks later, don't build): the collapsed/sticky top header with pinned date tabs, and the bottom navigation bar. Both are visible in the screenshot but are not specified here. The existing TopBar, DateTabs and HomeBottomBar stay as they are.

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
| `onSurface` | `#FFFFFF` | `#FFFFFF` | existing | all titles and body text, lock glyph, badge glyphs |
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
| `sectionTitle` | 18 Bold | 26 | category card titles, tip text "Let your adventures…", "Yes for today", "No for today" | **new** |
| `preview` | 16 Regular | 23 | category card preview text (2 lines, ellipsis) | **new** (`cardLead` is 16 Medium) |
| `bodyRegular` | 14 Regular | 18 | "Tip for the day", Yes/No list items | **new** (`tab` is 14 Medium) |
| `headline` | 22 Bold | normal | "Tarot Insight" | **new** |

## Layout (top → bottom), width 402

Y positions are from the top of the screenshot (dp). Horizontal sums are checked against 402.

The blocks go **after the Focus & Mood card**, inside the same scrolling content column, gap **32** from Focus & Mood (in the screenshot the card above the categories is hidden under the header; ignore it).

### 1. Category forecast cards (y 208–390)

- `LazyRow`, full screen width (it breaks out of the column's 16 dp padding), content padding horizontal **16** (measured 12, probably a slightly scrolled row; use 16 like everything else), item spacing **24**. In the screenshot card 3 is cut off at the right edge: 12 + 144 + 24 + 144 + 24 + 54 visible = 402 ✓.
- **Card**: 144 × 182, `surface`, radius ≈12 (`shapes.medium`), no border, no shadow, padding 12 horizontal.
  - **Illustration** area 80 × 64 at (12, 22) inside the card, artwork centred in it (both icons' centres are at x ≈52, y ≈54 from the card's top-left). Artwork size: briefcase ≈57×47, heart ≈76×44, pills ≈52 high.
  - **Lock badge** (locked cards only): 34 dp circle `lockBadge`, top 16, right 12 (overlaps the illustration area's right side); `ic_lock` glyph 12×15 white, centred (use a 16 dp icon box).
  - **Title**: `sectionTitle` white, cap top ≈99 from the card top (text box top ≈95, i.e. ≈9 below the illustration area).
  - Gap ≈6.
  - **Preview**: `preview` white, max 2 lines, `TextOverflow.Ellipsis`, width 120 (144 − 2×12). Bottom padding ≈14 (≈17 to the descender).
- Gap to the next block: **32**.

### 2. Tip for the day banner (y 422–510)

- x 16–386 (370 × 88), radius ≈12, no border.
- Fill: horizontal linear gradient `tipGradientStart` (0 %–40 %) → `tipGradientEnd` (100 %). Vertical variation is under 2 %.
- Padding 12 vertical, 16 start.
- Text column (start-aligned):
  - "Tip for the day": `bodyRegular` white.
  - Gap ≈2.
  - "Let your adventures unfold naturally.": `sectionTitle` white, wraps to 2 lines (line pitch 26.6). In the screenshot line 1 ends at x 277. Give the text an end padding of ≈72 so the illustration never overlaps it; with Geist the wrap point may differ, which is fine.
- **Illustration** (shooting stars), ≈70 × 88, anchored top-right and bleeding to the card's top/right/bottom edges, clipped by the card shape (`Modifier.clip(shapes.medium)` on the card). Visible glyph extent is x 328–386, y 430–498.
- Gap to the next block: ≈**40** (≈32 + the list's leading).

### 3. Yes / No for today (y ≈550–690)

Two identical blocks stacked with gap ≈**30** (use 32). Each is a `Row` of two equal columns: padding 16, gap 8 (16 + 181 + 8 + 181 + 16 = 402 ✓; measured right column start 204 ≈ 205 ✓).

- **Left column**, vertically centred against the right column: row with gap 4:
  - Badge: 20 dp circle, gradient fill (Yes: `yesGradient*`, top-right → bottom-left; No: `noGradient*`, top-left → bottom-right), white glyph ≈10 dp (✓ stroke 2 or ×), centred.
  - Title: `sectionTitle` white. "Yes for today" / "No for today".
- **Right column**: 3 rows, line pitch 17.5 (`bodyRegular`, line height 18), no extra spacing:
  - Mark 14 dp box (✓ `ic-check-gradient` / × `ic-cross-gradient`), gap ≈3, then text `bodyRegular` white, 1 line. The longest item "Engage in physical activity" measures 159 dp in the screenshot and fits 181 − 17 = 164; use `maxLines = 2` just in case Geist is wider.
- Yes block y ≈550–605, No block y ≈634–689.
- Gap to the next block: ≈**36** (use 32 + list leading).

### 4. Tarot Insight card (y 725 → cut off by the nav at 792)

- x 16–386, `surface`, radius ≈12 at the top (bottom not visible).
- Radial glow: centre ≈(200, bottom of the visible part), colour `cardGlow`, radius ≈200, fading to `surface`. It could also be an image in the original (it's too blurred to tell).
- "Tarot Insight": `headline` white, padding start 12, text box top ≈24 (cap top at y 755).
- **Everything below the title is not visible** (open question 2). Don't invent content: render the title and the glow, card height ≈160 (Decision 2).

- Bottom padding of the whole content stays as in `astrology-home` (clearance for the existing nav bar).

## Texts

Keys go to the existing `values/strings_home.xml` (prefix `home_`); add these:

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

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Career briefcase | `assets/category-career-briefcase.svg` (traced), crop `category-career-briefcase.png` | outline, stroke ≈2.2, round joins; gradient in the SVG. Convert to `drawable/home_ic_category_career.xml` |
| Love heart with orbit | `assets/category-love-heart.svg` (traced), crop `category-love-heart.png` | heart outline + tilted ellipse orbit (≈−12°) + two 4-point sparkles (top-left, bottom-right). In the original, the orbit passes in front of the heart on the left and behind it on the right; the traced SVG draws it on top everywhere, which is acceptable. `home_ic_category_love.xml` |
| Health pills | crop `category-health-pills-partial.png` only (cut off) | two capsules crossing, outline, green gradient. Draw a vector in the same style: two rounded capsules ≈20×44 rotated ±45°, stroke 2.2, one with a divider line across its middle. `home_ic_category_health.xml`. Only three categories for now (see Decisions) |
| Lock | `assets/ic-lock.svg`, crop `lock-badge.png` | filled padlock 12×15 with keyhole. `home_ic_lock.xml` (or `ic_lock` via Theme if shared) |
| Tip shooting stars | `assets/tip-shooting-stars.svg` (approximation), crop `tip-shooting-stars.png` (**placeholder**, includes the banner background) | three 7-point yellow stars with offset grey copies, three magenta comet tails going up-right, three tiny stars. Prefer the SVG → `home_tip_stars.xml`; the PNG crop is for visual reference |
| ✓ list mark | `assets/ic-check-gradient.svg`, crop `yes-check.png` | `home_ic_check.xml` |
| × list mark | `assets/ic-cross-gradient.svg`, crop `no-cross.png` | `home_ic_cross.xml` |
| Yes / No badges | crops `yes-badge.png`, `no-badge.png` | draw in code: gradient circle + white ✓/× (reuse the check/cross paths in white) |
| Tarot glow | crop `tarot-card-top.png` | draw with `Brush.radialGradient` |
| Reference crop | `tip-banner-full.png` | reference only |

## States and behaviour

- **Tabs**: the existing DateTabs selection does not change the new blocks yet; the mock returns the same content for every tab.
- **Category row**: horizontal scroll, no snapping visible. Tapping a card: locked → paywall/premium, unlocked → category detail. Neither exists yet: hoisted no-op `onCategoryClick(id)`.
- **Locked state**: the lock badge is shown when `isLocked`; the preview text is still shown (truncated). Unlocked cards: same card without the badge.
- **Tip banner, Yes/No**: static, not clickable (assumed).
- **Tarot card**: hoisted no-op `onTarotClick()`.

## Data

Mock in `data/home/`: extend `HomeData` / `HomeRepository.homeData()` and `MockHomeRepository`:

- `categories: List<CategoryForecast>`: `id`, `title`, `preview`, `isLocked`, icon key. Mock: Career (locked), Love (locked), Health (locked, preview text unknown, use a plausible one, e.g. "The Aries moon encourages you to move more and rest well."). Put Career and Love previews as full plausible sentences; the UI truncates them to 2 lines.
- `tipOfTheDay: String`.
- `yesForToday: List<String>` (3), `noForToday: List<String>` (3).
- `tarot`: title only for now.
- User name is already in the home mock.

## New tokens needed

For a separate `theme` Issue. Names are suggestions.

| Kind | Name | Value |
|---|---|---|
| colour | `lockBadge` | `#323996` |
| colour | `tipGradientStart` / `tipGradientEnd` | `#051B6F` / `#36327A` |
| colour | `yesGradientStart` / `yesGradientEnd` | `#26A09B` / `#2478A4` |
| colour | `noGradientStart` / `noGradientEnd` | `#B24044` / `#6C235F` |
| colour | `cardGlow` | `#252B78` |
| type | `sectionTitle` | 18 Bold, line height 26 |
| type | `preview` | 16 Regular, line height 23 |
| type | `bodyRegular` | 14 Regular, line height 18 |
| type | `headline` | 22 Bold |
| shape | none | radius ≈12 = existing `shapes.medium` (measured 12–14) |

Palette differences from the existing theme, left as is: page background `#010827` vs `background` `#0D0F2B`, card `#141A52` vs `surface` `#1A1D42`.

## Decisions (orchestrator defaults, 2026-09-27)

The screenshot doesn't answer these; the defaults below let the screen be built now and can be changed later.

1. **Categories**: exactly three cards, Career, Love, Health, all locked.
2. **Tarot Insight**: render the card with its title and the glow, height ≈160, no other content. What's inside comes with a later screenshot.
3. **Category row padding**: 16.
4. **Tabs**: the new blocks don't depend on the selected tab.
5. **Unlocked card**: same card without the lock badge; taps on any card are a hoisted no-op.

## Open questions

1. Which categories follow Health, with which icons and texts?
2. What is inside the Tarot Insight card and what comes after it?
3. What does a tap on a locked card open (paywall)?

# compatibility — design spec

The **Compatibility** screen: the tab "Compatibility" of the bottom nav bar, today a `ComingSoonScreen`. An empty state that invites the user to check their love compatibility: a headline, the user's avatar next to an empty "Partner" slot with a "+" button. New screen package `ui/compatibility/`.

**Out of scope** (don't build): the add-partner flow, compatibility results, sign pickers, a bottom bar redesign. Visual only in this round: the Partner "+" does nothing (Issue #49).

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the **original app** (not our Figma), received 2026-09-27 (Issue #49). No status bar drawn (top ≈60 dp empty); the bottom 50 px is the system navigation bar.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp**. Screenshot measurements below are dp at that scale.
- **The screenshot sets content only.** Every size, colour, text style, radius and spacing below is the Figma/feed treatment (`docs/design/astrology-home/`, `docs/design/home-feed/`, `docs/design/psychics/`, `docs/design/readings/`, code in `ui/components/`, `ui/home/`). The original headline is ≈21 Bold across 330 dp; it is scaled down to our roles. Differences are listed in *Screenshot vs Figma*.
- Colours sampled with PIL (flat areas) only to identify roles.
- `assets/`: `comets.svg` (new vector, ship it) and `ref-*.png` reference crops of each block (reference only, don't ship them).

## Colours

All existing `AppColors` tokens. **No new colour tokens.**

| Token | Hex | Status | Used for |
|---|---|---|---|
| `background` | `#0D0F2B` | existing | page fill, hero gradient end, "+" glyph on the white button (original page `#050D2A` → `#081645`) |
| `heroGradientTop` | `#18134F` | existing | hero gradient start (via the home hero vector) |
| `moon` | `#353E7E` | existing | hero moon (via the home hero vector) |
| `cardGlow` | `#252B78` | existing | bottom radial glow, at **50 %** alpha (original glow ≈`#08184E` near the bottom bar) |
| `surface` | `#1A1D42` | existing | Partner circle fill (original `#172551`) |
| `onSurface` | `#FFFFFF` | existing | headline, "You"/"Partner" labels, Partner "+" button fill |
| `accentLavender` | `#A1A5DB` | existing | subtitle (original grey `≈#9A9FB5`) |
| `onSurfaceMuted` | `#797C9B` | existing | dashed Partner border (original `#838BA4`), thin "+" between the circles (original `#666C85`) |
| `accentPurple` | `#B388FF` | existing (hardcoded in the vector) | comet tails and halos, at 25 / 45 / 35 % alpha (original tail `≈#382F71` on the page) |

## Typography

Only existing `appTypography` styles. **No new type tokens.**

| Style | Size / weight | Where | Original (≈) |
|---|---|---|---|
| `cardTitle` | 16 SemiBold | top bar "Compatibility" (via `ScreenTopBar`), headline "Check your love compatibility" | 17 Bold / 21 Bold |
| `body` | 13 Medium | subtitle "Add your partner to see the result" | 16 Regular |
| `tab` | 14 Medium | labels "You", "Partner" | 17 Medium |

## Layout (top → bottom), width 402

Screen = `Box(fillMaxSize)`, layers bottom → top:

1. **Page fill** `background`.
2. **Bottom glow** (not scrolling, fills the screen): `Brush.radialGradient(cardGlow @ 50 % → transparent)`, centre at (0.55 × width, height − 84 − 40) (just above the bottom bar), radius 0.8 × width (≈320). Same technique as the Tarot Insight / challenge-card glow.
3. **Scrolling column** (`verticalScroll`, `fillMaxSize`), holding:
   - **Hero background**: the home `HeroBackground` unchanged (402 × 420 frame of `home_hero_background`, scaled with the width, drawn behind the status bar): gradient `heroGradientTop` → `background`, moon, gold sparkles. It ends in `background`, so it blends into the page fill. It scrolls with the content (as on home).
   - **Comets**: `drawable/compatibility_comets.xml` (from `assets/comets.svg`, not tinted), 160 × 80 dp, placed at (228, 188) in the same 402-wide hero frame and scaled with it (put it in the same `Box` as the hero). Comet 1 head ≈(236, 220), comet 2 head ≈(372, 260), tails pointing up-right, as in the original.
   - Content column over the hero (`statusBarsPadding()`):

### 1. Top bar
Shared `ScreenTopBar(title = "Compatibility")`, no leading icon: 56 high, padding 20, centred `cardTitle` `onSurface`. Scrolls with the content (as Psychics/Readings).

### 2. Centred body
A `Box(fillMaxWidth, heightIn(min = viewport height − status bar − 56 − bar clearance), contentAlignment = Center)`, so the block below sits in the vertical centre of the space between the top bar and the bottom bar and the page scrolls only when it doesn't fit (use `BoxWithConstraints` / the scroll viewport height). Bar clearance = the shared bar height: 84 (12 + 48 item + 24) or the bar's own constant, plus `WindowInsets.navigationBars` as home does.

Body column, centred horizontally, padding horizontal 24:
- **Headline** "Check your love compatibility": `cardTitle` `onSurface`, `TextAlign.Center`, up to 2 lines.
- Gap **8**. **Subtitle** "Add your partner to see the result": `body` `accentLavender`, `TextAlign.Center`, up to 2 lines.
- Gap **40**. **Pair row** (see 3).
- Height ≈ 20 + 8 + 16 + 40 + 170 = **254**.

Fit check on 402 × 874 (iPhone status bar 62, home indicator 34 → bar ≈ 94): 874 − 62 − 56 − 94 = 662 ≥ 254 ✓, no scroll. The block is centred like the original (original content centre ≈ y 450, midway between title and bar).

### 3. Pair row
`Row`, centred, `verticalAlignment = Top`; items: You column · gap 16 · "+" · gap 16 · Partner column.
Widths: 33 + 140 + 16 + 24 + 16 + 140 + 33 = **402** ✓ (centre-to-centre 196; original 202).

- **You column** (centred, gap 12):
  - **Avatar** 140 × 140: the home profile avatar at a larger size (shared `ProfileAvatar`, see Shared components): 4 dp transparent inset, then a circle filled `background` with `home_avatar_character` `ContentScale.Crop`, clipped to `CircleShape` (visible circle 132). Replaces the original's archer illustration.
  - **Label** "You": `tab` `onSurface`, 1 line.
- **"+"** between: `compatibility_ic_plus_thin` 24 × 24 tinted `onSurfaceMuted`, vertically centred on the circles (top offset (140 − 24) / 2 = 58). Decorative (no content description).
- **Partner column** (centred, gap 12):
  - **Slot** 140 × 140 box with the same 4 dp inset, so it matches the avatar's visible 132:
    - circle fill `surface`;
    - dashed border: 1.5 dp stroke `onSurfaceMuted`, round caps, dash 8 / gap 5 (≈32 dashes around, as in the original), drawn with `drawCircle(style = Stroke(1.5.dp, cap = Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp, 5.dp))))` inset by half the stroke;
    - centred **add button**: 24 circle `onSurface` fill, `home_ic_plus` 16 tinted `background`. Content description "Add partner". Not clickable this round (see Behaviour).
  - **Label** "Partner": `tab` `onSurface`, 1 line.
- Row height 140 + 12 + 18 = 170.

### 4. Bottom nav bar
Not part of this screen: `ui/navigation/AppShell` draws the shared `ui/components/AppBottomBar` over every tab, here with `AppTab.Compatibility` selected. **Not re-specified.** `AppShell` renders `CompatibilityScreen()` instead of `ComingSoonScreen` for this tab.

## Texts

New `values/strings_compatibility.xml`, prefix `compatibility_`:

| Key | Text |
|---|---|
| `compatibility_title` | Compatibility |
| `compatibility_headline` | Check your love compatibility |
| `compatibility_subtitle` | Add your partner to see the result |
| `compatibility_you` | You |
| `compatibility_partner` | Partner |
| `compatibility_add_partner` | Add partner *(content description of the "+" button)* |
| `compatibility_your_avatar` | Your avatar *(content description of the avatar)* |

All verbatim from the screenshot except the two content descriptions.

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Avatar | existing `home_avatar_character.jpg` | the home profile avatar (Issue); replaces the original's archer illustration. Reference `assets/ref-pair-row.png` |
| Hero (gradient, moon, sparkles) | existing `home_hero_background.xml` | via the shared `HeroBackground` |
| Comets | **new** `compatibility_comets.xml` from `assets/comets.svg` | 160 × 80 viewport; tails = tapered triangles `#B388FF` at 25 % + 45 % alpha, head = halo circle `#B388FF` 35 % + 4-point white sparkle (r 3 / 2.5). Plain paths with `fillAlpha`, no gradients (`docs/COORDINATION.md`); colours baked in, **not tinted**. Reference `assets/ref-comets.png` |
| Thin "+" between the circles | **new** `compatibility_ic_plus_thin.xml` | 24 viewport, `M4,12H20 M12,4V20`, stroke 1.5, round caps, single colour (tinted `onSurfaceMuted`) |
| Partner "+" glyph | existing `home_ic_plus` | 16 dp, tinted `background` on the white 24 circle |
| Reference crops | `assets/ref-*.png` | top bar, comets, headline, pair row, partner slot — reference only |

## States and behaviour

- **Visual only** (Issue): the Partner "+" and slot do nothing; no add-partner flow. Don't add `clickable` or no-op callbacks (YAGNI, as Readings); they come with the add-partner Issue. The button is drawn, with its content description, but not interactive.
- **Layout**: the body is vertically centred between the top bar and the bottom bar; fits 402 × 874 without scrolling; on shorter screens the whole page (hero, top bar, body) scrolls.
- **Background**: hero + comets scroll with the content; the bottom glow and page fill stay fixed.
- **Bottom bar**: Compatibility tab selected (shell).
- Implied but not shown, not built: a partner-added state (partner avatar in the slot, a "Check" button), sign pickers, results.

## Data

**No mock data this round.** Every string is a resource and the avatar is the same static drawable home uses (home doesn't take it from the mock either). The screen is a stateless composable with no parameters besides `modifier`. A `CompatibilityRepository` and UI state (partner, result) come with the add-partner Issue, not now (YAGNI).

## Screenshot vs Figma (for information)

| Element | Original | Here |
|---|---|---|
| Page title | ≈17 Bold | `ScreenTopBar`, `cardTitle` 16 SemiBold |
| Background | flat navy gradient `#050D2A` → `#081645`, glow near the bar | home hero (gradient, moon, sparkles) + `background` + `cardGlow` @ 50 % radial glow |
| Comets | 2 purple-tailed comets with white heads | same, as a plain-path vector in `accentPurple` |
| Headline | ≈21 Bold, 330 dp wide, 1 line | `cardTitle` 16 SemiBold, ≈255 dp, 1 line |
| Subtitle | ≈16 Regular grey | `body` 13 Medium `accentLavender` |
| Headline → subtitle → circles | ≈12 / ≈52 | 8 / 40 |
| You | archer illustration, ≈132 circle | home avatar, 140 (visible 132) |
| Partner | ≈135 circle `#172551`, dashed `#838BA4` ≈1.5, white "+" button ≈23 | 132 visible, `surface`, dashed `onSurfaceMuted` 1.5, button 24 |
| Middle "+" | ≈18, thin grey | 24 box, 16 glyph, stroke 1.5, `onSurfaceMuted` |
| Labels | ≈17 Medium, gap ≈16 | `tab` 14 Medium, gap 12 |

## Shared components

Reuse:

| Where | Component | Use |
|---|---|---|
| `ui/components/ScreenTopBar.kt` | `ScreenTopBar(title)` | top bar, no leading icon |
| `ui/components/AppBottomBar` | bottom bar (shell) | Compatibility selected, not respecified |
| `ui/navigation/AppShell.kt` | tab switch | `AppTab.Compatibility -> CompatibilityScreen()` (drop `ComingSoonScreen` for it; `ComingSoonScreen` stays for Chatroom) |
| drawables `home_hero_background`, `home_avatar_character`, `home_ic_plus` | hero, avatar, plus glyph | now used by two screens; reuse under their current names (renaming `home_*` is a Theme task) |

Move to `ui/components/` (in the compatibility screen Issue; home must look identical afterwards), don't copy:

| Now in | Component | Suggested shared name |
|---|---|---|
| `ui/home/HomeScreen.kt` (private `HeroBackground` + `HERO_*` constants) | 402 × 420 hero art scaled with the width | `HeroBackground(modifier)` in `ui/components/HeroBackground.kt`; expose the frame scale (or a `content` slot in hero-frame coordinates) so Compatibility can place its comets in the same frame |
| `ui/home/ProfileHeader.kt` (the avatar `Image`) | circular avatar: 4 inset, `background` fill, crop, `CircleShape` | `ProfileAvatar(size: Dp, contentDescription: String?, modifier)`; home 100, Compatibility 140 |

The radial `cardGlow` glow is now drawn by Tarot Insight, the challenge card and this screen; a shared `Modifier.glow(...)` is optional, only if it removes real duplication.

## New tokens needed

None. The glow uses `cardGlow` at a 50 % alpha literal (like the `primary` @ 20 % chip fill); the comet colours are baked into the vector (as the home tip stars).

## Decisions (defaults, may be changed by the orchestrator)

1. **Headline `cardTitle` 16 SemiBold** (Issue: the original is too big). `name` 24 would not fit on one line (≈380 dp); no new 18–20 headline token.
2. **Subtitle `body` 13 Medium `accentLavender`**, the feed's muted secondary text; labels "You"/"Partner" `tab` 14 Medium white.
3. **Background** = page `background` + the **home hero** at the top (gradient, moon, sparkles; Issue) + a **`cardGlow` @ 50 % radial glow** near the bottom bar + a **new comets vector** at the original's position. The original's flat navy gradient is not reproduced; the hero gradient and glow are its Figma equivalents.
4. **Circles 140** (Issue ≈140), both with a 4 dp inset so the avatar and the Partner slot have the same visible 132 circle; gap 16 around a 24 dp thin "+".
5. **Avatar**: the home profile avatar (`avatar-character`), no ring or border (as home).
6. **Partner slot**: `surface` fill, dashed `onSurfaceMuted` 1.5 border (dash 8 / gap 5), white 24 button with the `home_ic_plus` glyph in `background`.
7. **Visual only**: Partner "+" not clickable, no callbacks; no mock data or repository this round.
8. **Vertical centring** between top bar and bottom bar; fits 402 × 874 without scrolling; on short screens the page scrolls (top bar and hero scroll with it, as Psychics/Readings).
9. **Comets not tinted**, colours baked into the vector (plain paths with alpha; no gradients per COORDINATION).

## Open questions

1. Should the headline be stronger than the page title (a new 20 SemiBold `headline` style)? Default: `cardTitle` 16, same as the title.
2. Should the Partner slot become a button that opens the add-partner flow (and should the whole circle be the target, not just the "+")? Default: not clickable this round.
3. Is the home hero (with the moon) wanted on this tab, or only the gradient without the moon? Default: the full hero, per the Issue.

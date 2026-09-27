# astrology-home — design spec

Source: Figma `Power-Place`, frame `astrology-home` (node `2576:44`),
https://www.figma.com/design/ehr6aIVaitNHH1KafRlVQW/Power-Place?node-id=2576-44

**Do not call the Figma MCP for this screen.** The account is on the Starter plan (20 MCP calls per month for everything). All the data is in this folder:

- `screenshot.png` is the visual target (367×1024 render of a 402×~1120 pt frame).
- `assets/` holds the original assets exported from Figma. SVGs are 24×24-ish line icons (stroke 1.5–2, `stroke-linecap=round`) plus the hero background. Convert them to Compose vector drawables (`composeResources/drawable/*.xml`), keeping the viewport and colours. Avatars are JPEG.
- This file lists every token and measurement from `get_design_context`. Units are dp/sp (1 Figma px = 1 dp).

The file has no Figma variables, so the tokens below are raw values extracted from the frame.

## Colours

| Token (suggested name) | Value | Used for |
|---|---|---|
| `background` | `#0D0F2B` | screen background, input field fill, avatar inner |
| `backgroundDeep` | `#0A0B21` | bottom nav bar |
| `surface` / card | `#1A1D42` | cards |
| `outline` | `#262954` | card border 1dp, nav bar border, input border, ring track |
| `heroGradientTop` | `#18134F` | hero gradient start (→ `background`) |
| `moon` | `#353E7E` | moon circle in hero |
| `primary` (blue) | `#4D7CFF` | active nav item, tab indicator, add-story ring, Asc symbol, Career ring |
| `accentOrange` | `#FF6B4A` | badges (FREE, 3), avatar thumb border, Love ring |
| `accentPink` | `#FF52A3` | moon symbol ☽, chat icon (10% alpha container) |
| `accentTeal` | `#26D0CE` | Health ring |
| `accentLavender` | `#A1A5DB` | secondary labels (zodiac names, ring labels), Family ring |
| `accentPurple` | `#B388FF` | highlighted text "a free personal reading" |
| `accentGold` | `#FFB84D` | sun symbol ⊙ |
| `onSurface` | `#FFFFFF` | primary text |
| `onSurfaceMuted` | `#797C9B` | inactive tabs/nav labels, placeholder, inactive icons |
| `textSoft` | `#EFEAEA` | "You have" text in cards |
| glass fill | `#FFFFFF` @ 7% | Birth Chart pill, Ask button |
| glass border | `#FFFFFF` @ 10% | same, 1dp |

Card shadow: `0 4 2 rgba(0,0,0,0.25)` (drop shadow).

## Typography

Font family: **Geist** (SIL OFL). Get the TTFs from the npm package `geist` (`registry.npmjs.org` is reachable from cloud sessions) and add Regular/Medium/SemiBold/Bold to `composeResources/font/`. Line height is `normal` everywhere.

| Style | Size / weight | Where |
|---|---|---|
| name | 24 SemiBold | "Andrew" |
| cardTitle | 16 SemiBold | "Focus & Mood" |
| cardLead | 16 Medium / Bold | "You have" (Medium, `#EFEAEA`) + "a free personal reading" (Bold, `#B388FF`) |
| tab | 14 Medium | date tabs |
| button | 14 SemiBold | "Ask" |
| pill | 13 SemiBold | "Birth Chart" |
| body | 13 Medium | zodiac names |
| input | 13 Regular | placeholder, single line with ellipsis |
| ringValue | 12 SemiBold | "50%" |
| caption | 11 Medium | ring labels, nav labels (active = SemiBold) |
| badge | 8 Bold UPPERCASE / 9 Bold | "FREE", "3" |

## Layout (top → bottom), frame width 402

1. **Hero background**: absolute, 402×420 at top. Linear gradient `#18134F` → `#0D0F2B` (diagonal, top-left to bottom-right). A moon made of two circles r=36 (`#353E7E` at (334,116), `#18134F` scrim offset (-14,-6)) and a few small gold sparkles. Use `assets/hero-background.svg` as a vector (its viewBox is 550×480 with a 70px left offset; the Figma inset is `0 -19.4% -14.29% -17.41%`).
2. **Status bar** (44 high): system status bar. Do **not** draw it. Draw behind it (edge-to-edge) and pad content with `WindowInsets.statusBars`.
3. **Top bar**: height 56, horizontal padding 20, space-between.
   - Left, gap 12: *add-story*, a 36 circle with a 1.5 border in `#4D7CFF` and the `plus` icon 16 in the centre. Then *avatar-thumb*, a 36 circle with a 2 border in `#FF6B4A`, holding `avatar-thumb.jpg` (crop).
   - Right: `settings` icon 22, white.
4. **Profile header**: column, centred, gap 16, padding top 12 / bottom 24 / horizontal 24.
   - Avatar: 100 circle, 4 padding ring (transparent), inner circle `#0D0F2B` holding `avatar-character.jpg` (crop).
   - Name "Andrew": 24 SemiBold, white.
   - Zodiac row, gap 12. Each item has gap 4: symbol + name (13 Medium `#A1A5DB`):
     `⊙` `#FFB84D` Sagittarius · `☽` `#FF52A3` Scorpio · `Aˢᶜ` (11 Bold `#4D7CFF`) Pisces.
   - *Birth Chart* pill: glass fill + border, radius 20, padding 16×8, gap 8, content `🔮` (14) and "Birth Chart" 13 SemiBold white. Backdrop blur 5 is optional.
5. **Scrollable content**: column, horizontal padding 16, gap 20, bottom padding 100 (clearance for the nav bar).
   - **Reading card ×2**: `#1A1D42` fill, 1 border `#262954`, radius 20, padding 16, gap 16, shadow.
     - Lead text (annotated string): "You have " + highlighted text. Card 1: "a free personal reading". Card 2: "a paid personal reading".
     - Input row, gap 12: chat-icon container 42×42, radius 12, fill `#FF52A3` @10%, holding `message-circle-pink` 20. Then the mock input: flex, height 42, radius 21, fill `#0D0F2B`, 1 border `#262954`, horizontal padding 16, placeholder "Will my ex and I get back together?" 13 Regular `#797C9B`, 1 line with ellipsis.
     - Ask button: full width, height 40, radius 20, glass fill + border, "Ask" 14 SemiBold white.
   - **Date tabs**: row, space-between, each item width 80: Yesterday / **Today** (selected) / Tomorrow / Week. 14 Medium. Selected is white and has an indicator below it (gap 8, 28×2, radius 1, `#4D7CFF`). The others are `#797C9B`. Selection is stateful (hoisted).
   - **Focus & Mood card**: same card style but padding 20×16 and gap 20.
     - Header row, space-between: "Focus & Mood" 16 SemiBold white + `info` icon 16.
     - Rings row, gap 12, 4 equal-weight columns, each a column with gap 8, centred:
       ring 52×52 (track ring `#262954`, progress arc from 12 o'clock clockwise, stroke ≈ 5, round caps). The value sits in the centre, 12 SemiBold white. The label is below, 11 Medium `#A1A5DB`.
       Career 50% `#4D7CFF` · Love 70% `#FF6B4A` · Health 65% `#26D0CE` · Family 60% `#A1A5DB`.
       Draw the rings with `Canvas`/`drawArc`, not the exported SVGs: the SVG arcs are pre-cut for the mock values. The SVGs remain in `assets/` for reference.
6. **Bottom nav bar**: pinned to bottom, fill `#0A0B21`, top border 1 `#262954`, height 84 (padding top 12 / bottom 24, so use the navigation-bar inset instead of the fixed 24 on devices). 5 items, each 80 wide, a column with gap 4: icon 20 + label 11.
   - Today `star`: active, `#4D7CFF`, label SemiBold.
   - Psychics `user`: badge "FREE", 8 Bold uppercase white on `#FF6B4A`, radius 6, padding 4×2, offset (+14, −6) from the icon.
   - Compatibility `heart`.
   - Chatroom `message-circle`: badge "3", 14×14 circle `#FF6B4A`, 9 Bold white, offset (+14, −6).
   - Readings `book`.
   - Inactive items use `#797C9B` for both icon and label.

## Data (mock)

User name, zodiac sun/moon/asc, reading cards (free/paid, placeholder question), mood scores (4 × label/percent/colour), nav badges. Put them in `data/home/` behind a small interface (see CLAUDE.md).

## Interactions

Nothing navigates yet. The tab selection changes state. The nav bar has a selected state. The buttons (Ask, Birth Chart, settings, add-story, nav items) are clickable no-ops with callbacks hoisted to the screen.

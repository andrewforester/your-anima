# paywall — design spec

The **Paywall** ("Anima Premium"): a full-screen overlay opened over the tab shell (bottom bar hidden) when the user taps a locked element. A feature pager at the top, three plan cards, the auto-renew legal text, a "Subscribe" button and legal links. New screen package `ui/paywall/`, data in `data/paywall/`.

**Out of scope** (don't build): the original brand "Nebula Premium" (use **"Anima Premium"**), the company legal address at the bottom ("Spiritual Nebula Limited, …"), real purchase / restore / links (visual only, no-ops), the status bar (Issue #58).

## Source

- `screenshot.png`: 864×1920 px, Android screenshot of the **original app** (not our Figma), received 2026-09-27 (Issue #58). No status bar drawn (top ≈60 dp empty); the bottom ≈50 px is the system navigation bar area.
- Scale: 864 px ÷ 402 dp = **2.149 px/dp**. Screenshot measurements below are dp at that scale.
- **The screenshot sets content only.** Every size, colour, text style, radius and spacing below is the Figma/feed treatment (`docs/design/astrology-home/`, `docs/design/home-feed/`, the Psychics/Readings/Chatroom/Compatibility packages, code in `ui/components/`). The original's magenta glow, serif titles, cream button and green savings badges are not reproduced. Differences are listed in *Screenshot vs Figma*.
- Colours sampled with PIL (flat areas) only to identify roles: page `#151821`, glow core `#7E0A43`, card `#212535`, selected card `#2D4386` → `#242C4A` with a `#6378FD` border, "HOT DEAL" tab `#A2B9FF`, savings badge `#54D993`, button `#F3F3E5`.
- `assets/`: `ic-close.svg` (new vector, ship it as `drawable/paywall_ic_close.xml`) and `ref-*.png` reference crops of each block (reference only, don't ship them).

## Colours

All existing `AppColors` tokens. **No new colour tokens.**

| Token | Hex | Status | Used for |
|---|---|---|---|
| `background` | `#0D0F2B` | existing | page fill (original `#151821`) |
| `accentPurple` | `#B388FF` | existing | glow core, at **30 %** alpha (replaces the original's magenta `#7E0A43`); slide 3 icon |
| `cardGlow` | `#252B78` | existing | glow middle ring, at **70 %** alpha (the Figma glow colour of Tarot / challenge card / Compatibility) |
| `surface` | `#1A1D42` | existing | plan card fill (via `appCard`) (original `#212535`) |
| `outline` | `#262954` | existing | unselected plan card border 1 (via `appCard`) |
| `primary` | `#4D7CFF` | existing | selected plan border 2 and gradient tint (35 % → 10 %), "Restore" and legal links text, Subscribe button fill (original border `#6378FD`, button cream `#F3F3E5`) |
| `accentOrange` | `#FF6B4A` | existing | "HOT DEAL" tab fill and "Save N%" chip fill: the nav-badge colour ("FREE"), our badge role (original tab `#A2B9FF`, chips green `#54D993`); slide 2 icon |
| `onSurface` | `#FFFFFF` | existing | top bar title, close icon, slide title, plan title + price, badge/chip labels, "Cancel anytime…", button label, selected page dot |
| `accentLavender` | `#A1A5DB` | existing | slide subtitle, emphasised legal sentence |
| `onSurfaceMuted` | `#797C9B` | existing | legal text (rest), link separators "·", unselected page dots |
| `accentGold` / `accentPink` | `#FFB84D` / `#FF52A3` | existing | slide 1 / slide 4 icons |

## Typography

Only existing `appTypography` styles. **No new type tokens.**

| Style | Size / weight | Where | Original (≈) |
|---|---|---|---|
| `cardTitle` | 16 SemiBold | top bar "Anima Premium" (via `ScreenTopBar`), plan titles | 19 Bold / 17 serif |
| `name` | 24 SemiBold | slide title "Compatibility readings" | 26 serif |
| `body` | 13 Medium | slide subtitle; "Cancel anytime. Recurring billing." | 17 Regular / 15 Medium |
| `button` | 14 SemiBold | plan price; "Subscribe" label | 13 Bold / 17 Medium |
| `pill` | 13 SemiBold | "Restore" (the "View All" link role) | 17 Medium |
| `caption` | 11 Medium | auto-renew legal text, legal links row, "HOT DEAL" tab, "Save N%" chips (via `TagChip`) | 15 / 15 / 12 / 15 |

Text widths checked with Geist at these sizes: slide title 259 (fits 370), "1 349,99 UAH" `button` 89 (fits the 94 card interior), "Every 3 months" `cardTitle` 118 (wraps to 2 lines, see plan cards), links row 272 (fits 370).

## Layout (top → bottom), width 402

Screen = `Box(fillMaxSize)` drawn **over** the shell (tabs and bottom bar hidden behind it), layers bottom → top:

1. **Page fill** `background`, `fillMaxSize`.
2. **Scrolling column** (`verticalScroll`, `fillMaxSize`, `testTag(SCREEN)`); the glow is drawn behind its top (it scrolls with the content):
   - **Glow** (`drawBehind` on the top part, or a `Box` behind the top bar + pager): `Brush.radialGradient(0f to accentPurple @ 30 %, 0.45f to cardGlow @ 70 %, 1f to Transparent)`, centre (0.5 × width, status bar + **170**), radius **260** (≈0.65 × width; the original's glow core is at ≈163 dp and fades out by ≈370 dp). Draw behind the status bar (edge-to-edge).
   - Content column, `statusBarsPadding()`, then `navigationBarsPadding()` at the bottom:

### 1. Top bar
Shared `ScreenTopBar(title = "Anima Premium")` with a **new `trailing` slot** (see Shared components): 56 high, padding 20, centred `cardTitle` `onSurface`.
- **Leading**: close button, `paywall_ic_close` 22 × 22 tinted `onSurface` (the `ScreenTopBarIconSize` slot), clickable with a 44 touch target (`minimumInteractiveComponentSize()` or a 44 box centred on the icon), content description "Close". Calls `onClose`.
- **Trailing**: "Restore" text button, `pill` `primary` (the "View All" link treatment), 1 line, aligned `CenterEnd`, touch target ≥ 44 high, `role = Role.Button`. Calls `onRestore` (no-op).
- Title stays centred: text 120 wide in the middle of 362; "Restore" is 49 wide at the right edge → no overlap.

### 2. Feature pager
`HorizontalPager(pageCount = 4)` (foundation), full width 402, each page a centred column, padding horizontal 24 (inner 354):
- Top gap **24**.
- **Feature icon**: `TintedIconBox(size = 96, iconSize = 48, shape = CircleShape, tint = feature tint)` (10 % tint fill circle + tinted icon), centred. Decorative (`contentDescription = null`). Sits in the glow core (original: empty glow area ≈ 60–230 dp).
- Gap **24**. **Title**: `name` 24 SemiBold `onSurface`, `TextAlign.Center`, 1 line (max 2).
- Gap **8**. **Subtitle**: `body` 13 Medium `accentLavender`, `TextAlign.Center`, `minLines = 2`, `maxLines = 2`, ellipsis (keeps every page the same height).
- Page height ≈ 24 + 96 + 24 + 30 + 8 + 34 = **216**.

Then, below the pager:
- Gap **16**. **Page dots** (`PAGE_INDICATOR`): `Row`, centred, gap **8**, 4 dots of **6 × 6** `CircleShape`; current page `onSurface`, others `onSurfaceMuted`. Width 4 × 6 + 3 × 8 = 48. Follows `pagerState.currentPage`; not clickable. Content description on the row: "Page 2 of 4" (`paywall_cd_page`).

### 3. Plan cards
Gap **32** after the dots. `Row(padding horizontal 16, gap 8, verticalAlignment = Bottom)`, three cards `weight(1f)`:
16 + 118 + 8 + 118 + 8 + 118 + 16 = **402** ✓ (original 113 wide, gap 16).
The row is **20 taller** than a card (the HOT DEAL tab above the first card): row height 20 + 132 = **152**.

**Card** (`PlanCard`, one composable, all three the same size): width 118 (weight), height **132** (≈; original 154), radius **20** (`shapes.large`), `selectable(selected, role = Role.RadioButton, onClick = onPlanSelect(id))`, clipped to the shape. Column, centred horizontally, padding horizontal **12** / vertical **16** (inner 94 wide):
- **Title** "Every 3 months": `cardTitle` 16 SemiBold `onSurface`, `TextAlign.Center`, `minLines = 2`, `maxLines = 2` (so titles and prices line up in all cards: "Every 3 / months", "Every / month", "Weekly" + empty line).
- Gap **4**. **Price** "1 349,99 UAH": `button` 14 SemiBold `onSurface`, 1 line, `softWrap = false` (89 wide, fits 94).
- `Spacer(weight(1f))`.
- **Savings chip** (only when the plan has one): shared `TagChip(label = "Save 68%", fill = accentOrange)` (20 high, radius 10, `caption` white), centred. No chip → nothing (space stays empty; card height is fixed).

States:
- **Unselected**: `appCard(PaddingValues(...))`: `surface` fill, 1 `outline` border, radius 20, drop shadow.
- **Selected**: same shape and shadow, fill `surface` + `Brush.verticalGradient(primary @ 35 % → primary @ 10 %)` over it, border **2** `primary` (original: blue gradient card with a `#6378FD` border). Content colours unchanged.
- Default selection: **Every 3 months** (`defaultPlanId` from the repository).

**HOT DEAL tab** (only on plans with `badge = HotDeal`, the Weekly card): a `Box` **behind** the card, same width as the card, height 20 + 20 = 40, top-aligned 20 above the card top, shape `RoundedCornerShape(topStart = 12, topEnd = 12)` (bottom square, hidden under the card), fill `accentOrange`. Label "HOT DEAL" `caption` `onSurface`, centred in the visible top 20. The card is drawn over its lower 20, so the card's rounded top corners show the orange behind them (as in the original, where the tab joins the card). Build it as a `Box { Tab(); Card(Modifier.padding(top = 20.dp)) }` for every card (the tab only drawn when present) so all three cards keep the same top.

### 4. Auto-renew legal text
Gap **20**. Padding horizontal **16** (width 370). `caption` 11 Medium, `TextAlign.Center`, **two colours** (see Decisions): the charge sentence in `accentLavender`, the rest `onSurfaceMuted`. Built with `buildAnnotatedString` from three string resources joined by a space:
`paywall_legal_intro` + " " + `paywall_legal_charge` (formatted with the selected plan's `price` and `period`, `accentLavender`) + " " + `paywall_legal_outro`.
≈ 5 lines at 370 wide (≈ **75** high). Updates when the selected plan changes. `testTag(LEGAL)`.

### 5. Subscribe button
Gap **20**. Padding horizontal 16. Shared `PrimaryButton(label = "Subscribe", onClick = { onSubscribe(selectedPlanId) })`, `fillMaxWidth()` (370), **height 48** with the fully rounded shape (see Shared components: `PrimaryButton` gets a `height` parameter), fill `primary`, label `onSurface` centred, no icon. `testTag(SUBSCRIBE)`. No-op.

### 6. "Cancel anytime. Recurring billing."
Gap **12**. `body` 13 Medium `onSurface`, centred, 1 line.

### 7. Links row
Gap **4**. `Row`, centred, padding horizontal 16: "Terms of Service" · "Privacy Policy" · "Subscription Terms". Links `caption` 11 Medium `primary`; separators " · " `caption` `onSurfaceMuted` with horizontal padding 4. Each link: `clickable(role = Role.Button)`, vertical padding **12** (touch height ≈ 38), calls `onTermsClick` / `onPrivacyClick` / `onSubscriptionTermsClick` (no-ops). Row width ≈ 272 (fits 370; if it doesn't fit on a narrow screen, wrap with `FlowRow`, centred).

### 8. Bottom
Bottom padding **16** + `navigationBarsPadding()`. No bottom bar, no company address.

**Height check** on 402 × 874 (status bar 62, home indicator 34 → 778 available): 56 + 216 + 16 + 6 + 32 + 152 + 20 + 75 + 20 + 48 + 12 + 16 + 4 + 38 + 16 = **727** ≤ 778 ✓, no scroll. Shorter screens scroll the whole page (top bar included).

## Texts

New `values/strings_paywall.xml`, prefix `paywall_`:

| Key | Text |
|---|---|
| `paywall_title` | Anima Premium |
| `paywall_restore` | Restore |
| `paywall_cd_close` | Close *(content description of the X)* |
| `paywall_cd_page` | Page %1$d of %2$d *(content description of the dots)* |
| `paywall_feature_horoscope_title` | Daily horoscope *(invented)* |
| `paywall_feature_horoscope_subtitle` | Personal forecasts for love, career and health, every day *(invented)* |
| `paywall_feature_compatibility_title` | Compatibility readings |
| `paywall_feature_compatibility_subtitle` | Accurate insights and practices to support your love journey |
| `paywall_feature_tarot_title` | Tarot readings *(invented)* |
| `paywall_feature_tarot_subtitle` | Daily draws and full spreads to guide your decisions *(invented)* |
| `paywall_feature_psychics_title` | Chat with psychics *(invented)* |
| `paywall_feature_psychics_subtitle` | Get answers from trusted advisors whenever you need them *(invented)* |
| `paywall_hot_deal` | HOT DEAL |
| `paywall_save` | Save %1$d%% *(→ "Save 23%", "Save 68%"; the original has a space before %, dropped)* |
| `paywall_legal_intro` | By tapping “Subscribe”, you agree to enroll in an automatically renewing subscription. |
| `paywall_legal_charge` | Starting today, you’ll be automatically charged %1$s every %2$s until you cancel. |
| `paywall_legal_outro` | To avoid future charges, you must cancel before your next subscription renewal date. Cancel by visiting your account settings at Manage Subscription. |
| `paywall_subscribe` | Subscribe |
| `paywall_cancel_anytime` | Cancel anytime. Recurring billing. |
| `paywall_terms` | Terms of Service |
| `paywall_privacy` | Privacy Policy |
| `paywall_subscription_terms` | Subscription Terms |

Verbatim from the screenshot except the brand, the invented slides, the content descriptions and the template placeholders. Curly quotes/apostrophes as in the original (escape `’` is not needed in Compose resources; `%%` is). Plan titles, prices and periods come from the repository (see Data), not from strings.

## Icons and images

| Element | Source | Notes |
|---|---|---|
| Close X | **new** `paywall_ic_close.xml` from `assets/ic-close.svg` | 24 viewport, `M6,6L18,18 M18,6L6,18`, stroke 1.75, round caps, single colour (tinted `onSurface`), drawn at 22. `home_ic_cross` (14 viewport, stroke 2) is too heavy at 22 |
| Slide 1 "Daily horoscope" | existing `home_ic_sun` | tint `accentGold` |
| Slide 2 "Compatibility readings" | existing `ic_heart` | tint `accentOrange` (the Love category tint) |
| Slide 3 "Tarot readings" | existing `home_ic_moon` | tint `accentPurple` |
| Slide 4 "Chat with psychics" | existing `ic_message_circle` | tint `accentPink` (the reading-card chat icon tint) |
| Glow | code (`Brush.radialGradient`) | no image |
| Reference crops | `assets/ref-*.png` | top bar, slide, plans, legal + button, footer — reference only |

All four slide icons are single-colour vectors, tintable with `Icon(tint = …)`.

## States and behaviour

- **Opening** (orchestrator decision): state-based overlay in `ui/navigation/AppShell.kt`, no navigation library. `AppShell` holds `var showPaywall by rememberSaveable { mutableStateOf(false) }`; when `true` it draws `PaywallScreen(onClose = { showPaywall = false })` over everything **instead of** the bottom bar (the tab content may stay composed underneath, so its scroll position survives). No enter/exit animation (see Decisions).
- **Which elements open it**: today only the home category cards with `isLocked = true` (`ui/home/CategoryCard.kt`, the lock badge; all three mock cards are locked). The whole card is the tap target. Wiring: the stateful `HomeScreen(...)` gets `onLockedClick: () -> Unit = {}` and maps `onCategoryClick(id)` → `onLockedClick()` when that card is locked (unlocked cards keep the current no-op). `AppShell` passes `onLockedClick = { showPaywall = true }`. No other screen has locked items.
- **Close**: X → `onClose`. System back (Android): if the Compose Multiplatform `BackHandler` is available in the current dependencies, `enabled = showPaywall` closes the overlay; if it needs a new dependency, skip it (build files are the infra zone) and note it in the Issue.
- **Pager**: swipeable, 4 pages, starts on page 0 (always, regardless of the card tapped), **no auto-advance**, no looping. Dots follow the current page.
- **Plans**: exactly one selected (radio semantics, `selectableGroup()` on the row); tap selects; default "Every 3 months". The legal text follows the selected plan's price and period. Selection is hoisted UI state (survives recomposition, `rememberSaveable` of the plan id in the stateful wrapper).
- **No-ops** (hoisted callbacks): Restore, Subscribe (receives the selected plan id), the three links.
- Implied but not built: purchase flow, loading/success states, restore result, opening the paywall on a specific slide, real links.

## Data

New `data/paywall/`, behind an interface (a store/billing backend replaces the mock later):

```kotlin
/** Premium feature shown on a pager slide; the UI maps it to its title, subtitle, icon and tint. */
enum class PremiumFeature { Horoscope, Compatibility, Tarot, Psychics }

enum class PlanBadge { HotDeal }

@Immutable
data class SubscriptionPlan(
    val id: String,              // "weekly", "monthly", "quarterly"
    val title: String,           // "Weekly" — store product name, not a string resource
    val price: String,           // "354,99 UAH" — opaque, no currency formatting in the app
    val period: String,          // "week" — fills %2$s in paywall_legal_charge
    val savingsPercent: Int?,    // 23 → "Save 23%"; null → no chip
    val badge: PlanBadge?,       // HotDeal → orange tab above the card
)

@Immutable
data class PaywallData(
    val features: List<PremiumFeature>,
    val plans: List<SubscriptionPlan>,
    val defaultPlanId: String,
)

interface PaywallRepository { fun paywallData(): PaywallData }
```

`MockPaywallRepository`:

| id | title | price | period | savingsPercent | badge |
|---|---|---|---|---|---|
| `weekly` | Weekly | 354,99 UAH | week | — | HotDeal |
| `monthly` | Every month | 1 099,99 UAH | month | 23 | — |
| `quarterly` | Every 3 months | 1 349,99 UAH | 3 months | 68 | — |

`features = [Horoscope, Compatibility, Tarot, Psychics]`, `defaultPlanId = "quarterly"`. Prices keep the screenshot's non-breaking thousands space as a plain space.

UI: `PaywallUiState(features, plans, selectedPlanId)` (+ `selectedPlan` getter); stateful `PaywallScreen(onClose, modifier, repository = MockPaywallRepository)` holds the selection; stateless `PaywallScreen(state, onPlanSelect, onClose, onRestore, onSubscribe, onTermsClick, onPrivacyClick, onSubscriptionTermsClick, modifier)`. The pager state stays inside the stateless screen (`rememberPagerState`), it is pure UI.

## Test tags

`PaywallScreenTags` in `ui/paywall/`:

| Constant / function | Value | On |
|---|---|---|
| `SCREEN` | `paywall_screen` | root scroll column |
| `CLOSE` | `paywall_close` | close button |
| `RESTORE` | `paywall_restore` | Restore |
| `PAGER` | `paywall_pager` | `HorizontalPager` |
| `PAGE_INDICATOR` | `paywall_page_indicator` | dots row |
| `plan(id)` | `paywall_plan_$id` | each plan card (selectable) |
| `HOT_DEAL` | `paywall_hot_deal` | HOT DEAL tab |
| `LEGAL` | `paywall_legal` | legal text |
| `SUBSCRIBE` | `paywall_subscribe` | button |
| `TERMS` / `PRIVACY` / `SUBSCRIPTION_TERMS` | `paywall_terms` / `paywall_privacy` / `paywall_subscription_terms` | links |

Suggested tests: default plan `quarterly` selected and the legal text contains "1 349,99 UAH every 3 months"; tapping `paywall_plan_weekly` selects it and the legal text shows "354,99 UAH every week"; close calls `onClose`; in `AppShell`, tapping a locked home category card shows `paywall_screen` and hides the bottom bar, close brings it back.

## Screenshot vs Figma (for information)

| Element | Original | Here |
|---|---|---|
| Background | `#151821` + magenta radial glow `#7E0A43` | `background` + `accentPurple` 30 % / `cardGlow` 70 % radial glow |
| Top bar | X, "Nebula Premium" ≈19 Bold, "Restore" ≈17 white | shared `ScreenTopBar`, "Anima Premium" `cardTitle`, "Restore" `pill` `primary` |
| Hero | empty glow ≈60–230 dp | glow + a 96 tinted feature icon per slide |
| Slide title / subtitle | ≈26 serif cream / ≈17 Regular grey, 2 lines | `name` 24 SemiBold white / `body` 13 `accentLavender` |
| Dots | 8, white / grey, pitch 15 | 6, gap 8, `onSurface` / `onSurfaceMuted` |
| Plan cards | 113 × 154, gap 16, radius ≈8, `#212535` | 118 × 132, gap 8, radius 20, `appCard` |
| Selected card | blue gradient + `#6378FD` border | `primary` 35→10 % gradient over `surface`, 2 `primary` border |
| HOT DEAL tab | light blue `#A2B9FF`, dark text, ≈17 high | `accentOrange`, white `caption`, 20 high |
| Savings badge | green `#54D993` ≈81 × 26, dark ≈15 text | `TagChip` `accentOrange`, 20 high, white `caption` |
| Legal text | ≈15, white + grey runs, 6 lines | `caption` 11, `accentLavender` charge sentence + `onSurfaceMuted`, ≈5 lines |
| Subscribe | cream `#F3F3E5` ≈324 × 43, dark ≈17 text | `PrimaryButton` `primary`, 370 × 48, `button`-sized label |
| Links | ≈15 grey-white | `caption` `primary` links, `onSurfaceMuted` dots |
| Company address | 2 lines grey | omitted (out of scope) |

## Shared components

Reuse:

| Where | Component | Use |
|---|---|---|
| `ui/components/ScreenTopBar.kt` | `ScreenTopBar(title, leading)` | top bar, close icon as `leading`; **extend** with `trailing` (below) |
| `ui/components/PrimaryButton.kt` | `PrimaryButton(label, onClick)` | Subscribe; **extend** with `height` (below) |
| `ui/components/AppCard.kt` | `Modifier.appCard` | unselected plan card (selected: same shape + gradient + 2 `primary` border) |
| `ui/components/TagChip.kt` | `TagChip(label, fill)` | "Save N%" chips |
| `ui/components/TintedIconBox.kt` | `TintedIconBox` | slide icon (96 / 48, `CircleShape`) |
| drawables `home_ic_sun`, `home_ic_moon`, `ic_heart`, `ic_message_circle` | slide icons | reuse under current names (renaming `home_*` is a Theme task) |

Extend in `ui/components/` (Theme zone, in the paywall screen Issue; the existing screens must look identical afterwards):

| Component | Change | Why |
|---|---|---|
| `ScreenTopBar` | add `trailing: (@Composable () -> Unit)? = null`, placed `CenterEnd`; when `trailing` is set the title's horizontal inset becomes **64** (reserve for a short text button) instead of `ScreenTopBarIconSize` | "Restore" on the right; no current user passes it |
| `PrimaryButton` | add `height: Dp = 32.dp` and use `RoundedCornerShape(percent = 50)` (identical to today's radius 16 at 32); label style `button` 14 SemiBold when `height ≥ 40`, else `pill` (or a `textStyle` parameter) | full-width 48 Subscribe CTA; current users unchanged |

No new shared component is created. The plan card, the tab and the dots stay in `ui/paywall/` (only one user).

## New tokens needed

None. Alphas of existing tokens (`accentPurple` 30 %, `cardGlow` 70 %, `primary` 35 % / 10 %) are literals as elsewhere (`primary` @ 20 % chip, `cardGlow` @ 50 % Compatibility glow).

## Decisions (reviewed and accepted by the orchestrator, #58)

1. **Glow colour** `accentPurple` 30 % core → `cardGlow` 70 % → transparent (Issue: our palette, not magenta). No `HeroBackground`: its moon would sit under "Restore" and compete with the slide icon.
2. **Slide icon**: a 96 `TintedIconBox` per feature in the glow (the original shows an empty glow; an icon per slide makes the pager readable and reuses existing drawables). Drop it if the orchestrator wants the empty glow; the pager then starts with a 120 spacer.
3. **Invented slides** (Issue): Daily horoscope / Compatibility readings / Tarot readings / Chat with psychics, in that order (the original's compatibility slide is 2 of 4). Texts in *Texts*.
4. **No auto-advance**, no looping; pager starts on page 0 whatever was tapped.
5. **Slide title `name` 24 SemiBold** (the largest existing style; fits one line); subtitle `body` `accentLavender`, always 2 lines high.
6. **Plan cards 118 × 132, gap 8, radius 20**: gap 16 (original) would leave 91 inside, too narrow for "1 349,99 UAH" at `button` 14. Titles take 2 lines in every card so prices line up.
7. **Badges in `accentOrange`** (our badge colour, "FREE"): both the HOT DEAL tab and the savings chips; no green, no light blue. Savings text "Save 68%" without the space.
8. **Selected card** = `primary` gradient + 2 `primary` border (the original's blue selection, mapped to `primary`). No check mark (the original has none).
9. **Legal text: two colours kept, toned down**: only the charge sentence (price and period) is emphasised in `accentLavender`, the rest `onSurfaceMuted`, all `caption` 11. Three string resources (intro, charge template, outro) so translations can keep the emphasis.
10. **Subscribe = `PrimaryButton`, 48 high, full width, `primary`** (not the cream of the original). 48 rather than the 32 of Call/Chat: the page's one CTA and a comfortable touch target. Needs the `height` parameter on `PrimaryButton`.
11. **"Restore" and the links in `primary`** (the "View All" link treatment); "Cancel anytime…" `body` `onSurface`.
12. **Whole page scrolls**, the button is not pinned (fits 402 × 874 without scrolling).
13. **Overlay without animation**, bottom bar hidden while it's shown; Android back closes it only if `BackHandler` is available without new dependencies.
14. **Locked-card wiring** through a new `onLockedClick` on the stateful `HomeScreen`, the whole locked card as the target; unlocked cards unchanged.

15. **Shared component extensions** (orchestrator): the paywall screen Issue may add the optional `trailing` slot to `ScreenTopBar` and the `height` parameter to `PrimaryButton`, both with defaults that keep every existing screen pixel-identical. No other `ui/components/**` change.
16. **Open questions below: all defaults accepted** (icon in the glow, always start on slide 1, back only if `BackHandler` needs no new dependency, `accentOrange` for both badges).

## Open questions

1. Keep the slide icon in the glow, or leave the glow empty as in the original? Default: icon (Decision 2).
2. Should the paywall open on the slide matching the tapped element (e.g. Love → Compatibility)? Default: always slide 1.
3. Is a system-back handler wanted if it needs a new dependency (`ui-backhandler`)? Default: X only, back handled only if already available.
4. `accentOrange` for both HOT DEAL and savings, or a new "success" colour for savings? Default: `accentOrange`, no new token.

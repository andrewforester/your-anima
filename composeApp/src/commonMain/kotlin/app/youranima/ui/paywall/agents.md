# ui/paywall

The Paywall ("Anima Premium"), #59: a full-screen overlay opened from locked elements (today: the locked home category cards). `ui/navigation/AppShell` draws it over the tab content and hides the bottom bar; X closes it back to the same tab. Design: `docs/design/paywall/SPEC.md`.

## Entry points (`PaywallScreen.kt`)

- `PaywallScreen(onClose, repository = MockPaywallRepository)` (stateful): loads `PaywallData` once, keeps the selected plan id (`rememberSaveable`, default `defaultPlanId` = "quarterly").
- `PaywallScreen(state, onPlanSelect, onClose, onRestore, onSubscribe(planId), onTermsClick, onPrivacyClick, onSubscriptionTermsClick)` (stateless): one `verticalScroll` column: `PaywallTopBar`, `FeaturePager` + `PageDots`, `PlanRow`, `LegalText`, then the footer (48dp `PrimaryButton` "Subscribe", "Cancel anytime…", `LegalLinks`). The pager state is pure UI and lives here. `Modifier.paywallGlow()` (`PaywallGlow.kt`) draws the radial hero glow behind the top (edge-to-edge, scrolls with the content).

## Components (one file each)

- `PaywallTopBar`: shared `ScreenTopBar` with a close X (`paywall_ic_close`, 22dp in a 44dp touch area) as `leading` and "Restore" as `trailing`.
- `FeaturePager` / `FeatureSlide`: `HorizontalPager` of `PremiumFeature` slides (96dp `TintedIconBox` circle, `name` title, 2-line lavender subtitle). No auto-advance, starts at page 0.
- `PageDots`: 6dp dots following `pagerState.currentPage`, read as "Page N of M".
- `PlanRow` + `PlanCard`: three radio-selectable cards (`appCard`; selected = `primary` gradient + 2dp `primary` border), "Save N%" `TagChip`, the orange "HOT DEAL" tab behind the card of a `PlanBadge.HotDeal` plan.
- `LegalText`: auto-renew text from three strings; the charge sentence (selected plan's price and period, non-breaking) in `accentLavender`, the rest muted.
- `LegalLinks`: Terms / Privacy / Subscription Terms (`primary` caption links).

`PaywallResources.kt`: `PremiumFeature` → title, subtitle, icon, tint; `ScreenPadding`; preview helper `PaywallPreview`. `PaywallUiState` (`features`, `plans`, `selectedPlanId`, `selectedPlan`). Test tags: `PaywallScreenTags`.

## Data

`data/paywall/` (`PaywallRepository`, `MockPaywallRepository`, UAH prices).

## Stubs

Subscribe, Restore and the three links are no-ops (hoisted callbacks). No system back handling: `BackHandler` isn't on the compile classpath without a new dependency (build files are infra). Slide icons reuse `home_ic_sun` / `home_ic_moon` (renaming to `ic_*` is a Theme task).

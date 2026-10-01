# data/paywall

Mock data source for the Paywall ("Anima Premium", `ui/paywall/PaywallScreen`), #59.

## Types (`PaywallModels.kt`)

- `PaywallData(features, plans, defaultPlanId)`: aggregate returned by the repository.
- `PremiumFeature` (`Horoscope`, `Compatibility`, `Tarot`, `Psychics`): one pager slide each; the UI (`ui/paywall/PaywallResources.kt`) maps it to title, subtitle, icon and tint.
- `SubscriptionPlan(id, title, price, period, savingsPercent, badge)`: one plan card. `price` is an opaque store string (no currency formatting in the app); `period` fills the auto-renew legal text; `savingsPercent` → "Save N%" chip; `badge = HotDeal` → orange tab above the card.

## Repository

`PaywallRepository.paywallData()` is the interface a store/billing backend would implement. `MockPaywallRepository` is the only implementation: Weekly 354,99 UAH (HOT DEAL), Every month 1 099,99 UAH (−23 %), Every 3 months 1 349,99 UAH (−68 %, default).

## Stubs

- No purchase, restore or entitlement check: Subscribe / Restore are no-ops in the UI.
- Plan titles and periods are plain English mock strings (a store would localize them).

package app.youranima.data.paywall

import androidx.compose.runtime.Immutable

/** Premium feature shown on a pager slide; the UI maps it to its title, subtitle, icon and tint. */
enum class PremiumFeature { Horoscope, Compatibility, Tarot, Psychics }

/** Marketing tab drawn above a plan card. */
enum class PlanBadge { HotDeal, }

@Immutable
data class SubscriptionPlan(
    /** Stable product id: `weekly`, `monthly`, `quarterly`. */
    val id: String,
    /** Store product name ("Every 3 months"), not a string resource. */
    val title: String,
    /** Store-formatted price ("1 349,99 UAH"); opaque, the app does no currency formatting. */
    val price: String,
    /** Billing period for the auto-renew legal text ("3 months"). */
    val period: String,
    /** 68 → "Save 68%"; `null` → no chip. */
    val savingsPercent: Int?,
    val badge: PlanBadge?,
)

/** Everything the paywall shows, as returned by [PaywallRepository]. */
@Immutable
data class PaywallData(
    val features: List<PremiumFeature>,
    val plans: List<SubscriptionPlan>,
    val defaultPlanId: String,
)

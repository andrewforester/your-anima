package app.youranima.data.paywall

/** Static offer from the original screenshot (UAH prices), SPEC → Data. */
object MockPaywallRepository : PaywallRepository {
    override fun paywallData() =
        PaywallData(
            features = PremiumFeature.entries,
            plans =
                listOf(
                    SubscriptionPlan(
                        id = "weekly",
                        title = "Weekly",
                        price = "354,99 UAH",
                        period = "week",
                        savingsPercent = null,
                        badge = PlanBadge.HotDeal,
                    ),
                    SubscriptionPlan(
                        id = "monthly",
                        title = "Every month",
                        price = "1 099,99 UAH",
                        period = "month",
                        savingsPercent = 23,
                        badge = null,
                    ),
                    SubscriptionPlan(
                        id = "quarterly",
                        title = "Every 3 months",
                        price = "1 349,99 UAH",
                        period = "3 months",
                        savingsPercent = 68,
                        badge = null,
                    ),
                ),
            defaultPlanId = "quarterly",
        )
}

package app.youranima.ui.paywall

object PaywallScreenTags {
    const val SCREEN = "paywall_screen"
    const val CLOSE = "paywall_close"
    const val RESTORE = "paywall_restore"
    const val PAGER = "paywall_pager"
    const val PAGE_INDICATOR = "paywall_page_indicator"
    const val HOT_DEAL = "paywall_hot_deal"
    const val LEGAL = "paywall_legal"
    const val SUBSCRIBE = "paywall_subscribe"
    const val TERMS = "paywall_terms"
    const val PRIVACY = "paywall_privacy"
    const val SUBSCRIPTION_TERMS = "paywall_subscription_terms"

    fun plan(id: String) = "paywall_plan_$id"
}

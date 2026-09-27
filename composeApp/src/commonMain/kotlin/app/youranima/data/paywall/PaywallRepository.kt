package app.youranima.data.paywall

/** Source of the paywall offer. [MockPaywallRepository] until a store/billing backend exists. */
interface PaywallRepository {
    fun paywallData(): PaywallData
}

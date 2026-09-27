package app.youranima.data.compatibility

/** Source of the Compatibility tab data. [MockCompatibilityRepository] until there is a backend. */
interface CompatibilityRepository {
    fun compatibilityPair(): CompatibilityPair
}

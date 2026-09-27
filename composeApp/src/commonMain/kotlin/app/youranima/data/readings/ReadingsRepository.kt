package app.youranima.data.readings

/** Source of the Readings tab data. [MockReadingsRepository] until there is a backend. */
interface ReadingsRepository {
    fun readingsContent(): ReadingsContent
}

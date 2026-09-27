package app.youranima.data.home

/** Source of the Today screen data. [MockHomeRepository] until there is a backend. */
interface HomeRepository {
    fun homeData(): HomeData
}

package app.youranima.data.home

import app.youranima.data.navigation.NavBadges

/** Source of the Today screen data and the shared nav badge counts. [MockHomeRepository] until there is a backend. */
interface HomeRepository {
    fun homeData(): HomeData

    fun navBadges(): NavBadges
}

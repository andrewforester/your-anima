package app.youranima.ui.home

import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.MoodCategory

object HomeScreenTags {
    const val SCREEN = "home_screen"
    const val ADD_STORY = "home_add_story"
    const val SETTINGS = "home_settings"
    const val USER_NAME = "home_user_name"
    const val BIRTH_CHART = "home_birth_chart"
    const val FOCUS_MOOD = "home_focus_mood"
    const val BOTTOM_BAR = "home_bottom_bar"
    const val CATEGORIES = "home_categories"
    const val TIP = "home_tip"
    const val YES = "home_yes"
    const val NO = "home_no"
    const val YES_NO = "home_yes_no"
    const val TAROT = "home_tarot"

    fun readingCard(id: String) = "home_reading_$id"

    fun askButton(id: String) = "home_ask_$id"

    fun categoryCard(id: String) = "home_category_$id"

    fun lockBadge(id: String) = "home_lock_$id"

    fun tab(period: ForecastPeriod) = "home_tab_${period.name.lowercase()}"

    fun moodRing(category: MoodCategory) = "home_mood_${category.name.lowercase()}"

    fun navItem(item: HomeNavItem) = "home_nav_${item.name.lowercase()}"
}

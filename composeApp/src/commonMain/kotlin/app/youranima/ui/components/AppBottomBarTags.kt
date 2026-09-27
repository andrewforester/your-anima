package app.youranima.ui.components

object AppBottomBarTags {
    const val BAR = "app_bottom_bar"

    fun navItem(tab: AppTab) = "app_nav_${tab.name.lowercase()}"
}

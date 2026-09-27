package app.youranima.data.navigation

import androidx.compose.runtime.Immutable

/** Badge counts shown on the shared bottom nav bar (`AppBottomBar`). */
@Immutable
data class NavBadges(
    val psychicsFree: Boolean,
    val unreadChats: Int,
)

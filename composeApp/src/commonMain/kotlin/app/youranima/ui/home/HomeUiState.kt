package app.youranima.ui.home

import androidx.compose.runtime.Immutable
import app.youranima.data.home.CategoryForecast
import app.youranima.data.home.ForecastPeriod
import app.youranima.data.home.HomeData
import app.youranima.data.home.MockHomeRepository
import app.youranima.data.home.MoodScore
import app.youranima.data.home.NavBadges
import app.youranima.data.home.ReadingOffer
import app.youranima.data.home.UserProfile

enum class HomeNavItem { Today, Psychics, Compatibility, Chatroom, Readings }

@Immutable
data class HomeUiState(
    val user: UserProfile,
    val readings: List<ReadingOffer>,
    val selectedPeriod: ForecastPeriod,
    val mood: List<MoodScore>,
    val selectedNavItem: HomeNavItem,
    val badges: NavBadges,
    val categories: List<CategoryForecast>,
    val tipOfTheDay: String,
    val yesForToday: List<String>,
    val noForToday: List<String>,
)

fun HomeData.toUiState(
    selectedPeriod: ForecastPeriod,
    selectedNavItem: HomeNavItem,
) = HomeUiState(
    user = user,
    readings = readings,
    selectedPeriod = selectedPeriod,
    mood = moodByPeriod[selectedPeriod].orEmpty(),
    selectedNavItem = selectedNavItem,
    badges = badges,
    categories = categories,
    tipOfTheDay = tipOfTheDay,
    yesForToday = yesForToday,
    noForToday = noForToday,
)

internal val PreviewHomeUiState =
    MockHomeRepository.homeData().toUiState(ForecastPeriod.Today, HomeNavItem.Today)

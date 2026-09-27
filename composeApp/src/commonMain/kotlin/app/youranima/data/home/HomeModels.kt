package app.youranima.data.home

import androidx.compose.runtime.Immutable

enum class ZodiacSign {
    Aries,
    Taurus,
    Gemini,
    Cancer,
    Leo,
    Virgo,
    Libra,
    Scorpio,
    Sagittarius,
    Capricorn,
    Aquarius,
    Pisces,
}

@Immutable
data class UserProfile(
    val name: String,
    val sun: ZodiacSign,
    val moon: ZodiacSign,
    val ascendant: ZodiacSign,
)

enum class ReadingType { Free, Paid }

@Immutable
data class ReadingOffer(
    val id: String,
    val type: ReadingType,
    val suggestedQuestion: String,
)

/** Period picked in the date tabs on the Today screen. */
enum class ForecastPeriod { Yesterday, Today, Tomorrow, Week }

enum class MoodCategory { Career, Love, Health, Family }

@Immutable
data class MoodScore(
    val category: MoodCategory,
    val percent: Int,
)

@Immutable
data class NavBadges(
    val psychicsFree: Boolean,
    val unreadChats: Int,
)

@Immutable
data class HomeData(
    val user: UserProfile,
    val readings: List<ReadingOffer>,
    val moodByPeriod: Map<ForecastPeriod, List<MoodScore>>,
    val badges: NavBadges,
)

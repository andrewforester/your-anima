package app.youranima.data.home

import app.youranima.data.navigation.NavBadges

object MockHomeRepository : HomeRepository {
    private const val QUESTION = "Will my ex and I get back together?"

    private val data =
        HomeData(
            user =
                UserProfile(
                    name = "Andrew",
                    sun = ZodiacSign.Sagittarius,
                    moon = ZodiacSign.Scorpio,
                    ascendant = ZodiacSign.Pisces,
                ),
            readings =
                listOf(
                    ReadingOffer(id = "free", type = ReadingType.Free, suggestedQuestion = QUESTION),
                ),
            moodByPeriod =
                mapOf(
                    ForecastPeriod.Yesterday to mood(career = 40, love = 55, health = 70, family = 80),
                    ForecastPeriod.Today to mood(career = 50, love = 70, health = 65, family = 60),
                    ForecastPeriod.Tomorrow to mood(career = 75, love = 45, health = 60, family = 70),
                    ForecastPeriod.Week to mood(career = 60, love = 65, health = 70, family = 55),
                ),
            categories =
                listOf(
                    CategoryForecast(
                        id = "career",
                        category = ForecastCategory.Career,
                        preview = "Today’s energy enhances your focus, so tackle the task you’ve been putting off.",
                        isLocked = true,
                    ),
                    CategoryForecast(
                        id = "love",
                        category = ForecastCategory.Love,
                        preview = "In love, the Aries moon may spark a bold conversation with someone close.",
                        isLocked = true,
                    ),
                    CategoryForecast(
                        id = "health",
                        category = ForecastCategory.Health,
                        preview = "The Aries moon encourages you to move more and rest well.",
                        isLocked = true,
                    ),
                ),
            tipOfTheDay = "Let your adventures unfold naturally.",
            yesForToday = listOf("Initiate new projects", "Plan spontaneous outings", "Engage in physical activity"),
            noForToday = listOf("Rush decisions", "Ignore others' needs", "Overcommit financially"),
        )

    override fun homeData(): HomeData = data

    override fun navBadges() = NavBadges(psychicsFree = true, unreadChats = 3)

    private fun mood(
        career: Int,
        love: Int,
        health: Int,
        family: Int,
    ) = listOf(
        MoodScore(MoodCategory.Career, career),
        MoodScore(MoodCategory.Love, love),
        MoodScore(MoodCategory.Health, health),
        MoodScore(MoodCategory.Family, family),
    )
}

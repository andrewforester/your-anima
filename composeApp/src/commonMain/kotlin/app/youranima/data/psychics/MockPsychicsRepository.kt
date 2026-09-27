package app.youranima.data.psychics

private const val PRICE = "$3,99"
private const val FREE_MINUTES = 3

/** Static mock of the Psychics tab: the two sections from the original screenshot plus an invented "Career & Money". */
object MockPsychicsRepository : PsychicsRepository {
    override fun psychicsData() =
        PsychicsData(
            promo = FreeMinutesPromo(freeMinutes = FREE_MINUTES, psychicsCount = 3),
            sections = listOf(mostAccurate, bestInLove, careerAndMoney),
        )

    private val mostAccurate =
        PsychicSection(
            id = "accurate",
            title = "Most Accurate",
            subtitle = "Psychics recognized for their exceptional accuracy in predictions and guidance",
            icon = SectionIcon.Accurate,
            psychics =
                listOf(
                    psychic("luna", "Luna", PsychicPhoto.Luna, years = 1, rating = 4.0, reviews = 45, canCall = false),
                    psychic("whimsy_lou", "Whimsy Lou", PsychicPhoto.WhimsyLou, years = 4, rating = 4.0, reviews = 436),
                    psychic("shenay", "Shenay", years = 9, rating = 5.0, reviews = 1204, canCall = false),
                    psychic("orion", "Orion", status = PsychicStatus.Busy, years = 12, rating = 4.6, reviews = 872),
                ),
        )

    private val bestInLove =
        PsychicSection(
            id = "love",
            title = "Best in Love Readings",
            subtitle = "Psychics who have earned their reputation as experts in love questions and relationship mending",
            icon = SectionIcon.Love,
            psychics =
                listOf(
                    psychic("rosalie", "Rosalie", status = PsychicStatus.Busy, years = 7, rating = 4.8, reviews = 612),
                    psychic("love_luna", "Luna", PsychicPhoto.Luna, years = 1, rating = 4.0, reviews = 45, canCall = false),
                    psychic("love_whimsy_lou", "Whimsy Lou", PsychicPhoto.WhimsyLou, years = 4, rating = 4.0, reviews = 436),
                    psychic("amara", "Amara", years = 5, rating = 4.3, reviews = 158, canChat = false),
                ),
        )

    private val careerAndMoney =
        PsychicSection(
            id = "career",
            title = "Career & Money",
            subtitle = "Advisors who help with work, business and financial decisions",
            icon = SectionIcon.Career,
            psychics =
                listOf(
                    psychic("sterling", "Sterling", years = 15, rating = 4.9, reviews = 1530),
                    psychic("maya", "Maya", status = PsychicStatus.Busy, years = 6, rating = 4.4, reviews = 204),
                    psychic("felix", "Felix", years = 3, rating = 3.8, reviews = 67, canCall = false),
                    psychic("iris", "Iris", years = 8, rating = 4.7, reviews = 389),
                ),
        )

    private fun psychic(
        id: String,
        name: String,
        photo: PsychicPhoto? = null,
        status: PsychicStatus = PsychicStatus.Online,
        years: Int,
        rating: Double,
        reviews: Int,
        canCall: Boolean = true,
        canChat: Boolean = true,
    ) = Psychic(
        id = id,
        name = name,
        photo = photo,
        status = status,
        yearsOfExperience = years,
        rating = rating,
        reviewCount = reviews,
        canCall = canCall,
        canChat = canChat,
        freeMinutes = FREE_MINUTES,
        pricePerMinute = PRICE,
    )
}

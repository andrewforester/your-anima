package app.youranima.data.readings

/** Static mock of the Readings tab: the items from the original screenshot plus invented ones so every carousel scrolls. */
object MockReadingsRepository : ReadingsRepository {
    override fun readingsContent() =
        ReadingsContent(
            challengeSection = improveYourself,
            quizSections = listOf(explorePotential, attractLove),
        )

    private val improveYourself =
        ChallengeSection(
            id = "improve",
            title = "Improve yourself",
            challenges =
                listOf(
                    Challenge(
                        id = "find_purpose",
                        days = 5,
                        title = "Find your purpose",
                        description =
                            "Ever felt lost, different, or brimming with potential but unsure how to create a life " +
                                "that truly feels like yours? Discover what drives you, one day at a time.",
                        illustration = ReadingArt.Compass,
                    ),
                    Challenge(
                        id = "free_yourself",
                        days = 12,
                        title = "Free yourself from the past",
                        description =
                            "When you let go of old hurts and patterns, life can feel lighter and new doors open. " +
                                "Release what holds you back.",
                        illustration = null,
                    ),
                    Challenge(
                        id = "daily_confidence",
                        days = 7,
                        title = "Build daily confidence",
                        description = "Small daily steps to trust yourself, speak up and stop waiting for permission.",
                        illustration = null,
                    ),
                    Challenge(
                        id = "open_heart",
                        days = 10,
                        title = "Open your heart",
                        description = "Heal old wounds and make room for the love you deserve.",
                        illustration = null,
                    ),
                ),
        )

    private val explorePotential =
        QuizSection(
            id = "potential",
            title = "Explore your potential",
            quizzes =
                listOf(
                    Quiz("witch_type", "What is your Witch Type?", durationMinutes = 3, ReadingArt.WitchHat),
                    Quiz("shaman_path", "What is your Shaman Path?", durationMinutes = 3, ReadingArt.Flame),
                    Quiz("feminine_archetype", "What is your Feminine Archetype?", durationMinutes = 3, ReadingArt.Moon),
                    Quiz("element", "Which element rules you?", durationMinutes = 2, ReadingArt.Star),
                    Quiz("spirit_animal", "What is your spirit animal?", durationMinutes = 4, ReadingArt.Spirit),
                ),
        )

    private val attractLove =
        QuizSection(
            id = "love",
            title = "Attract love into your life",
            quizzes =
                listOf(
                    Quiz("love_language", "What is your Love Language?", durationMinutes = 3, ReadingArt.Heart),
                    Quiz("new_love", "Are you ready for new love?", durationMinutes = 2, ReadingArt.HandsHeart),
                    Quiz("soulmate_sign", "Which sign is your soulmate?", durationMinutes = 3, ReadingArt.Star),
                ),
        )
}

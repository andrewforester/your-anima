package app.youranima.data.readings

/** Everything the Readings tab shows, as returned by [ReadingsRepository]. */
data class ReadingsContent(
    val challengeSection: ChallengeSection,
    val quizSections: List<QuizSection>,
)

/** The carousel of multi-day challenges ("Improve yourself"). */
data class ChallengeSection(
    val id: String,
    val title: String,
    val challenges: List<Challenge>,
)

data class Challenge(
    val id: String,
    val days: Int,
    val title: String,
    val description: String,
    /** `null` = no artwork, the card shows a glow instead. */
    val illustration: ReadingArt?,
)

/** A themed carousel of quizzes ("Explore your potential", "Attract love into your life"). */
data class QuizSection(
    val id: String,
    val title: String,
    val quizzes: List<Quiz>,
)

data class Quiz(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val illustration: ReadingArt,
)

/** Artwork key until a backend serves image URLs; the UI maps it to a bundled image or a tinted line icon. */
enum class ReadingArt { Compass, WitchHat, Flame, HandsHeart, Moon, Star, Heart, Spirit }

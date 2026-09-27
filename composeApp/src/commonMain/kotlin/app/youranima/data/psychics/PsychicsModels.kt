package app.youranima.data.psychics

/** Everything the Psychics tab shows, as returned by [PsychicsRepository]. */
data class PsychicsData(
    val promo: FreeMinutesPromo,
    val sections: List<PsychicSection>,
)

/** "You have N minutes FREE with M psychics" banner. */
data class FreeMinutesPromo(
    val freeMinutes: Int,
    val psychicsCount: Int,
)

/** A themed carousel of psychics ("Most Accurate", "Best in Love Readings", ...). */
data class PsychicSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: SectionIcon,
    val psychics: List<Psychic>,
)

/** Badge next to a section title; the UI maps it to an icon and a tint. */
enum class SectionIcon { Accurate, Love, Career }

data class Psychic(
    val id: String,
    val name: String,
    /** Placeholder photo key until a backend serves image URLs; `null` = neutral placeholder. */
    val photo: PsychicPhoto?,
    val status: PsychicStatus,
    val yearsOfExperience: Int,
    /** 0–5. */
    val rating: Double,
    val reviewCount: Int,
    val canCall: Boolean,
    val canChat: Boolean,
    val freeMinutes: Int,
    /** Preformatted by the backend, e.g. "$3,99". */
    val pricePerMinute: String,
)

enum class PsychicStatus { Online, Busy }

/** Bundled placeholder photos. */
enum class PsychicPhoto { Luna, WhimsyLou }

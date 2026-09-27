package app.youranima.data.chatroom

/** Everything the Chatroom tab shows, as returned by [ChatroomRepository]. */
data class ChatroomData(
    val promo: PsychicsPromo,
    val chats: List<ChatPreview>,
)

/** "Check our Psychics" card: avatar stack, "More than N psychics", rating. */
data class PsychicsPromo(
    val psychicsCount: Int,
    /** 0–5. */
    val rating: Double,
    val reviewCount: Int,
    /** Back-left, back-right, front; `null` = neutral placeholder. */
    val avatars: List<ChatAvatar?>,
)

/** One conversation with a psychic in the list. */
data class ChatPreview(
    val id: String,
    val psychicName: String,
    /** Placeholder photo key until a backend serves image URLs; `null` = neutral placeholder. */
    val avatar: ChatAvatar?,
    val online: Boolean,
    /** Drives the phone badge next to the name. */
    val canCall: Boolean,
    /** Preformatted ("02:54 pm", "Sept 25, 01:13 pm"); a backend would send a timestamp and the UI format it. */
    val timeLabel: String,
    val lastMessage: String,
    val unreadCount: Int,
)

/** Bundled placeholder photos. */
enum class ChatAvatar { Chandra, EstherEclipse, Ana, MissSheyna, MadamSarah, Lyrienne, PromoFront, Luna, WhimsyLou }

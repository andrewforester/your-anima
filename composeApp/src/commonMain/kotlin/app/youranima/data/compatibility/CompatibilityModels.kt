package app.youranima.data.compatibility

import androidx.compose.runtime.Immutable

/** One side of the compatibility pair. */
@Immutable
data class CompatibilityPerson(
    val name: String,
    /** Placeholder avatar key until a backend serves image URLs. */
    val avatar: PersonAvatar,
)

/** Bundled placeholder avatars. */
enum class PersonAvatar { Character, }

/** The user and their partner; [partner] is `null` until one is added. */
@Immutable
data class CompatibilityPair(
    val user: CompatibilityPerson,
    val partner: CompatibilityPerson?,
)

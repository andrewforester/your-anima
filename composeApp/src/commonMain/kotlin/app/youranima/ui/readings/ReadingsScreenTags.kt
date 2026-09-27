package app.youranima.ui.readings

object ReadingsScreenTags {
    const val SCREEN = "readings_screen"
    const val TITLE = "readings_title"

    fun section(id: String) = "readings_section_$id"

    fun carousel(id: String) = "readings_carousel_$id"

    fun challenge(id: String) = "readings_challenge_$id"

    fun quiz(id: String) = "readings_quiz_$id"
}

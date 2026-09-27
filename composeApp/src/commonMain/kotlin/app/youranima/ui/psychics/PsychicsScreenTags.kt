package app.youranima.ui.psychics

object PsychicsScreenTags {
    const val SCREEN = "psychics_screen"
    const val TITLE = "psychics_title"
    const val FAVOURITES = "psychics_favourites"
    const val PROMO = "psychics_promo"
    const val FILTER = "psychics_filter"

    fun filter(filter: PsychicFilter) = "psychics_filter_${filter.name.lowercase()}"

    fun section(id: String) = "psychics_section_$id"

    fun carousel(id: String) = "psychics_carousel_$id"

    fun viewAll(id: String) = "psychics_view_all_$id"

    fun card(id: String) = "psychics_card_$id"

    fun callButton(id: String) = "psychics_call_$id"

    fun chatButton(id: String) = "psychics_chat_$id"
}

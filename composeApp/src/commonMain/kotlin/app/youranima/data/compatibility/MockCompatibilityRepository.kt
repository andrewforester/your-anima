package app.youranima.data.compatibility

/** Static mock: the home user ("Andrew") with the home avatar, no partner yet (empty state). */
object MockCompatibilityRepository : CompatibilityRepository {
    override fun compatibilityPair() =
        CompatibilityPair(
            user = CompatibilityPerson(name = "Andrew", avatar = PersonAvatar.Character),
            partner = null,
        )
}

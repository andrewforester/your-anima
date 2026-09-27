# data/compatibility

Data for the Compatibility tab (`ui/compatibility/`, #51): the user and their partner.

- `CompatibilityPerson(name, avatar: PersonAvatar)`: one side of the pair. `PersonAvatar` is a bundled placeholder key (`Character` = the home avatar drawable), mapped to a drawable in `ui/compatibility/CompatibilityResources.kt`; a backend would replace it with an image URL.
- `CompatibilityPair(user, partner?)`: `partner == null` is the empty state the screen shows now ("Add your partner").
- `CompatibilityRepository.compatibilityPair()`; `MockCompatibilityRepository` returns the home user ("Andrew", same name as `MockHomeRepository`) and no partner.

## Stubs

Everything is mocked. No add-partner flow, sign data or compatibility results yet (future Issues).

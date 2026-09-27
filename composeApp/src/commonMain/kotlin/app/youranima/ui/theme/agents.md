# ui/theme

Design system: dark-only theme feeding every screen via `MaterialTheme.appColors` / `MaterialTheme.appTypography`.

## Types

- `AppColors` (`Color.kt`): colour tokens from `docs/design/astrology-home/SPEC.md` and `docs/design/home-feed/SPEC.md`. `DarkAppColors` is the only instance (no light variant).
- `AppTypography` (`Type.kt`): named text styles (`name`, `cardTitle`, `cardLead`, `tab`, `button`, `pill`, `body`, `input`, `ringValue`, `caption`, `badge`), all Geist. Also builds the Material 3 `Typography` (`materialTypography`) so plain Material components pick up Geist too.
- `AppShapes` (`Shape.kt`): `medium` (12dp) / `large` (20dp) corner radii.
- `AppTheme` (`Theme.kt`): the composable every screen wraps itself in (`AppTheme { ... }`); provides `LocalAppColors`/`LocalAppTypography` and a Material 3 `darkColorScheme` derived from `AppColors`.

## Conventions for consumers

Never hardcode a colour, size or string in a screen — add a token here (or a component/screen-local `dp` constant with a comment) instead. Fonts: Geist only; glyphs outside it (emoji, ⊙, ☽) don't render on web and must be drawn as vectors.

## Stubs

`ThemePreview.kt`: a `@Preview` gallery of the tokens above, for visual review — not used by the app itself.

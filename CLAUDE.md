# Your Anima

Demo clone of an existing mobile app. One Compose Multiplatform codebase for **Android, iOS and mobile Web (Kotlin/Wasm)**. No backend yet: all data is static mocks in `shared`.

## Layout

| Module | What lives there |
|---|---|
| `shared/` | All UI and logic (KMP library). `commonMain` is where 99% of code goes. |
| `shared/src/commonMain/kotlin/app/youranima/ui/theme/` | Design system: colors, typography, shapes. Single source of truth for design tokens. |
| `shared/src/commonMain/composeResources/` | Strings, images, fonts (Compose Multiplatform resources, `Res.*`, package `app.youranima.shared.resources`). |
| `shared/src/commonTest/` | UI tests (`runComposeUiTest`), run on JVM and iOS simulator. |
| `androidApp/` | Thin Android host (`MainActivity` → `App()`). |
| `iosApp/` | Thin Xcode host (SwiftUI → `MainViewController()`). |
| `webApp/` | Thin Wasm host (`ComposeViewport` → `App()`), `index.html`. |

Platform hosts must stay thin. Put platform-specific code in `shared/src/<platform>Main` via `expect`/`actual` only when unavoidable.

## Commands

```bash
./gradlew ktlintCheck                                   # lint (ktlintFormat to auto-fix)
./gradlew :shared:jvmTest                               # fast UI tests, no emulator
./gradlew :androidApp:assembleDebug                     # Android APK
./gradlew :webApp:wasmJsBrowserDistribution             # web bundle -> webApp/build/dist/wasmJs/productionExecutable
./gradlew :webApp:wasmJsBrowserDevelopmentRun           # web dev server
./gradlew :shared:iosSimulatorArm64Test                 # iOS (macOS only)
```

Before every push: `./gradlew ktlintCheck :shared:jvmTest` must pass.

Cloud sessions: the SessionStart hook (`.claude/hooks/session-start.sh`) installs the Android SDK. It needs `dl.google.com` in the environment's allowed domains; without it no Gradle build works in the container and CI is the only verifier.

## Conventions

- Kotlin official style, enforced by ktlint. Trailing commas on.
- Composables: `PascalCase`, first optional param `modifier: Modifier = Modifier`, hoisted state, a `@Preview` for every screen/component.
- Never hardcode colors, text sizes or strings in screens: use `MaterialTheme` tokens from `ui/theme` and `stringResource(Res.string.*)`.
- One screen = one package under `ui/<screen>/` (`XxxScreen.kt`, its components, `XxxScreenTags` for test tags).
- Every screen gets at least one UI test in `commonTest`.
- Mock data lives in `shared/src/commonMain/kotlin/app/youranima/data/` as plain Kotlin objects, behind a small interface so a real backend can replace it later.

## Design (Figma)

The source of truth for UI is the Figma file. When the Figma connector is available: read variables/styles first and map them into `ui/theme` before building screens; then build screens from frames. See `.claude/skills/implement-screen`.

## Git & CI

- Work in feature branches; `main` is updated only via PRs.
- CI (`.github/workflows/ci.yml`): ktlint + JVM tests, Android APK (artifact), Web bundle, iOS (PRs/main only, macOS).
- Every push publishes the web build to GitHub Pages: `main` → `/`, other branches → `/preview/<branch-with-dashes>/`. Link is in the CI run summary.

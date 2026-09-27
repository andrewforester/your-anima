# Your Anima

Demo clone of an existing mobile app. One Compose Multiplatform codebase for **Android, iOS and mobile Web (Kotlin/Wasm)**. No backend yet: all data is static mocks in `composeApp`.

## Layout

| Path | What lives there |
|---|---|
| `composeApp/` | All UI and logic (KMP library, targets: Android, iOS, Wasm, JVM for tests). Also the web entry point. |
| `composeApp/src/commonMain/kotlin/app/youranima/App.kt` | Root composable: `AppTheme { HomeScreen() }`. |
| `composeApp/src/commonMain/kotlin/app/youranima/ui/` | Design system (`ui/theme`), components, screens. |
| `composeApp/src/commonMain/composeResources/` | Strings, images, fonts (`Res.*`, package `app.youranima.resources`). |
| `composeApp/src/commonTest/` | UI tests (`runComposeUiTest`), run on JVM and iOS simulator. |
| `composeApp/src/wasmJsMain/` | Web entry (`ComposeViewport` → `App()`) and `index.html`. |
| `composeApp/src/iosMain/` | `MainViewController()` for the iOS host. |
| `androidApp/` | Thin Android host (`MainActivity` → `App()`). Separate module because AGP 9 forbids an application in a KMP module. |
| `iosApp/` | Thin Xcode host (SwiftUI → `MainViewControllerKt.MainViewController()`), framework `ComposeApp`. |
| `docs/COORDINATION.md` | Standing rules for parallel Claude sessions: file ownership, Issue labels. Read it before touching files. Task status lives in GitHub Issues, not in the repo. |
| `docs/design/<screen>/` | Design package exported once from Figma (`SPEC.md`, `screenshot.png`, `assets/`). Build from it; don't call Figma MCP. |

Platform hosts must stay thin. Put platform-specific code in `composeApp/src/<platform>Main` via `expect`/`actual` only when unavoidable.

## Commands

```bash
./gradlew ktlintCheck                                   # lint (ktlintFormat to auto-fix)
./gradlew :composeApp:jvmTest                           # fast UI tests, no emulator
./gradlew :androidApp:assembleDebug                     # Android APK
./gradlew :composeApp:wasmJsBrowserDistribution         # web bundle -> composeApp/build/dist/wasmJs/productionExecutable
./gradlew :composeApp:wasmJsBrowserDevelopmentRun       # web dev server
./gradlew :composeApp:iosSimulatorArm64Test             # iOS (macOS only)
```

Before every push: `./gradlew ktlintCheck :composeApp:jvmTest` must pass.

Cloud sessions: the SessionStart hook (`.claude/hooks/session-start.sh`) installs the Android SDK, routes Maven Central through Google's mirror (the shared egress IP gets 429s), and seeds yarn's offline mirror with the `github:Kotlin/karma` tarball (codeload.github.com is blocked, git reads are not). After it, every command above except iOS works in the container. It needs `dl.google.com` in the environment's allowed domains; without it no Gradle build works in the container and CI is the only verifier.

## Conventions

- Kotlin official style, enforced by ktlint. Trailing commas on.
- Composables: `PascalCase`, first optional param `modifier: Modifier = Modifier`, hoisted state, a `@Preview` for every screen/component.
- Never hardcode colors, text sizes or strings in screens: use `MaterialTheme` tokens from `ui/theme` and `stringResource(Res.string.*)`.
- One screen = one package under `ui/<screen>/` (`XxxScreen.kt`, its components, `XxxScreenTags` for test tags).
- Every screen gets at least one UI test in `commonTest`.
- Mock data lives in `composeApp/src/commonMain/kotlin/app/youranima/data/` as plain Kotlin objects, behind a small interface so a real backend can replace it later.

## Design (Figma)

The source of truth for UI is the Figma file. When the Figma connector is available: read variables/styles first and map them into `ui/theme` before building screens; then build screens from frames. See `.claude/skills/implement-screen`.

## Git & CI

- Work in feature branches; `main` is updated only via PRs.
- CI (`.github/workflows/ci.yml`): PRs run only ktlint + JVM tests. Builds (Android APK artifact, Web bundle, iOS on macOS) run on push to `main` (after merge) and on manual runs. Pushes to feature branches trigger no CI.
- Push to `main` publishes the web build to GitHub Pages `/`. A branch preview (`/preview/<branch-with-dashes>/`) is published only by a manual run (Actions → CI → Run workflow on that branch). The latest `main` debug APK is re-published to the `main-latest` prerelease: `https://github.com/andrewforester/your-anima/releases/download/main-latest/your-anima-debug.apk`. Links are in the CI run summary.

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
| `docs/COORDINATION.md` | Standing rules for parallel Claude sessions: file ownership, design source of truth, Issue labels. Read it before touching files. |
| `docs/design/<name>/` | Design packages (`SPEC.md`, `screenshot.png`, `assets/`). Build from them; don't call Figma MCP. `astrology-home` = top of the home screen, from Figma. `home-feed` = home screen below Focus & Mood, from a screenshot. |

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

## Architecture & code quality

- **Layers:** `data/` (models, repository interfaces, mocks) → screen state holder (UI state + events, e.g. a `ViewModel`/presenter when a screen gets logic) → stateless composables (state in, callbacks out). UI never reads mocks directly; it gets state.
- **Unidirectional data flow:** immutable UI state (`data class`, `@Immutable` where useful), events as callbacks, no business logic in composables.
- **Small files:** one component (plus its preview) per file; split a file when it grows past ≈200–250 lines or does two jobs. Composables past ≈60 lines get split into named sub-composables.
- **Don't duplicate (DRY):** before writing a component, look in `ui/components/` and other screens. If a second screen needs the same piece, move it to `ui/components/` (a Theme-zone PR, see `docs/COORDINATION.md`) instead of copying it. Same for dimensions and styles: reuse tokens, add a token rather than repeat a literal.
- **Single responsibility, clear names, no dead code**, no speculative abstractions (YAGNI): build what the Issue asks, in a shape a real backend can plug into.
- **Package docs:** every code package you create or change has an `agents.md`: a short business description of what the package does (which screen or feature, what the user sees, main types and how they connect, where the data comes from, known stubs). Keep it under ≈40 lines, write it for the next agent, update it in the same PR as the code.

## Skills (roles)

`.claude/skills/`: `orchestrate` (coordinator: Issues, sessions, merge, reports), `develop` (a session working one Issue), `design` (design package from a screenshot, no Figma), `implement-screen` (how to build a screen), `quick-fix` (small fixes: filing, launching, working them), `qa-release` (watches `main` after merges via the CI-watch PR #75, reverts or files fixes).

## Design

- **Figma is the reference** for sizes, colours, type, radii and spacing. It is an improved version of the original app but covers only the top of the first screen (`docs/design/astrology-home/`).
- **Screenshots of the original app** set only the content of further screens/parts; their blocks are restyled in the Figma language (existing tokens, Geist, Figma card style). Details: `docs/COORDINATION.md` → Design source of truth.
- Only the orchestrator calls Figma MCP (20 calls/month); see `.claude/skills/orchestrate`.

## Process

GitHub Issues hold the whole working process: status labels, session ids, scope changes, questions and decisions, web screenshots of results (stored on the orphan branch `screens`, embedded in Issue comments). Each closed Issue gets a closing comment with the Claude usage (model, USD, context, tokens). Issues declare `Depends on: #N`; the orchestrator launches them as their dependencies merge. The orchestrator's reports to the human include a cost table. The repository holds only the product and the standing rules; PR bodies are short (`Closes #N` + what changed).

## Git & CI

- Work in feature branches; `main` is updated only via PRs.
- CI (`.github/workflows/ci.yml`): non-draft PRs run ktlint, JVM tests **and Web smoke** (builds the wasm bundle and runs a Playwright startup/navigation check against it, screenshots uploaded as the `web-smoke-screenshots` artifact). Android APK and iOS builds run on push to `main` (after merge) and on manual runs. Pushes to feature branches and draft PRs trigger no CI; a PR's CI starts when it is marked Ready for review.
- Push to `main` publishes the web build to GitHub Pages `/`. A branch preview (`/preview/<branch-with-dashes>/`) is published only by a manual run (Actions → CI → Run workflow on that branch). The latest `main` debug APK is re-published to the `main-latest` prerelease: `https://github.com/andrewforester/your-anima/releases/download/main-latest/your-anima-debug.apk`. Links are in the CI run summary.

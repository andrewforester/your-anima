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
| `docs/COORDINATION.md` | Standing rules for parallel Claude sessions: file ownership, design source of truth, the tracker (Linear: statuses, labels, brief format) and **Tooling** (the concrete commands the skills' general steps map to). Read it before touching files. |
| `docs/design/<name>/` | Design packages (`SPEC.md`, `screenshot.png`, `assets/`). Build from them; don't call Figma MCP. `astrology-home` = top of the home screen, from Figma. `home-feed` = home screen below Focus & Mood, from a screenshot. |

Platform hosts must stay thin. Put platform-specific code in `composeApp/src/<platform>Main` via `expect`/`actual` only when unavoidable.

## Commands

Skills refer to these slots by name (*lint*, *format*, *test*, *build*, *run*, *web check*).

| Slot | Command | Notes |
|---|---|---|
| lint | `./gradlew ktlintCheck` | |
| format | `./gradlew ktlintFormat` | auto-fix for *lint* |
| test | `./gradlew :composeApp:jvmTest` | fast UI tests, no emulator |
| build | `./gradlew :composeApp:wasmJsBrowserDistribution` | web bundle → `composeApp/build/dist/wasmJs/productionExecutable`. Android APK: `./gradlew :androidApp:assembleDebug`. iOS (macOS only): `./gradlew :composeApp:iosSimulatorArm64Test` |
| run | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` | web dev server |
| web check | serve the *build* output with `python3 -m http.server`, screenshot with Playwright at 402×874 | create the page with `locale: 'en-US'` (headless Chromium otherwise makes Compose Wasm throw `RangeError: Incorrect locale information provided`); any `pageerror` fails the check: a green build can still crash at startup |

Before every push: *lint* and *test* must pass.

Local sessions (launched by a local orchestrator, see `docs/COORDINATION.md` → Tooling → Sessions): install what you need yourself (JDK 21, Android SDK, Playwright with its Chromium). The session-start hook does not run locally.

Cloud sessions: `.claude/hooks/session-start.sh` installs the Android SDK, routes Maven Central through Google's mirror (the shared egress IP gets 429s), and seeds yarn's offline mirror with the `github:Kotlin/karma` tarball (codeload.github.com is blocked, git reads are not). After it, every command above except iOS works in the container. It needs `dl.google.com` in the environment's allowed domains; without it no Gradle build works in the container and CI is the only verifier.

## Conventions

- Kotlin official style, enforced by ktlint. Trailing commas on.
- Composables: `PascalCase`, first optional param `modifier: Modifier = Modifier`, hoisted state, a `@Preview` for every screen/component.
- Never hardcode colors, text sizes or strings in screens: use `MaterialTheme` tokens from `ui/theme` and `stringResource(Res.string.*)`.
- One screen = one package under `ui/<screen>/` (`XxxScreen.kt`, its components, `XxxScreenTags` for test tags).
- Every screen gets at least one UI test in `commonTest` (`runComposeUiTest`, checks key content is displayed).
- **Building a screen or UI component:** work only from its design package `docs/design/<screen>/` (`SPEC.md`, `screenshot.png`, `assets/`) and never call Figma MCP; with only an image, take the style from Figma and the theme (`docs/COORDINATION.md` → Design source of truth). Write down the component tree before coding. Tokens first: a new colour, text style, shape or spacing goes into `ui/theme/`; reuse an existing token when the value matches within ≈2 px or the colour is near-identical. Icons are vector drawables (`composeResources/drawable/*.xml`) with plain single-colour paths, tinted in code; no `aapt:attr` gradients (broken on Android), draw gradients with a `Brush`. Images go to `composeResources/drawable/<screen>_*`, strings to `composeResources/values/strings_<screen>.xml` with keys `<screen>_*`. Glyphs missing from Geist (emoji, ⊙, ☽) don't render on web: draw them as vectors. Key elements get test tags in `XxxScreenTags`. Finish with the *web check*: compare its screenshot side by side with the design and fix visible differences.
- Mock data lives in `composeApp/src/commonMain/kotlin/app/youranima/data/` as plain Kotlin objects, behind a small interface so a real backend can replace it later.

## Architecture & code quality

- **Layers:** `data/` (models, repository interfaces, mocks) → screen state holder (UI state + events, e.g. a `ViewModel`/presenter when a screen gets logic) → stateless composables (state in, callbacks out). UI never reads mocks directly; it gets state.
- **Unidirectional data flow:** immutable UI state (`data class`, `@Immutable` where useful), events as callbacks, no business logic in composables.
- **Small files:** one component (plus its preview) per file; split a file when it grows past ≈200–250 lines or does two jobs. Composables past ≈60 lines get split into named sub-composables.
- **Don't duplicate (DRY):** before writing a component, look in `ui/components/` and other screens. If a second screen needs the same piece, move it to `ui/components/` (a Theme-zone PR, see `docs/COORDINATION.md`) instead of copying it. Same for dimensions and styles: reuse tokens, add a token rather than repeat a literal.
- **Single responsibility, clear names, no dead code**, no speculative abstractions (YAGNI): build what the task asks, in a shape a real backend can plug into.
- **Package docs:** every code package you create or change has an `AGENTS.md` (upper case: the name coding agents look for) and next to it a `CLAUDE.md` whose only line is `@AGENTS.md` (Claude Code loads it when it works in that folder). `AGENTS.md` says **why** the package exists, not how it works: the user or business problem it solves and the result it delivers (which screen or feature, what the user gets), the domain terms it uses, how it fits the architecture (layer, who uses it, what it depends on, where its data comes from), and known stubs and limits. Leave implementation details (functions, props, control flow, file-by-file tours) to the code: good code shows them. Keep it under ≈40 lines, write it for the next agent, update it in the same PR as the code. The repository root works the same way: this file is the root `AGENTS.md`, and the root `CLAUDE.md` only imports it.

## Skills (roles)

`.claude/skills/`: `orchestrate` (coordinator: tracker tasks, sessions, merge, reports), `develop` (a session working one task), `design` (design package from a screenshot), `quick-fix` (small fixes: filing, launching, working them), `qa-release` (watches `main` after merges via the CI-watch PR #75, reverts or files fixes). Skills describe roles in general terms; project-specific tools and commands live in `docs/COORDINATION.md` → Tooling and in this file. They cover the process (roles, tracker, PRs, zones); engineering technique comes from Anthropic's global skills when the environment has them (`docs/COORDINATION.md` → Tooling → Global skills).

## Design

The design reference (Figma, top of the home screen only) and the rules for screenshots of the original app are in `docs/COORDINATION.md` → Design source of truth. Only the orchestrator calls Figma MCP (20 calls/month).

## Process

The tracker is **Linear** (team {{TRACKER_TEAM}}, project **Your Anima**, one ticket `{{TRACKER_KEY}}-N` per task; GitHub Issues are no longer used, the old ones stay as history). **Not connected yet:** the Linear workspace is set up through the project's Linear MCP in a follow-up; until then `{{TRACKER_TEAM}}` / `{{TRACKER_KEY}}` are placeholders (fill them with one search-and-replace) and new tasks can't be filed. It holds the whole working process: status, Role/Type labels, dependencies (blocked-by relations), session names/ids, scope changes, questions and decisions, web screenshots of results (uploaded straight to the ticket, never committed), and a closing comment with the Claude usage (model, USD when known, context, tokens). Working sessions report in PR comments; the orchestrator mirrors to the ticket. The orchestrator's reports to the human include a cost table. The repository holds only the product and the standing rules; PR bodies are short (`Closes {{TRACKER_KEY}}-N` + what changed). Details: `docs/COORDINATION.md` → Tracker.

## Git & CI

- Work in feature branches; `main` is updated only via PRs.
- CI (`.github/workflows/ci.yml`): non-draft PRs run ktlint, JVM tests **and Web smoke** (builds the wasm bundle and runs a Playwright startup/navigation check against it, screenshots uploaded as the `web-smoke-screenshots` artifact). Android APK and iOS builds run on push to `main` (after merge) and on manual runs. Pushes to feature branches and draft PRs trigger no CI; a PR's CI starts when it is marked Ready for review.
- Deliverables of a push to `main`: the web build on GitHub Pages `/` (https://andrewforester.github.io/your-anima/) and the debug APK re-published to the `main-latest` prerelease (https://github.com/andrewforester/your-anima/releases/download/main-latest/your-anima-debug.apk). A branch preview (`/preview/<branch-with-dashes>/`) is published only by a manual run (Actions → CI → Run workflow on that branch). Links are in the CI run summary.

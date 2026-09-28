# Parallel sessions in one repository

Standing rules only: who changes which files and how sessions stay out of each other's way. This file does **not** change per task.

- **Tasks, their status and the whole working process** live in GitHub Issues (see below).
- **Orchestration** (who launches sessions, who merges, when to notify the human) is in `.claude/skills/orchestrate`.

## General rules

1. **One session, one zone.** A zone is the set of paths a session may change. The Issue sets it. Everything else is read-only for that session.
2. **One screen part, one session.** A screen lives in the package `ui/<screen>/` (`XxxScreen.kt`, components, `XxxScreenTags`), its test in `commonTest/.../ui/<screen>/`. A screen can be built in several rounds (one design package and one Issue per round).
3. **Small PRs, frequent merges of `main`.** Run `git merge origin/main` before starting and before the PR. No rebase.
4. **CI is the referee.** Before pushing, run `./gradlew ktlintCheck :composeApp:jvmTest`. PR CI runs (only once the PR is out of draft) lint, JVM tests and a Web smoke job (build + Playwright startup/navigation check, screenshots in the `web-smoke-screenshots` artifact). Android/iOS builds run after merge to `main`. If `main` goes red after a merge, fixing it is the top priority.
5. **Need something outside your zone?** Don't change it. Say so in an Issue comment and continue on a local stub.
6. **Roles are skills:** `orchestrate` (coordinator), `develop` (session on an Issue), `design` (design package from a screenshot), `implement-screen` (how to build a screen), `quick-fix` (small fixes).
7. **Only the coordinator calls Figma MCP.** The plan allows 20 calls a month. The coordinator exports each frame once into `docs/design/<screen>/` (`SPEC.md`, `screenshot.png`, `assets/`). Sessions work from those files.

## Design source of truth

1. **The Figma file is the reference** for sizes, colours, type, radii, spacing and component style. It is an improved version of the app, but it covers only the top of the first screen (`docs/design/astrology-home/`).
2. **Screenshots of the original app** show *what* is on a screen (blocks, content, texts, icons, behaviour). They don't set the style. A design package from a screenshot restyles every block in the Figma language: existing theme tokens, Geist, the Figma card style (fill, border, radius, padding), the Figma spacing grid. Screenshot colours, fonts and sizes are used only when Figma has no equivalent role, and then they become new tokens.
3. When a screenshot and Figma disagree, Figma wins. Record the difference in the package, don't copy the screenshot.

## Process lives in Issues

Everything about *how the work is going* goes into the Issue, as comments: launch (session id), scope changes, questions, decisions, blockers, verification results, **web screenshots of the result**, and at closing **the Claude usage of the work (model, USD, context, tokens)**. The repository holds only the product (code, resources, design packages) and the standing rules. The PR body stays short: what changed and `Closes #N`.

**Screenshots** are stored on the orphan branch `screens` (never merged), path `issue-<N>/<name>.png`, and embedded in the Issue comment by their raw URL:
`https://raw.githubusercontent.com/andrewforester/your-anima/screens/issue-<N>/<name>.png`.
Don't commit screenshots to feature branches.

**Questions never block a session.** Nobody is watching it. Write the question in an Issue comment, pick the most conservative option, note it, and keep going. The coordinator or the human answers in the Issue.

## Hot spots

Each has one owner: a role, not a particular session. The Issue names the role.

| What | Owner | Others |
|---|---|---|
| `settings.gradle.kts`, `*/build.gradle.kts`, `gradle/libs.versions.toml`, modules, `.github/workflows/**`, `.github/dependabot.yml` | Scaffold (`infra`) | ask in the Issue |
| Entry points: `MainActivity`, `MainViewController`, `wasmJsMain/**`, `App.kt` | Scaffold | a screen may only register itself as the start screen in `App.kt` |
| `ui/theme/**`, `composeResources/font/**` | Theme (`theme`) | the theme merges **before** screens that depend on it |
| `ui/components/**` | Theme | a component lives in its screen package first; when a second screen needs it, a separate PR moves it |
| Strings | each screen has its own `values/strings_<screen>.xml` with keys `<screen>_*` | the shared `strings.xml` belongs to Theme |
| Images, icons | `drawable/<screen>_*`; shared icons `drawable/ic_*` belong to Theme | never rename other screens' resources |
| `data/**` | interfaces and models: the first screen that needs them | a screen's mocks live in `data/<screen>/` |
| `docs/**`, `CLAUDE.md`, `.claude/**`, `.github/ISSUE_TEMPLATE/**`, `.github/pull_request_template.md` | coordinator or human | others propose changes in a PR |

## Issues and labels

One Issue = one session = one PR (`Closes #N`). The orchestrator opens the branch and a **draft** PR before launching the session and subscribes to it; the session pushes there and marks the PR Ready for review when done, which starts CI and signals the orchestrator. Issues use the templates in `.github/ISSUE_TEMPLATE` (`design`, `screen`, `task`): design package, zone, **out of scope**, dependencies, done-when.

| Label | Meaning |
|---|---|
| `design`, `screen`, `theme`, `infra`, `docs` | task type (default zone). `design` = design package from a screenshot, zone `docs/design/<screen>/**` |
| `fix` | small fix, handled with the `quick-fix` skill (combined with a type label for the zone) |
| `status: ready` | the Issue is complete and can be launched |
| `status: in progress` | a session works on it (session id in a comment) |
| `status: blocked` | waiting for a dependency or a decision, reason in a comment |
| `needs: human` | needs an answer from the human |

When the status changes, remove the old label.

**Dependencies.** Every Issue body has a line `Depends on: #A, #B` (merged first) or `Depends on: none`, and optionally `Starts on branch of: #C` (may start once #C's branch exists and merge it). An Issue with an unmet dependency is `status: blocked`; the orchestrator flips it to `status: ready` and launches it when the dependencies are merged (at most 3 sessions at once). Details: `.claude/skills/orchestrate` → Queue with dependencies.

**Closing comment** (orchestrator): merged PR, verification, and the session's Claude usage: model, USD, context used / max, input and output tokens. The PR closes the Issue via `Closes #N`; labels on closed Issues don't matter.

## Merge order

Scaffold first, then theme, then screens (in parallel, any order). A screen can start before the theme is merged if the theme Issue fixes the API contract (token names). The screen then merges the theme branch as soon as it appears.

## Scaffold decisions (reference)

- **Package:** `app.youranima`. UI code is in `composeApp/src/commonMain/kotlin/app/youranima/ui/**`.
- **Resources:** `composeApp/src/commonMain/composeResources/`, class `Res` in package `app.youranima.resources`.
- **Modules:**
  - `composeApp`: KMP library (Android, iOS, Wasm, JVM for tests) and the web entry point;
  - `androidApp`: thin host (AGP 9 doesn't allow the application plugin in a KMP module);
  - `iosApp`: Xcode project, framework `ComposeApp`.
- **Dependencies** go through the version catalog. Keep `compose-material3` on the same line as `compose-multiplatform`: a newer alpha breaks the web start (LinkError in skiko). Dependabot doesn't bump it.
- **Vector drawables** in `composeResources`: plain single-colour paths only; `aapt:attr` gradients render broken on Android. Gradients are drawn in code with a `Brush`.
- **Theme:** `app.youranima.ui.theme.AppTheme`, `MaterialTheme.appColors`, `MaterialTheme.appTypography`. Font: Geist. Glyphs outside Geist (emoji, ⊙, ☽) don't exist on web, so draw them as vectors.
- **ktlint** 1.8.0. `@Composable` functions in PascalCase are allowed.

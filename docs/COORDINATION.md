# Parallel sessions in one repository

Standing rules: who changes which files, how sessions stay out of each other's way, and **which concrete tools** the process uses. The skills in `.claude/skills/` describe roles in general terms ("the tracker", "open a draft PR", "launch a session"); this file and `AGENTS.md` say how that is done in this project. This file does **not** change per task.

- **Tasks, their status and the whole working process** live in the tracker: Linear (see Tracker).
- **Orchestration** (who launches sessions, who merges, when to notify the human) is in `.claude/skills/orchestrate`; the commands it uses are in Tooling.

## General rules

1. **One session, one zone.** A zone is the set of paths a session may change. The task sets it. Everything else is read-only for that session.
2. **One screen part, one session.** A screen lives in the package `ui/<screen>/` (`XxxScreen.kt`, components, `XxxScreenTags`), its test in `commonTest/.../ui/<screen>/`. A screen can be built in several rounds (one design package and one task per round).
3. **Small PRs, frequent merges of `main`.** Run `git merge origin/main` before starting and before the PR. No rebase.
4. **CI is the referee.** Before pushing, run *lint* and *test* (`AGENTS.md` → Commands). PR CI runs (only once the PR is out of draft) lint, JVM tests and a Web smoke job (build + Playwright startup/navigation check, screenshots in the `web-smoke-screenshots` artifact). Android/iOS builds run after merge to `main`. If `main` goes red after a merge, fixing it is the top priority.
5. **Need something outside your zone?** Don't change it. Say so in a comment (see Tracker → Where sessions write) and continue on a local stub.
6. **Roles are skills:** `orchestrate` (coordinator), `develop` (session on a task), `design` (design package from a screenshot), `quick-fix` (small fixes), `qa-release` (health of `main` after merges: CI, deploy checks, reverts). The task's **Role** label says which one runs it (Tracker → Labels).
7. **Only the coordinator calls design-tool MCPs** (Figma): the plan allows 20 calls a month. The coordinator exports each frame once into `docs/design/<screen>/` (`SPEC.md`, `screenshot.png`, `assets/`). Sessions work from those files.

## Design source of truth

The style reference is the **Figma file**, Starter plan: **20 MCP calls/month**, only the coordinator calls it. It is an improved version of the original app but covers only the top of the first screen (`docs/design/astrology-home/`). Further screens come from **screenshots of the original app**.

1. **The style reference** (Figma) sets sizes, colours, type, radii, spacing and component style.
2. **Screenshots** (of an existing app, a competitor, a sketch) show *what* is on a screen: blocks, content, texts, icons, behaviour. They don't set the style. A design package from a screenshot restyles every block in the Figma language: existing theme tokens, Geist, the Figma card style (fill, border, radius, padding), the Figma spacing grid. Screenshot colours, fonts and sizes are used only when the reference has no equivalent role, and then they become new tokens.
3. When a screenshot and the reference disagree, the reference wins. Record the difference in the package, don't copy the screenshot.

## Tracker: Linear

GitHub Issues are **not** used. Everything about *how the work is going* lives in Linear: launch (session name/id), scope changes, questions, decisions, blockers, verification results, **web screenshots of the result**, and at closing **the Claude usage of the work (model, USD, context, tokens)**. The repository holds only the product (code, resources, design packages) and the standing rules.

- **Where:** team **Your Anima** (key `YOU`). Linear project **Your Anima** (a separate project only for a big epic); one **ticket per task** (`YOU-N`) = one session = one branch `claude/<short>` = one PR.
- **Statuses:** Backlog (filed, blocked by a dependency or a decision) → Todo (complete, can be launched) → In Progress (a session works on it) → In Review (PR marked Ready, waiting for verify/merge) → Done (merged). Canceled / Duplicate as usual.
- **Dependencies:** Linear relations, not text: `blocked by` for hard dependencies (must be merged first); a soft dependency ("may start once YOU-N's branch exists and merge it") is written in the brief as `Starts on branch of: YOU-N` and linked as `related`. Two tasks touching the same file are always a hard dependency. A screen is always blocked by its design task.
- **Brief (ticket description)**, self-contained because the session never sees the chat:
  ```
  ## What needs to be done
  ## Design package        (docs/design/<name>/, or "none")
  ## Zone                  (may change / must not change; see Hot spots)
  ## Out of scope
  ## Dependencies          (blocked by / starts on branch of; mirrors the relations)
  ## Done when
  ```
- **PR ↔ ticket:** the branch is `claude/<short>`; the PR body starts with `Closes YOU-N`. Linear's GitHub integration is **not connected** (the GitHub account is already linked to another Linear workspace), so nothing moves by itself: the orchestrator attaches the PR link to the ticket when it opens the draft PR, moves the ticket to In Review when the PR is marked Ready, and to Done after the merge. Keep `Closes YOU-N` anyway, so a later integration picks it up. Template: `.github/pull_request_template.md`.

### Labels
Label groups, one label from each group per ticket:

| Group | Labels | Meaning |
|---|---|---|
| **Role** (team) | Research · Architecture · Design · Development · QA · DevOps · Docs | who (which role/skill) executes the task. Research → a written answer/comparison, no code; Architecture → system design, ADR, API contract (docs only); Design → a design package (`design` skill or the orchestrator from Figma); Development → screens, theme, features, backend (`develop`); QA → verification, device checks, reverts (`qa-release`); DevOps → CI, build, hosting, domains, environment; Docs → process rules, skills, the root `AGENTS.md`. |
| **Type** (workspace) | Feature · Improvement · Bug · Chore | what kind of change. A Bug with Role Development is a quick fix (`quick-fix` skill). |

Plus **Needs human**: waiting for the human's answer or action (the question is in a ticket comment). The zone (which folders) is in the brief, not in labels.

### Where sessions write
Working sessions may not have the Linear tools. They post questions, deviations and their final report as **PR comments**, and on the ticket too when they can. The orchestrator mirrors decisions, the verification result and the closing comment to the ticket.

**Screenshots** (web results, before/after, the human's device screenshots) are uploaded **straight to the Linear ticket** and embedded in a ticket comment as `![<name>](<assetUrl>)` (steps in Tooling → Tracker); the PR comment links to that ticket comment. They are never committed to git. A session without the Linear tools leaves its PNGs in `/tmp/YOU-<N>/` and says so in its PR comment; the orchestrator uploads them.

**Questions never block a session.** Nobody is watching it. Write the question as a comment, pick the most conservative option, note it, and keep going. The coordinator or the human answers.

**Closing comment** (orchestrator, on the ticket): merged PR, verification, and the session's Claude usage: model, USD (when known), context used / max, input and output tokens.

## Tooling

Concrete commands behind the general steps in the skills. When a tool here stops working, fix this section, not the skills.

### Tracker (Linear MCP)
- Server: **`linear-your-anima`** (workspace https://linear.app/your-anima), a local-scope MCP of the main checkout `~/workspace/your-anima`: sessions started there (or with `claude --bg` from there) have it; worktree and cloud sessions don't, so they report in PR comments and the orchestrator mirrors. Never use another Linear connector for this project.
- File / update a ticket: `save_issue` (`team: Your Anima`, `project`, `labels: [<Role>, <Type>]`, `state`, `blockedBy`, `relatedTo`, `description` = the brief). Project per epic: `save_project`.
- Comment: `save_comment`. Read: `get_issue`, `list_issues` (`project`, `state`), `list_comments`.
- Attach a screenshot, one file at a time:
  1. `prepare_attachment_upload` (`issue: YOU-N`, `filename`, `contentType: image/png`, `size` = exact bytes, e.g. `stat -f%z` on macOS).
  2. Within 60 s: `curl -sS -o /dev/null -w "%{http_code}" -X PUT --data-binary @<file> <uploadRequest.url>` with **every** header from `uploadRequest.headers` verbatim (`content-type`, `cache-control`, `x-goog-content-length-range`, `Content-Disposition`); expect `200`.
  3. `create_attachment_from_upload` (`issue`, `assetUrl`).
  4. Embed in a ticket comment (`save_comment`) as `![<name>](<assetUrl>)`, the plain `assetUrl` without a signature (Linear signs it). Read images back with `extract_images`.
  Keep images reasonable: crop close-ups, JPEG for large full-page mobile shots.

### Code host (GitHub)
- Cloud sessions use the GitHub MCP tools; local sessions use the `gh` CLI.
- **Open the branch and draft PR** (orchestrator, before launch): from `origin/main` push one empty commit without touching the checkout: `c=$(git commit-tree "$(git rev-parse 'origin/main^{tree}')" -p origin/main -m "Start YOU-N: <title> [skip ci]")`, `git push origin "${c}:refs/heads/claude/<short>"` (braces matter in zsh). `[skip ci]` anywhere in a head commit message skips CI, so never quote it in other commit messages. Then a **draft** PR to `main`: title = the task, body = `Closes YOU-N` + one line "the session marks it ready when done" (`create_pull_request` with `draft: true` / `gh pr create --draft`).
- **Mark ready** (session, last step): `update_pull_request` with `draft: false` / `gh pr ready <P>`. It starts CI and is the orchestrator's signal.
- **Follow a PR** (orchestrator; sessions after marking ready): cloud → `subscribe_pr_activity` (events for CI, comments, ready, merge). Local → one `Monitor` script polling all open task PRs every ≥ 120 s with `gh pr view <P> --json isDraft,state,statusCheckRollup,comments`, printing a line only when the draft flag, the state, the `Lint & tests` / `web-smoke` results or the count of non-bot comments change (ignore `linear-code` and `vercel`); re-arm when it expires (30 min). The GitHub API limit (5,000/h) is shared with every session.
- `ready_for_review` events can get lost: always keep a fallback check-in (below) and, when it fires, look at **all** open PRs.
- **CI on `main`** has no event of its own: the permanent draft PR **"CI watch: main (never merge)"** (head `main`, base `ci-watch`, created by `scripts/bootstrap.sh`) stands in for it; `qa-release` follows it. Never merge, close or mark it ready, never push to `ci-watch`.

### Sessions
The orchestrator launches each task as a new agent in a **new session of the same kind as itself**, with the brief, the branch, the draft PR, the skill to use and the standing rules in the prompt (template in `.claude/skills/orchestrate` → Launch a session): the orchestrator runs in the cloud → a new cloud session; the orchestrator runs locally → a new local background session **with Remote Control**, so the human can follow and steer it from the Claude app.

- **Cloud orchestrator → new cloud session:** `create_session` with `source_url` = repo, `source_revision` = `outcome_branch` = `claude/<short>`, `permission_mode: auto`, `model` (below), `tags: [your-anima, YOU-N]`, `title: "YOU-N <short title>"`. Fallback check-in with `send_later` at the expected finish (design ≈ 15 min, theme ≈ 10, screen part ≈ 20–25, quick fix ≈ 12); cancel with `delete_trigger` when the ready signal comes. Usage after merge: `get_session` → `external_metadata.usage` (`cost_usd`, tokens) and `context_usage`; then `archive_session`. A cloud session can't be messaged: steer it with a comment it reads, or launch a follow-up session on the same branch. Don't pass messages via Routines (`fire_trigger` always starts a new session).
- **Local orchestrator → new local background session with Remote Control:** one git worktree per task (`git worktree add .claude/worktrees/<short> claude/<short>`), prompt written to `<scratchpad>/prompt-YOU-N.md`, then from the worktree:
  ```
  claude --bg -n "YOU-N <short title>" --remote-control "YOU-N <short title>" \
    --model <model> --effort <effort> --permission-mode auto "$(cat <prompt file>)"
  ```
  `--bg` runs it in the background and prints its id; `--remote-control` turns Remote Control on from the start (the log shows `/remote-control is active` and a claude.ai/code link), so it appears in the Claude app under that name. Record the id and name on the ticket. It shows in `claude agents` (attach: `claude attach <id>`, log: `claude logs <id>`). Message it with `SendMessage` (name from `ListAgents`), e.g. to resume after a usage-limit stop (`claude --bg --resume <id>` also works). Remove the worktree after merge (`git worktree remove`). Usage: what the session reports (model, tokens, duration; USD when shown).
- **Models:** Sonnet (`claude-sonnet-5-5` / `--model sonnet --effort medium`) for theme tokens, small fixes, docs, mechanical tasks; Opus (`claude-opus-5-5` / `--model opus --effort high`) for research, architecture, scaffold, design packages, screens, backend. At most 3 sessions at once (2 Opus locally: they share the account's usage limit); check the limit before a batch (cloud: `get_session` → `rate_limit_info`).
- **Global skills:** tell sessions to use Anthropic's design / system-design / architecture skills (`engineering:system-design`, `engineering:architecture`, frontend design) when available in their environment, after the project skills. `develop` and `quick-fix` call `engineering:debug` and `engineering:testing-strategy` by name; code review and architecture stay with the orchestrator and its Research / Architecture sessions. The plugins (`engineering`, `superpowers`) come from the human's claude.ai account, not from `.claude/settings.json`: pinning a git marketplace there duplicates them locally and may not be fetchable in a cloud environment with a restricted network. A session without them works from the project skills alone.
- **Connectors load at session start.** One the human connects mid-session is invisible to the running orchestrator: for an action that needs it, launch a short session (a cheaper model) with that one action instead of asking the human.
- **Secrets.** Auto mode blocks sessions from reading local env files and moving tokens; don't route that through another session. Only the orchestrator moves secrets, and only with the human's explicit permission: host env vars as the host's secret/sensitive type, a worktree that needs them gets a copy of the git-ignored local env file. Never commit or paste them into PR/ticket comments. Don't remove a worktree that holds the only copy of that file.
- **Worktree isolation.** An orchestrator that entered an isolated worktree (`EnterWorktree`) can't run `git` or `claude --bg` against other worktrees; leave it (`ExitWorktree`) before orchestrating.
- **Didn't work (as of Sept 2026), don't retry:** the `Agent` tool with `isolation: "remote"` silently runs in a local worktree (its subagents are invisible and share the orchestrator's usage); starting a cloud session through a Routine (`RemoteTrigger`) is denied in auto mode. A host MCP answering `403 … re-authenticate to this scope` → the human reconnects that connector; creating a host project from a Git repo → `repo_not_found` → the host's GitHub App has no access to the repo (GitHub → Settings → Applications → <host> → Repository access).
- **Hand-off to a new orchestrator:** handoff comment on the epic's project/main ticket, then launch the new orchestrator the same way as a task session (cloud: `create_session` without a branch; local: `claude --bg -n "orchestrator <epic>" --remote-control "orchestrator <epic>" --model opus --effort high --permission-mode auto "…"` in the main checkout) with "Use the orchestrate skill. Continue <epic>; handoff: <link>".

### Notifications and deploy checks
- To the human: chat message + `PushNotification` (reaches the phone only while Remote Control is connected); anything the human must do (a key, a setting, a DNS record) also goes into a ticket comment with **Needs human**, since the Linear app notifies the phone.
- Deploy checks: a push to `main` publishes the web to GitHub Pages and the debug APK to the `main-latest` prerelease (`AGENTS.md` → Git & CI); `qa-release` checks both. There is no PR preview unless someone runs the CI workflow manually on the branch, so a session proves its change with the local *web check* and CI's Web smoke. No backend yet.

## Hot spots

Each has one owner: a role, not a particular session. The task names the role.

| What | Owner | Others |
|---|---|---|
| `settings.gradle.kts`, `*/build.gradle.kts`, `gradle/libs.versions.toml`, modules, `.github/workflows/**`, `.github/dependabot.yml`, `.claude/hooks/**` | Scaffold (DevOps) | ask in a comment |
| Entry points: `MainActivity`, `MainViewController`, `wasmJsMain/**`, `App.kt` | Scaffold | a screen may only register itself as the start screen in `App.kt` |
| `ui/theme/**`, `composeResources/font/**` | Theme (Development) | the theme merges **before** screens that depend on it |
| `ui/components/**` | Theme | a component lives in its screen package first; when a second screen needs it, a separate PR moves it |
| Strings | each screen has its own `values/strings_<screen>.xml` with keys `<screen>_*` | the shared `strings.xml` belongs to Theme |
| Images, icons | `drawable/<screen>_*`; shared icons `drawable/ic_*` belong to Theme | never rename other screens' resources |
| `data/**` | interfaces and models: the first screen that needs them | a screen's mocks live in `data/<screen>/` |
| `docs/**`, root `AGENTS.md` and `CLAUDE.md`, `.claude/skills/**`, `.claude/settings.json`, `.github/pull_request_template.md` | coordinator or human | others propose changes in a PR |

## Merge order

Scaffold first, then theme, then screens (in parallel, any order). A screen can start before the theme is merged if the theme task fixes the API contract (token names). The screen then merges the theme branch as soon as it appears.

## Scaffold decisions (reference)

- **Package:** `app.youranima`. UI code is in `composeApp/src/commonMain/kotlin/app/youranima/ui/**`.
- **Target viewport:** phone, 402×874 logical px (the Figma frame width).
- **Resources:** `composeApp/src/commonMain/composeResources/`, class `Res` in package `app.youranima.resources`.
- **Modules:**
  - `composeApp`: KMP library (Android, iOS, Wasm, JVM for tests) and the web entry point;
  - `androidApp`: thin host (AGP 9 doesn't allow the application plugin in a KMP module);
  - `iosApp`: Xcode project, framework `ComposeApp`.
- **Dependencies** go through the version catalog. Keep `compose-material3` on the same line as `compose-multiplatform`: a newer alpha breaks the web start (LinkError in skiko). Dependabot doesn't bump it.
- **Vector drawables** in `composeResources`: plain single-colour paths only; `aapt:attr` gradients render broken on Android. Gradients are drawn in code with a `Brush`.
- **Theme:** `app.youranima.ui.theme.AppTheme`, `MaterialTheme.appColors`, `MaterialTheme.appTypography`. Font: Geist. Glyphs outside Geist (emoji, ⊙, ☽) don't exist on web, so draw them as vectors.
- **ktlint** 1.8.0. `@Composable` functions in PascalCase are allowed.
- **History:** tasks up to Sept 2026 were GitHub Issues (with screenshots on the orphan branch `screens`); they stay as history, new work is in Linear.

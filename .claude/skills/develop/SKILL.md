---
name: develop
description: Work a Your Anima GitHub Issue as a developer session — stay inside the Issue's zone, build the feature or screen, verify locally (lint, tests, web check), and open a PR with "Closes #N" for the orchestrator to merge. Use when a session is started on an Issue, told to implement a task/feature/fix/screen from an Issue, or given the develop role.
---

# Develop

You are one working session on one Issue. The orchestrator launched you; a human is usually not watching.

## Start
1. `git fetch origin && git merge origin/main` on your branch (`claude/<short>`). Never rebase or force-push.
2. Read `CLAUDE.md`, `docs/COORDINATION.md` and the **whole Issue** through the GitHub MCP tools: task, design package, zone, depends-on, done-when. The Issue is your only brief.
3. If the Issue depends on another branch that isn't in `main` yet, merge that branch (`git merge origin/<branch>`) as soon as it exists. Use only the API contract the Issue names.

## Work
- **Zone.** Change only the paths the Issue lists. If you need something outside the zone (a token, a dependency, an `App.kt` hookup that isn't listed), comment on the Issue with exactly what and why, and continue on a local stub (e.g. a private constant marked `TODO(<owner>)`). List every stub in the PR.
- **Screens and UI components:** follow `.claude/skills/implement-screen`. Work from `docs/design/<screen>/` (`SPEC.md`, `screenshot.png`, `assets/`). **Never call Figma MCP.**
- **Conventions** are in `CLAUDE.md`:
  - theme tokens only, no hardcoded colours or sizes;
  - `stringResource` with a per-screen strings file;
  - a `@Preview` for every screen and component;
  - hoisted state;
  - mocks behind an interface in `data/<screen>/`.
- Commit and push early and often. The sandbox can restart, and the orchestrator watches your branch.

## Verify before every push
- `./gradlew ktlintCheck :composeApp:jvmTest` must be green. Run `ktlintFormat` to auto-fix.
- For UI:
  1. Build `./gradlew :composeApp:wasmJsBrowserDistribution`.
  2. Serve `composeApp/build/dist/wasmJs/productionExecutable` with `python3 -m http.server`.
  3. Take a Playwright screenshot at 402×874 with `locale: 'en-US'`, and treat any `pageerror` as a failure.
  4. Compare with `screenshot.png` and fix visible differences.
- Glyphs missing from Geist (emoji, ⊙, ☽) don't render on web; draw them as vectors.
- If the environment can, also run `./gradlew :androidApp:assembleDebug`.

## Finish
1. `git merge origin/main` again, re-run the checks, then push.
2. Open a PR to `main` (template: `.github/pull_request_template.md`) with `Closes #N`. Include:
   - what changed;
   - deviations from the design and why;
   - stubs and `TODO`s;
   - how you verified it;
   - the web screenshot. To embed it: commit the PNG, link it from the PR by that commit's raw URL, then remove it in the next commit.
3. `subscribe_pr_activity` on your PR. Fix red CI and review comments until it's green.
4. Don't merge. The orchestrator verifies and merges.
5. If you're blocked, comment on the Issue with exactly what is missing, push what you have, and stop. Don't guess or substitute.

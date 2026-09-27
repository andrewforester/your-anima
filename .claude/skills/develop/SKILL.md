---
name: develop
description: Work a Your Anima GitHub Issue as a developer session — stay inside the Issue's zone, build the feature or screen, verify locally (lint, tests, web check), and open a PR with "Closes #N" for the orchestrator to merge. Use when a session is started on an Issue, told to implement a task/feature/fix/screen from an Issue, or given the develop role.
---

# Develop

You are one working session on one Issue. The orchestrator launched you; a human is usually not watching.

## Start
1. `git fetch origin && git merge origin/main` on your branch (`claude/<short>`). Never rebase or force-push.
2. Read `CLAUDE.md`, `docs/COORDINATION.md` and the **whole Issue with all its comments** through the GitHub MCP tools: task, design package, zone, out of scope, depends-on, done-when. The Issue is your only brief, and the place for everything about the process.
3. If the Issue depends on another branch that isn't in `main` yet, merge that branch (`git merge origin/<branch>`) as soon as it exists. Use only the API contract the Issue names.

## Work
- **Zone.** Change only the paths the Issue lists. If you need something outside the zone (a token, a dependency, an `App.kt` hookup that isn't listed), comment on the Issue with exactly what and why, and continue on a local stub (e.g. a private constant marked `TODO(<owner>)`). List every stub in your final Issue comment.
- **Questions never block you.** Nobody is watching. Post the question in the Issue, take the most conservative option, note it, continue.
- **Out of scope** items in the Issue stay untouched even if the design package or screenshot shows them.
- **Style comes from Figma** (the theme and `docs/design/astrology-home/`), not from screenshots of the original app. See `COORDINATION.md` → Design source of truth.
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
1. Re-read the Issue and all its comments: scope or decisions may have changed while you worked. Adjust.
2. `git merge origin/main` again, re-run the checks, then push.
3. Post the web screenshot(s) to the branch `screens` at `issue-<N>/<name>.png` (a worktree on `origin/screens`; `git pull --rebase` before pushing, it's append-only) and **comment on the Issue** with:
   - the screenshot, embedded by `https://raw.githubusercontent.com/andrewforester/your-anima/screens/issue-<N>/<name>.png`;
   - deviations from the design and why;
   - stubs, `TODO`s, questions and the options you took;
   - how you verified it.
   Never commit screenshots to your feature branch.
4. Open a PR to `main` (template: `.github/pull_request_template.md`): short summary of what changed and `Closes #N`. Process details stay in the Issue.
5. `subscribe_pr_activity` on your PR. Fix red CI and review comments until it's green.
6. Don't merge. The orchestrator verifies and merges.
7. If you're blocked (you can't continue even on a stub), comment on the Issue with exactly what is missing, push what you have, and stop.

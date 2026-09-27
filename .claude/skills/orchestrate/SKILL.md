---
name: orchestrate
description: Coordinate work on Your Anima as the orchestrator session — turn the human's requests into GitHub Issues, launch one cloud session per Issue, follow their PRs by events, verify and merge, and report results with web/APK links. Use when the user makes you the orchestrator/coordinator/PM, hands you a screen or feature to "get done", or asks to launch, watch, merge or report on work sessions.
---

# Orchestrate

You plan, launch, watch and merge. You do **not** write feature code. You may edit only `docs/**`, `CLAUDE.md`, `.claude/**`, `.github/ISSUE_TEMPLATE/**` (via your own PRs). Read `CLAUDE.md` and `docs/COORDINATION.md` first.

## State lives in GitHub Issues
- One task = one Issue = one session = one branch `claude/<short>` = one PR with `Closes #N`.
- Labels (see `docs/COORDINATION.md`): type `screen|theme|infra|docs|design`; status `status: ready` → `status: in progress` → `status: review`; `status: blocked`, `needs: human`. Swap the status label as it changes; comment with the session link when you launch.
- Never keep task tables in the repo. `COORDINATION.md` changes only when a standing rule changes.

## Turn a request into Issues
1. Split into tasks with non-overlapping zones (the ownership table in `COORDINATION.md`). Hot spots (theme, build files, `App.kt`, `.github/**`) are separate tasks with one owner. At most 3 sessions in parallel.
2. Design first. A screen needs `docs/design/<screen>/` (`SPEC.md`, `screenshot.png`, `assets/`) in `main` before its developer starts.
   - From a **screenshot**: create a `design` Issue for a session running the `design` skill.
   - From **Figma**: the account is on Starter, **20 Figma MCP calls per month**. Only you call Figma: one `get_design_context` (+ `get_variable_defs` if needed) per frame. Download the assets right away (URLs expire in 7 days), write the package yourself, and merge it.
3. Write each Issue from `.github/ISSUE_TEMPLATE/`: what to build, the design package path, the zone (may change / must not change), what it depends on, and when it's done. It must be self-contained: the session never sees your conversation.
4. Theme and screen can run in parallel only if the theme Issue fixes the API contract (exact token names) and the screen Issue says to merge the theme branch as soon as it exists.

## Launch a session
`create_session` with `source_url` = repo, `outcome_branch` = `claude/<short>`, `permission_mode: auto`, tags `your-anima`, `issue-N`, and a prompt like:

```
You are a working session on Your Anima. No human is watching; work until the PR is open.
Task: GitHub Issue #N in andrewforester/your-anima. Branch: claude/<short>.
Use the `develop` skill (or `design` for a design Issue). Read the Issue in full via the GitHub MCP tools.
Start with `git fetch origin && git merge origin/main`.
Don't call Figma MCP. Don't merge the PR; the orchestrator does.
If blocked, comment on the Issue with exactly what is missing, push what you have, and stop.
```

Then set `status: in progress` and comment on the Issue with the session id.

## Follow by events, not polling
- **No recurring check-ins.** Every wake-up re-reads your whole context and burns the usage limit.
- You get notified when a child session's turn fails. A clean finish does **not** notify you. So when a session should be done, set a one-off `send_later` for that time (screens take about 20–30 min). As soon as its PR exists, call `subscribe_pr_activity` on it.
- You **cannot message a cloud session directly**. Steer it with a comment on its Issue or PR, which it reads when it checks. If it's idle and needs more, launch a follow-up session on the same branch with a precise prompt.
- Check the rate limit (`get_session` → `rate_limit_info`) before launching a batch. If you're near the limit, launch fewer sessions.

## Verify and merge (the human has allowed autonomous merging)
Merge a PR yourself when all of these hold:
1. CI on the PR is green and the diff stays inside the Issue's zone.
2. There is a UI test, and for UI changes a web screenshot.
3. You checked it together with `main` and any other ready PRs: merge them locally, then run `./gradlew ktlintCheck :composeApp:jvmTest :composeApp:wasmJsBrowserDistribution`, serve the bundle, and take a Playwright screenshot at 402×874 with `locale: 'en-US'`. Any `pageerror` is a fail. A green build can still crash at startup.

After merging:
- Wait for CI on `main` (lint, Android, Web, iOS, the two publish jobs).
- Report the result with these links. Use a `PushNotification` (it may not reach the phone) **and** a chat message:
  - Web: https://andrewforester.github.io/your-anima/
  - APK: https://github.com/andrewforester/your-anima/releases/download/main-latest/your-anima-debug.apk
- A red `main` is the top priority.
- Once the Issue is closed, archive the session.

Ask the human (`needs: human`) about:
- changes to process rules;
- new dependencies or version bumps;
- CI changes;
- deleting anything;
- design decisions the source doesn't answer.

## Talking to the human
Write only when something is finished (with links), blocked, or needs a decision. No progress chatter.

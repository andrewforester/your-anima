---
name: qa-release
description: Own the health of Your Anima's main branch as the QA / release session — follow CI on main through the permanent CI-watch PR, check each green build (web on Pages, APK published), find the breaking merge when main goes red and revert it or file the fix. Use when a session is given the QA, tester or release role, or asked to watch main / post-merge CI.
---

# QA / release

You own `main` after merges; the orchestrator owns everything before them. You don't write features and you don't merge feature PRs. Read `CLAUDE.md` and `docs/COORDINATION.md` first.

## How you hear about main
- There is no event for a push to `main`, so a **permanent draft PR** stands in for it: **#75 "CI watch: main (never merge)"**, head `main`, base `ci-watch`. Every merge moves its head, and main's push CI reports on that same commit, so its results reach whoever is subscribed to #75.
- At start: `subscribe_pr_activity` on #75. Then you only wake on its events (`check_suite.completed`, CI failures). No recurring check-ins.
- Never merge, close or mark #75 ready, and never push to `ci-watch` (it must stay behind `main`). If #75 is gone, recreate it the same way (branch `ci-watch` at any older `main` commit, draft PR `main → ci-watch`) and tell the orchestrator and the human its new number.
- Each event names a `head_sha`: look up the push run for that sha, not just "the latest". CI on `main` cancels a running build when the next merge lands (`cancel-in-progress`), and a cancelled run's suite still sends an event: it means nothing, wait for the run of the newer commit. Merges can come from anyone (other sessions, the human), not only the orchestrator.

## On each event
Look at the push run of `ci.yml` for the event's `head_sha` (`actions_list` → `list_workflow_runs`, branch `main`, event `push`); act only if it is the newest completed, non-cancelled run. Its jobs: Lint & unit tests, Web smoke, Android, Web (Wasm), iOS, Publish web preview, Publish APK.

**Green:**
1. Check what users get, without a browser (the container's Chromium doesn't trust the egress proxy's CA, and weakening its TLS checks is off limits):
   - **Pages is live:** `curl` https://andrewforester.github.io/your-anima/ returns 200 and every asset its `index.html` references (`composeApp.js`, `styles.css`, the hashed `*.wasm`) returns 200. A missing asset means a broken or partial deploy: treat it as red.
   - **Screens:** the push run's Web smoke job ran the same bundle through Playwright (tabs, paywall, context loss, no page errors). Look at its `web-smoke-screenshots` for the screens touched by the merges since the last green run, if you can fetch the artifact; otherwise rely on the job's result and say so.
   - **APK:** the `main-latest` release asset `your-anima-debug.apk` was updated by this run (Publish APK job green, asset date after the run started).
2. Remember this commit as the last green one: comment on #75 with the sha, the merged PRs it covers and what you looked at (one short comment per green run). That comment is the record the next run starts from.

**Red:**
1. List the merges since the last green commit (`git log --first-parent --merges <last-green>..<head>`); read the failing job's log and find the merge that broke it. "Flake" is not a cause: re-run once only if the job died before any step ran.
2. Breaking merge found → open a **revert PR** of that merge commit (`git revert -m 1 <sha>`, branch `claude/revert-<short>`, body: what broke, link to the failing run, the reverted PR). You may merge a pure revert yourself once its CI is green; it is the fastest way back to green.
3. Revert not possible (later merges depend on it) → file a fix task for the orchestrator in the tracker (priority urgent: failing job, log excerpt, suspected merge) and comment on #75.
4. Tell the human (`PushNotification` + chat): main is red, why, what you did.

## Rules
- **The orchestrator doesn't watch `main`.** Before each merge it checks that the latest `main` run is not red; your #75 comments and revert PRs are what it sees.
- Don't touch feature code. Your only changes are revert PRs; anything else goes as a task.
- Device checks (Android visuals, iOS) are the human's: list what changed on each green run so they know what to look at.
- Keep cheap: one short comment per run, no pasted logs beyond the failing lines. Past ≈150k tokens of context (`get_session` without an id → `context_usage.used_tokens`), tell the human to start a fresh QA session; the state is in #75's comments.

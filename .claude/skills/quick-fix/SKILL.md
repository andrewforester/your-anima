---
name: quick-fix
description: Small Your Anima fixes (a visual glitch, wrong colour/icon/spacing, a platform setting like the status bar) — how the orchestrator files and launches them and how a session works one. Use when the human reports a small bug or polish item, or when a session is started on an Issue labelled fix.
---

# Quick fix

A fix is small: one symptom, a few files, no design package, no new screen. If it needs a design decision or touches many zones, it is not a quick fix: use the normal flow (`orchestrate` / `develop`).

## Orchestrator
1. **Where does it go?**
   - An open Issue already owns the files (e.g. a restyle in progress)? Don't start a parallel session. Comment on that Issue with the symptom and the expected result, and check for it before merging.
   - Otherwise, one Issue per fix, template `task.yml`, labels `fix` + the zone's type (`infra` for hosts/entry points, `theme`, `screen`) + `status: ready`. Several fixes in the same zone can share one Issue.
2. **Issue content:** symptom (platform, where on screen, the human's words), expected result, likely cause if you know it, zone (exact files), out of scope, done-when (before/after screenshot in the Issue, checks green).
3. **Launch** with `model: claude-sonnet-5`, branch `claude/fix-<short>`, the standard prompt from `orchestrate` with "Use the `quick-fix` skill". Check-in `send_later` ≈ 12 min. A quick fix should cost about $1; if the Issue needs measurements, say exactly which (e.g. "two screenshots, no network throttling").
   Human's device screenshots go to the `screens` branch and into the Issue.
   If two fixes touch the same file, run them one after the other (see `orchestrate`).
4. **Merge** per `orchestrate` → Verify and merge. For platform-only fixes the web screenshot may not show the change; then check the diff carefully and rely on CI's Android/iOS builds after merge.

## Session
1. `git fetch origin && git merge origin/main`. Read `CLAUDE.md`, `docs/COORDINATION.md`, the Issue and all its comments.
2. **Reproduce first.** Find the cause in code; for UI take a "before" web screenshot (Playwright 402×874, `locale: 'en-US'`). For platform settings, find where the platform decides the behaviour (manifest, `Info.plist`, `index.html`, `enableEdgeToEdge`, …).
3. **Minimal fix** inside the zone. No refactors, no new dependencies (ask in the Issue if one is needed). Keep Figma as the style reference. If the fix changes what a package does, update its `agents.md` (create one if the package has none).
4. **Verify, proportionately:** `./gradlew ktlintCheck :composeApp:jvmTest`; for UI also the web bundle + "after" screenshot, any `pageerror` fails; for Android changes `./gradlew :androidApp:assembleDebug`. Add or adjust a test when the fix is testable (UI tags, token values). Don't run long performance experiments (network throttling, repeated timed loads) unless the Issue asks; one before/after pair is enough. You have no emulator: list what needs a check on a real Android/iOS device.
5. **Report in the Issue:** cause, fix, before/after screenshots (branch `screens`, `issue-<N>/<name>.png`, embedded by raw URL), what you couldn't verify (e.g. iOS without macOS).
6. PR to `main`: one-line summary + `Closes #N`. `subscribe_pr_activity`, fix red CI. Don't merge, and don't schedule check-ins: the orchestrator follows the PR.
7. Never wait for answers: post the question in the Issue, take the conservative option, continue.

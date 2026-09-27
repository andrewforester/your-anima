---
name: design
description: Turn a screenshot (or photo/mock) of an app screen into a Your Anima design package — docs/design/<screen>/SPEC.md with tokens, measurements, structure, texts and behaviour, plus screenshot.png and assets/ — without calling Figma. Use when asked to spec, describe, reverse-engineer or prepare the design of a screen from an image, or when a session is started on an Issue labelled design.
---

# Design from a screenshot

Output: `docs/design/<screen>/` in the same shape as `docs/design/astrology-home/`, which is your reference for format and depth. A developer session will build the screen **only** from this package, so anything you leave out gets guessed.

**Never call Figma MCP**: the account has 20 calls/month. Your zone is `docs/design/<screen>/**` and nothing else.

## 1. Get the image into the repo
- Find the screenshot:
  - a path the human or Issue gives you;
  - an attachment saved under `~/.claude/uploads/`;
  - a file in the repo.

  If you can only *see* it in the chat and have no file, ask the human to attach it as a file or commit it, and stop.
- Save it as `docs/design/<screen>/screenshot.png`, keeping the original resolution.
- Record the pixel size and the scale: dp = px ÷ (image width ÷ logical width). Assume a 402 dp-wide phone unless the image says otherwise (status-bar height, known device).

## 2. Map to the existing design system first
- Read `ui/theme/Color.kt`, `ui/theme/Type.kt` and `ui/theme/Shape.kt`.
- Sample colours from the image. For example, use Python/PIL: average a few pixels in flat areas, never on antialiased edges.
- Match each colour to an existing `AppColors` token when it's within about 3% per channel, and each text style to an `AppTypography` style when size and weight match.
- Put only the genuinely new values in a **"New tokens needed"** table, with suggested names. The theme owner adds them in a separate `theme` Issue.
- Font: assume Geist unless it's clearly different; if it is, say so.

## 3. Measure and describe
Write `SPEC.md` with these sections (copy the headings from `astrology-home/SPEC.md`):
- **Source**: the image, the date, what it shows, and the scale used.
- **Colours**: each colour as a token name, hex value and where it's used. Mark each one as *existing* or *new*.
- **Typography**: each style as its token, size/weight and where it's used. Estimate size from cap height (cap ≈ 0.7 × font size for Geist) and round to whole sp.
- **Layout**, top to bottom: every block with size, padding, gap, corner radius, border, shadow and alignment. Round to a 2 dp or 4 dp grid, and state how sure you are when a value is an estimate ("≈16").
- **Texts**: every visible string, verbatim. These become `strings_<screen>.xml`.
- **Icons and images**, for each one:
  - which existing drawable matches (`drawable/home_ic_*`, `ic_*`);
  - or a precise description (shape, stroke width, size, colour) for a new vector;
  - or, for photos and avatars, a crop saved to `assets/` (PIL crop at full resolution, noting it's a placeholder).
- **States and behaviour**: taps, selection, scrolling, and anything implied but not shown. List the open questions for the human instead of inventing answers.
- **Data**: what should come from mocks (names, numbers, lists).

## 4. Check yourself
- Re-read the screenshot block by block against `SPEC.md`: every visible element must appear once.
- Check the sums: the widths of the items in a row plus the gaps and paddings should equal the screen width.

## 5. Deliver
- Commit only `docs/design/<screen>/**` and open a PR with `Closes #N`, listing the new tokens and open questions.
- Don't merge; the orchestrator merges and then files the `theme` and `screen` Issues from your package.

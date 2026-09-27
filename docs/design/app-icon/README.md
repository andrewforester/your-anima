# App icon

**Concept:** a crescent moon drawn as a line (the hero moon of the home screen) with a four-point sparkle and a small star dot. Line style and round caps of the in-app icons, one violet→mint gradient on a dark navy field. Chosen from three concepts (constellation, moon, "anima" A-glyph) posted in Issue #43; the others stay in `tools/concepts.py`.

**Source:** `icon.svg` (108×108 adaptive-icon grid, artwork inside the 66-unit safe-zone circle). It is generated, like every export, from `tools/concepts.py`; edit the geometry there, not the SVG.

**Colours** (from `ui/theme/Color.kt`):

| Role | Token | Value |
|---|---|---|
| Background (solid) | `background` | `#0D0F2B` |
| Gradient start (top-left) | `accentPurple` | `#B388FF` |
| Gradient end (bottom-right) | `accentTeal` | `#26D0CE` |

**Exports** (`python3 tools/build.py`; needs Python 3 + Pillow and the global Playwright npm package, whose Chromium rasterises the SVG):

| Platform | Files |
|---|---|
| Android | `drawable/ic_launcher_{background,foreground,monochrome}.xml` (vectors), `mipmap-anydpi-v26/ic_launcher{,_round}.xml` (adaptive + themed-icon monochrome), legacy `mipmap-*dpi/ic_launcher{,_round}.png` 48–192 px (rounded square / circle) |
| iOS | `AppIcon.appiconset/app-icon-1024.png`, full bleed, no alpha |
| Web | `wasmJsMain/resources/favicon.svg`, `favicon-32.png` (rounded), `apple-touch-icon.png` 180 px (full bleed, no alpha), linked from `index.html` |

Flat exports show the visible 72×72 centre of the grid (viewBox `18 18 72 72`), so all platforms frame the mark the same way.

**Switching concept:** `python3 tools/build.py export <n>` (or change `CHOSEN`). `python3 tools/build.py previews <dir>` renders the concept sheets (512 + 48 px) and Android circle/squircle mask previews; those images go to the `screens` branch, not here.

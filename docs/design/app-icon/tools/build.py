"""Builds the app icon for every platform from concepts.py.

  python3 build.py                  # export the chosen concept (CHOSEN) to Android, iOS, web
  python3 build.py export 3         # export another concept instead
  python3 build.py previews OUT_DIR # concept sheets and Android mask previews (not committed)

Needs Python 3 with Pillow and the global Playwright npm package (Chromium).
Run from anywhere; paths are resolved from the repository root.
"""

import json
import math
import subprocess
import sys
import tempfile
from pathlib import Path

from PIL import Image, ImageDraw

from concepts import BACKGROUND, CONCEPTS, GRADIENT_END, GRADIENT_FROM, GRADIENT_START, GRADIENT_TO

CHOSEN = 2

TOOLS = Path(__file__).resolve().parent
ROOT = TOOLS.parents[3]
DESIGN = TOOLS.parent
RES = ROOT / "androidApp/src/main/res"
IOS = ROOT / "iosApp/iosApp/Assets.xcassets/AppIcon.appiconset"
WEB = ROOT / "composeApp/src/wasmJsMain/resources"
LEGACY_SIZES = {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}
FULL_VIEW = "0 0 108 108"
FLAT_VIEW = "18 18 72 72"  # the part of an adaptive icon a launcher shows


# ---------- SVG ----------


def svg(shapes, view=FLAT_VIEW, corner=0.0, background=True):
    x, y, w, _ = (float(v) for v in view.split())
    gx1, gy1 = GRADIENT_FROM
    gx2, gy2 = GRADIENT_TO
    body = [
        '<defs><linearGradient id="g" gradientUnits="userSpaceOnUse" '
        f'x1="{gx1}" y1="{gy1}" x2="{gx2}" y2="{gy2}">'
        f'<stop offset="0" stop-color="{GRADIENT_START}"/><stop offset="1" stop-color="{GRADIENT_END}"/>'
        "</linearGradient></defs>",
    ]
    if background:
        body.append(f'<rect x="{x}" y="{y}" width="{w}" height="{w}" rx="{w * corner}" fill="{BACKGROUND}"/>')
    for kind, d, width in shapes:
        if kind == "stroke":
            body.append(
                f'<path d="{d}" fill="none" stroke="url(#g)" stroke-width="{width}" '
                'stroke-linecap="round" stroke-linejoin="round"/>',
            )
        else:
            body.append(f'<path d="{d}" fill="url(#g)"/>')
    return f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="{view}">\n  ' + "\n  ".join(body) + "\n</svg>\n"


# ---------- Android vector drawables ----------


def vector(paths):
    return (
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<vector xmlns:android="http://schemas.android.com/apk/res/android"\n'
        '    xmlns:aapt="http://schemas.android.com/aapt"\n'
        '    android:width="108dp"\n    android:height="108dp"\n'
        '    android:viewportWidth="108"\n    android:viewportHeight="108">\n' + "".join(paths) + "</vector>\n"
    )


def gradient_attr(name):
    gx1, gy1 = GRADIENT_FROM
    gx2, gy2 = GRADIENT_TO
    return (
        f'        <aapt:attr name="android:{name}">\n'
        f'            <gradient android:type="linear" android:startX="{gx1}" android:startY="{gy1}"\n'
        f'                android:endX="{gx2}" android:endY="{gy2}"\n'
        f'                android:startColor="{GRADIENT_START}" android:endColor="{GRADIENT_END}" />\n'
        "        </aapt:attr>\n"
    )


def vector_path(kind, d, width, solid=None):
    head = f'    <path\n        android:pathData="{d}"'
    if kind == "stroke":
        head += (
            f'\n        android:strokeWidth="{width}"\n        android:strokeLineCap="round"'
            '\n        android:strokeLineJoin="round"'
        )
        colour = "strokeColor"
    else:
        colour = "fillColor"
    if solid:
        return head + f'\n        android:{colour}="{solid}" />\n'
    return head + ">\n" + gradient_attr(colour) + "    </path>\n"


# ---------- raster ----------


def render(jobs):
    """jobs: list of (svg_text, png_path, size)."""
    with tempfile.TemporaryDirectory() as tmp:
        spec = []
        for i, (text, png, size) in enumerate(jobs):
            src = Path(tmp) / f"{i}.svg"
            src.write_text(text)
            spec.append({"svg": str(src), "png": str(png), "size": size})
        (Path(tmp) / "jobs.json").write_text(json.dumps(spec))
        subprocess.run(["node", str(TOOLS / "render.mjs"), str(Path(tmp) / "jobs.json")], check=True)


def opaque(png):
    Image.open(png).convert("RGB").save(png, optimize=True)


def squircle_mask(size, n=4.0):
    r = size / 2
    steps = 720
    pts = []
    for i in range(steps):
        t = 2 * math.pi * i / steps
        c, s = math.cos(t), math.sin(t)
        pts.append((r + r * math.copysign(abs(c) ** (2 / n), c), r + r * math.copysign(abs(s) ** (2 / n), s)))
    mask = Image.new("L", (size, size), 0)
    ImageDraw.Draw(mask).polygon(pts, fill=255)
    return mask


def circle_mask(size):
    mask = Image.new("L", (size, size), 0)
    ImageDraw.Draw(mask).ellipse((0, 0, size - 1, size - 1), fill=255)
    return mask


# ---------- targets ----------


def export(number):
    name, shapes = CONCEPTS[number]
    (DESIGN / "icon.svg").write_text(svg(shapes, view=FULL_VIEW))

    # Android adaptive icon + legacy PNGs.
    drawable = RES / "drawable"
    (drawable / "ic_launcher_background.xml").write_text(
        vector([f'    <path\n        android:pathData="M0,0h108v108h-108z"\n        android:fillColor="{BACKGROUND}" />\n']),
    )
    (drawable / "ic_launcher_foreground.xml").write_text(vector([vector_path(*s) for s in shapes]))
    (drawable / "ic_launcher_monochrome.xml").write_text(vector([vector_path(*s, solid="#FFFFFFFF") for s in shapes]))
    adaptive = (
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
        '    <background android:drawable="@drawable/ic_launcher_background" />\n'
        '    <foreground android:drawable="@drawable/ic_launcher_foreground" />\n'
        '    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />\n'
        "</adaptive-icon>\n"
    )
    for file in ("ic_launcher.xml", "ic_launcher_round.xml"):
        (RES / "mipmap-anydpi-v26" / file).write_text(adaptive)

    jobs = []
    for density, size in LEGACY_SIZES.items():
        jobs.append((svg(shapes, corner=0.18), RES / f"mipmap-{density}/ic_launcher.png", size))
        jobs.append((svg(shapes, corner=0.5), RES / f"mipmap-{density}/ic_launcher_round.png", size))

    # iOS: one 1024 px image, full bleed, no alpha (iOS applies its own mask).
    jobs.append((svg(shapes), IOS / "app-icon-1024.png", 1024))

    # Web: SVG favicon (rounded), 32 px PNG fallback, 180 px apple-touch-icon (full bleed).
    (WEB / "favicon.svg").write_text(svg(shapes, corner=0.22))
    jobs.append((svg(shapes, corner=0.22), WEB / "favicon-32.png", 32))
    jobs.append((svg(shapes), WEB / "apple-touch-icon.png", 180))

    render(jobs)
    opaque(IOS / "app-icon-1024.png")
    opaque(WEB / "apple-touch-icon.png")
    print(f"Exported concept {number} ({name}).")


def previews(out):
    out = Path(out)
    out.mkdir(parents=True, exist_ok=True)
    jobs = []
    for number, (_, shapes) in CONCEPTS.items():
        jobs.append((svg(shapes, corner=0.22), out / f"_c{number}-512.png", 512))
        jobs.append((svg(shapes, corner=0.22), out / f"_c{number}-48.png", 48))
        jobs.append((svg(shapes, view=FULL_VIEW), out / f"_c{number}-full.png", 432))
    render(jobs)

    sheet_bg = (40, 42, 54)
    for number in CONCEPTS:
        big, small = Image.open(out / f"_c{number}-512.png"), Image.open(out / f"_c{number}-48.png")
        sheet = Image.new("RGB", (512 + 48 + 96, 512 + 64), sheet_bg)
        sheet.paste(big, (32, 32), big)
        sheet.paste(small, (512 + 64, 32 + 232), small)
        sheet.save(out / f"concept-{number}.png")

    # Android adaptive preview of each concept: the visible 72/108 under circle and squircle masks.
    for number in CONCEPTS:
        full = Image.open(out / f"_c{number}-full.png").convert("RGB")
        visible = full.crop((72, 72, 360, 360))
        sheet = Image.new("RGB", (288 * 2 + 96, 288 + 64), sheet_bg)
        for i, mask in enumerate((circle_mask(288), squircle_mask(288))):
            sheet.paste(visible, (32 + i * (288 + 32), 32), mask)
        sheet.save(out / f"android-adaptive-{number}.png")

    for tmp in out.glob("_c*.png"):
        tmp.unlink()


if __name__ == "__main__":
    args = sys.argv[1:]
    if args[:1] == ["previews"]:
        previews(args[1])
    elif args[:1] == ["export"]:
        export(int(args[1]))
    else:
        export(CHOSEN)

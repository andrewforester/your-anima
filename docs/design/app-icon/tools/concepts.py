"""Icon concepts as plain geometry on the Android adaptive-icon grid.

Every concept is drawn in a 108x108 viewport centred on (54, 54). The artwork stays
inside the 66-unit safe-zone circle (radius 33). Flat exports (iOS, web, legacy
Android PNGs) crop the viewport to the visible 72x72 centre (18..90).

A shape is (kind, path_data, width): kind "stroke" (gradient line, round caps and
joins) or "fill" (gradient fill). All shapes share one gradient.
"""

import math

# Colours from ui/theme/Color.kt
BACKGROUND = "#0D0F2B"  # background
GRADIENT_START = "#B388FF"  # accentPurple (violet)
GRADIENT_END = "#26D0CE"  # accentTeal (mint)
# Gradient axis, top-left to bottom-right of the safe zone.
GRADIENT_FROM = (30.0, 30.0)
GRADIENT_TO = (78.0, 78.0)


def f(v):
    return f"{v:.2f}".rstrip("0").rstrip(".")


def sparkle(cx, cy, r, waist=0.18):
    """Four-point star: curved sides pinched towards the centre."""
    w = r * waist
    pts = [(cx, cy - r), (cx + r, cy), (cx, cy + r), (cx - r, cy)]
    ctrl = [(cx + w, cy - w), (cx + w, cy + w), (cx - w, cy + w), (cx - w, cy - w)]
    d = f"M{f(pts[0][0])},{f(pts[0][1])}"
    for i in range(4):
        c, p = ctrl[i], pts[(i + 1) % 4]
        d += f" Q{f(c[0])},{f(c[1])} {f(p[0])},{f(p[1])}"
    return d + " Z"


def circle(cx, cy, r):
    return (
        f"M{f(cx - r)},{f(cy)} A{f(r)},{f(r)} 0 1,0 {f(cx + r)},{f(cy)} "
        f"A{f(r)},{f(r)} 0 1,0 {f(cx - r)},{f(cy)} Z"
    )


def crescent(c1, r1, c2, r2):
    """Outline of circle 1 minus circle 2 (they must intersect)."""
    (x1, y1), (x2, y2) = c1, c2
    d = math.hypot(x2 - x1, y2 - y1)
    a = (r1 * r1 - r2 * r2 + d * d) / (2 * d)
    h = math.sqrt(r1 * r1 - a * a)
    mx, my = x1 + a * (x2 - x1) / d, y1 + a * (y2 - y1) / d
    ox, oy = -h * (y2 - y1) / d, h * (x2 - x1) / d
    p1, p2 = (mx + ox, my + oy), (mx - ox, my - oy)
    return (
        f"M{f(p1[0])},{f(p1[1])} A{f(r1)},{f(r1)} 0 1,1 {f(p2[0])},{f(p2[1])} "
        f"A{f(r2)},{f(r2)} 0 0,0 {f(p1[0])},{f(p1[1])} Z"
    )


def polyline(points):
    return "M" + " L".join(f"{f(x)},{f(y)}" for x, y in points)


# 1. Constellation: the Today tab star idea as a small star map.
_STARS = [(32, 64), (43, 48), (58, 54), (72, 38), (76, 70)]
CONSTELLATION = [
    ("stroke", polyline(_STARS[:4]), 2.6),
    ("stroke", polyline([_STARS[2], _STARS[4]]), 2.6),
    ("fill", circle(*_STARS[0], 3.4), 0),
    ("fill", circle(*_STARS[1], 3.0), 0),
    ("fill", circle(*_STARS[2], 3.4), 0),
    ("fill", circle(*_STARS[4], 3.0), 0),
    ("fill", sparkle(*_STARS[3], 10), 0),
]

# 2. Crescent moon and a sparkle: the hero moon of the home screen as a line mark.
MOON = [
    ("stroke", crescent((52, 57), 21, (63, 49), 17), 4.2),
    ("fill", sparkle(71, 35, 9), 0),
    ("fill", circle(80, 52, 2.4), 0),
]

# 3. "Anima" glyph: an A whose crossbar is a smile-shaped arc, a sparkle at the apex.
ANIMA = [
    ("stroke", polyline([(38, 80), (54, 40), (70, 80)]), 4.4),
    ("stroke", "M44,65 Q54,73 64,65", 4.0),
    ("fill", sparkle(54, 29, 7.5), 0),
]

CONCEPTS = {1: ("constellation", CONSTELLATION), 2: ("moon", MOON), 3: ("anima", ANIMA)}

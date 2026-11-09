"""Generates the launcher icon (adaptive vector + legacy PNGs) from the StreakFlame paths.

Usage (needs ImageMagick): python3 scripts/generate_launcher_icon.py android/app/src/main/res <tmp dir>
"""
import os, subprocess, sys
RES = sys.argv[1]
OUT = sys.argv[2]
P = {
 "left": "M21 30 C17 48 13 64 18 80 C21 89 29 95 42 95 C37 78 35 64 31 54 C27 46 22 40 21 30 Z",
 "right": "M80 22 C85 42 88 62 83 79 C80 88 71 95 58 95 C63 76 65 62 69 52 C73 42 79 34 80 22 Z",
 "main": "M51 2 C55 24 70 38 71 60 C72 80 64 98 50 98 C36 98 29 84 31 66 C33 46 48 30 51 2 Z",
 "body": "M50 40 C66 50 84 64 83 86 C82 106 67 117 50 117 C33 117 18 106 17 86 C16 64 34 50 50 40 Z",
 "innerTip": "M48 46 C52 60 62 70 64 84 C66 98 58 108 50 108 C42 108 35 100 36 88 C37 76 46 66 48 46 Z",
 "innerBody": "M50 74 C60 82 68 90 66 102 C64 110 58 113 50 113 C42 113 36 110 34 102 C32 90 40 82 50 74 Z",
 "core": "M50 82 C53 90 58 96 58 102 C58 108 54 111 50 111 C46 111 42 108 42 102 C42 96 47 90 50 82 Z",
}
OUTER = [("#FB923C", 0), ("#DC2626", 1)], (0, 117)
INNER = [("#F97316", 0), ("#FDE68A", 1)], (44, 113)
CORE = [("#00FFFFFF", 0), ("#D9FFFBEB", 0.6), ("#F2FFFFFF", 1)], (82, 111)
FILL = {"left": OUTER, "right": OUTER, "main": OUTER, "body": OUTER,
        "innerTip": INNER, "innerBody": INNER, "core": CORE}
ORDER = ["left", "right", "main", "body", "innerTip", "innerBody", "core"]
BG = "#161513"
GLOW = "#EF4444"
# Flame box 100x120 placed in the 108dp adaptive canvas, inside the 66dp safe zone.
SCALE, TX, TY = 0.5, 29, 24

def argb(c):
    c = c.lstrip("#")
    return "#" + (c if len(c) == 8 else "FF" + c)

def vd_gradient(stops, y):
    items = "".join(f'<item android:offset="{o}" android:color="{argb(c)}"/>' for c, o in stops)
    return (f'<aapt:attr name="android:fillColor"><gradient android:type="linear" android:startX="0" '
            f'android:startY="{y[0]}" android:endX="0" android:endY="{y[1]}">{items}</gradient></aapt:attr>')

def write(path, text):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    open(path, "w").write(text)

HEAD = ('<?xml version="1.0" encoding="utf-8"?>\n<!-- Generated from the StreakFlame paths (src/components/StreakFlame.tsx). -->\n'
        '<vector xmlns:android="http://schemas.android.com/apk/res/android" xmlns:aapt="http://schemas.android.com/aapt" '
        'android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">\n')

paths = "\n".join(f'    <path android:pathData="{P[k]}">{vd_gradient(*FILL[k])}</path>' for k in ORDER)
write(f"{RES}/drawable/ic_launcher_foreground.xml", HEAD +
      f'  <group android:translateX="{TX}" android:translateY="{TY}" android:scaleX="{SCALE}" android:scaleY="{SCALE}">\n{paths}\n  </group>\n</vector>\n')

write(f"{RES}/drawable/ic_launcher_background.xml", HEAD +
      f'  <path android:pathData="M0 0 H108 V108 H0 Z" android:fillColor="{argb(BG)}"/>\n'
      f'  <path android:pathData="M0 0 H108 V108 H0 Z"><aapt:attr name="android:fillColor"><gradient android:type="radial" '
      f'android:centerX="54" android:centerY="58" android:gradientRadius="40">'
      f'<item android:offset="0" android:color="#59{GLOW[1:]}"/><item android:offset="0.5" android:color="#1A{GLOW[1:]}"/>'
      f'<item android:offset="1" android:color="#00{GLOW[1:]}"/></gradient></aapt:attr></path>\n</vector>\n')

# Themed icons (Android 13+) tint a single-colour silhouette.
silhouette = "\n".join(f'    <path android:pathData="{P[k]}" android:fillColor="#FF000000"/>' for k in ["left", "right", "main", "body"])
write(f"{RES}/drawable/ic_launcher_monochrome.xml", HEAD +
      f'  <group android:translateX="{TX}" android:translateY="{TY}" android:scaleX="{SCALE}" android:scaleY="{SCALE}">\n{silhouette}\n  </group>\n</vector>\n')

write(f"{RES}/mipmap-anydpi-v26/ic_launcher.xml",
      '<?xml version="1.0" encoding="utf-8"?>\n<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
      '    <background android:drawable="@drawable/ic_launcher_background"/>\n'
      '    <foreground android:drawable="@drawable/ic_launcher_foreground"/>\n'
      '    <monochrome android:drawable="@drawable/ic_launcher_monochrome"/>\n</adaptive-icon>\n')

# Legacy PNGs for Android 7.x, rendered from the same artwork with rounded corners.
def svg_grad(i, stops, y):
    st = "".join(f'<stop offset="{o}" stop-color="#{argb(c)[3:]}" stop-opacity="{int(argb(c)[1:3],16)/255:.2f}"/>' for c, o in stops)
    return f'<linearGradient id="g{i}" gradientUnits="userSpaceOnUse" x1="0" y1="{y[0]}" x2="0" y2="{y[1]}">{st}</linearGradient>'
grads, shapes = [], []
for i, k in enumerate(ORDER):
    grads.append(svg_grad(i, *FILL[k]))
    shapes.append(f'<path d="{P[k]}" fill="url(#g{i})"/>')
svg = (f'<svg xmlns="http://www.w3.org/2000/svg" width="432" height="432" viewBox="9 9 90 90">'
       f'<defs>{"".join(grads)}<radialGradient id="glow" gradientUnits="userSpaceOnUse" cx="54" cy="58" r="40">'
       f'<stop offset="0" stop-color="{GLOW}" stop-opacity=".35"/><stop offset=".5" stop-color="{GLOW}" stop-opacity=".1"/>'
       f'<stop offset="1" stop-color="{GLOW}" stop-opacity="0"/></radialGradient>'
       f'<clipPath id="r"><rect x="9" y="9" width="90" height="90" rx="20"/></clipPath></defs>'
       f'<g clip-path="url(#r)"><rect x="0" y="0" width="108" height="108" fill="{BG}"/><rect x="0" y="0" width="108" height="108" fill="url(#glow)"/>'
       f'<g transform="translate({TX} {TY}) scale({SCALE})">{"".join(shapes)}</g></g></svg>')
open(f"{OUT}/icon.svg", "w").write(svg)
subprocess.run(["magick", "-background", "none", f"{OUT}/icon.svg", f"{OUT}/icon.png"], check=True)
for d, px in {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}.items():
    subprocess.run(["magick", f"{OUT}/icon.png", "-resize", f"{px}x{px}", f"{RES}/mipmap-{d}/ic_launcher.png"], check=True)
print("ok")

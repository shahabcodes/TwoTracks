"""
Draws the podcast covers: the thumbnail beside the episode in the
notification, the picture on the lock screen and the artwork in the player.

    python tools/podcast/art/cover.py            the AZ-204 cover
    python tools/podcast/art/cover.py german     the German A1 cover
    python tools/podcast/art/cover.py ai200      the AI-200 cover

writes app/src/main/res/drawable-nodpi/podcast_cover.png (or
podcast_cover_de.png). The fonts are the app's own, so the cover speaks the
same type as the screens around it.
"""
import math
import sys
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter, ImageFont

ROOT = Path(__file__).resolve().parents[3]
FONTS = ROOT / "core" / "ui" / "src" / "main" / "res" / "font"
DRAWABLES = ROOT / "app" / "src" / "main" / "res" / "drawable-nodpi"
SIZE = 1024   # drawn large, saved at 512: enough for a lock screen, small in the APK

SERIES = {
    # Azure blue, from deep navy at the top left to cyan at the bottom right.
    "az204": {
        "out": "podcast_cover.png", "title": "AZ-204", "sub": "P O D C A S T", "hosts": "M L", "credit": "TwoTracks · Maya & Leo",
        "stops": [(0.0, (8, 30, 82)), (0.55, (0, 103, 192)), (1.0, (58, 196, 240))], "glow": (120, 220, 255, 90), "ink": (8, 30, 82, 255),
    },
    # AZ-204's successor: the same Azure family, turned towards AI's violet, from deep indigo to teal.
    "ai200": {
        "out": "podcast_cover_ai.png", "title": "AI-200", "sub": "P O D C A S T", "hosts": "M L", "credit": "TwoTracks · Maya & Leo",
        "stops": [(0.0, (26, 16, 72)), (0.55, (92, 52, 200)), (1.0, (40, 200, 200))], "glow": (190, 160, 255, 90), "ink": (26, 16, 72, 255),
    },
    # The flag's colours, kept dark enough for white type: charcoal, a deep red, then gold.
    "german": {
        "out": "podcast_cover_de.png", "title": "DEUTSCH", "sub": "A 1   P O D C A S T", "hosts": "M L J", "credit": "TwoTracks · Maya, Leo & Jonas",
        "stops": [(0.0, (24, 24, 28)), (0.55, (150, 22, 24)), (1.0, (214, 160, 0))], "glow": (255, 190, 90, 70), "ink": (24, 24, 28, 255),
    },
}


def gradient(size, stops):
    img = Image.new("RGB", (size, size))
    px = img.load()
    for y in range(size):
        for x in range(size):
            t = (x * 0.45 + y * 0.55) / size
            for (a, ca), (b, cb) in zip(stops, stops[1:]):
                if a <= t <= b:
                    f = (t - a) / (b - a)
                    px[x, y] = tuple(int(ca[i] + (cb[i] - ca[i]) * f) for i in range(3))
                    break
    return img


def main():
    s = SERIES[sys.argv[1] if len(sys.argv) > 1 else "az204"]
    img = gradient(SIZE, s["stops"]).convert("RGBA")

    # A soft glow behind the title, so the type sits on light rather than on a flat field.
    glow = Image.new("RGBA", img.size, (0, 0, 0, 0))
    ImageDraw.Draw(glow).ellipse((180, 260, 980, 900), fill=s["glow"])
    img = Image.alpha_composite(img, glow.filter(ImageFilter.GaussianBlur(120)))
    d = ImageDraw.Draw(img)

    condensed = ImageFont.truetype(str(FONTS / "barlow_condensed_bold.ttf"), 300)
    semibold = ImageFont.truetype(str(FONTS / "barlow_semibold.ttf"), 54)
    mono = ImageFont.truetype(str(FONTS / "ibm_plex_mono_medium.ttf"), 40)

    # The hosts: a voice each, overlapping, top left.
    letters = s["hosts"].split()
    for i, letter in enumerate(letters):
        cx = 120 + i * 78
        fill = (255, 255, 255, 235 - i * 50)
        d.ellipse((cx - 52, 68, cx + 52, 172), fill=fill)
        tw = d.textlength(letter, font=semibold)
        d.text((cx - tw / 2, 86), letter, font=semibold, fill=s["ink"])

    # The title as large as fits the width.
    title_font = condensed
    while d.textlength(s["title"], font=title_font) > SIZE - 160:
        title_font = ImageFont.truetype(str(FONTS / "barlow_condensed_bold.ttf"), title_font.size - 10)
    d.text((80, 360 + (300 - title_font.size) // 2), s["title"], font=title_font, fill=(255, 255, 255, 255))
    d.text((88, 676), s["sub"], font=mono, fill=(255, 255, 255, 230))

    # A waveform along the bottom: the same few bars a player shows when it plays.
    base, w, gap = 900, 22, 14
    for i in range(int((SIZE - 160) / (w + gap)) + 1):
        h = 26 + 70 * abs(math.sin(i * 0.55)) * (0.55 + 0.45 * math.sin(i * 0.19 + 1.3) ** 2)
        x = 80 + i * (w + gap)
        d.rounded_rectangle((x, base - h, x + w, base), radius=w // 2, fill=(255, 255, 255, 120 if i % 3 else 200))

    d.text((80, 940), s["credit"], font=ImageFont.truetype(str(FONTS / "barlow_medium.ttf"), 40), fill=(255, 255, 255, 200))

    out = DRAWABLES / s["out"]
    out.parent.mkdir(parents=True, exist_ok=True)
    img.convert("RGB").resize((512, 512), Image.LANCZOS).save(out, optimize=True)
    print(f"wrote {out.relative_to(ROOT)} ({out.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()

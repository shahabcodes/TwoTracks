"""
Draws the AZ-204 podcast cover: the thumbnail beside the episode in the
notification, the picture on the lock screen and the artwork in the player.

    python tools/podcast/art/cover.py

writes app/src/main/res/drawable-nodpi/podcast_cover.png. The fonts are the
app's own, so the cover speaks the same type as the screens around it.
"""
import math
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter, ImageFont

ROOT = Path(__file__).resolve().parents[3]
FONTS = ROOT / "core" / "ui" / "src" / "main" / "res" / "font"
OUT = ROOT / "app" / "src" / "main" / "res" / "drawable-nodpi" / "podcast_cover.png"
SIZE = 1024   # drawn large, saved at 512: enough for a lock screen, small in the APK


def gradient(size):
    """Azure blue, from deep navy at the top left to cyan at the bottom right."""
    stops = [(0.0, (8, 30, 82)), (0.55, (0, 103, 192)), (1.0, (58, 196, 240))]
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
    img = gradient(SIZE).convert("RGBA")

    # A soft glow behind the title, so the type sits on light rather than on a flat field.
    glow = Image.new("RGBA", img.size, (0, 0, 0, 0))
    ImageDraw.Draw(glow).ellipse((180, 260, 980, 900), fill=(120, 220, 255, 90))
    img = Image.alpha_composite(img, glow.filter(ImageFilter.GaussianBlur(120)))
    d = ImageDraw.Draw(img)

    condensed = ImageFont.truetype(str(FONTS / "barlow_condensed_bold.ttf"), 300)
    semibold = ImageFont.truetype(str(FONTS / "barlow_semibold.ttf"), 54)
    mono = ImageFont.truetype(str(FONTS / "ibm_plex_mono_medium.ttf"), 40)

    # The two hosts: two voices, overlapping, top left.
    for i, (fill, letter) in enumerate([((255, 255, 255, 235), "M"), ((255, 255, 255, 140), "L")]):
        cx = 120 + i * 78
        d.ellipse((cx - 52, 68, cx + 52, 172), fill=fill)
        tw = d.textlength(letter, font=semibold)
        d.text((cx - tw / 2, 86), letter, font=semibold, fill=(8, 30, 82, 255))

    d.text((80, 360), "AZ-204", font=condensed, fill=(255, 255, 255, 255))
    d.text((88, 676), "P O D C A S T", font=mono, fill=(255, 255, 255, 230))

    # A waveform along the bottom: the same few bars a player shows when it plays.
    base, w, gap = 900, 22, 14
    for i in range(int((SIZE - 160) / (w + gap)) + 1):
        h = 26 + 70 * abs(math.sin(i * 0.55)) * (0.55 + 0.45 * math.sin(i * 0.19 + 1.3) ** 2)
        x = 80 + i * (w + gap)
        d.rounded_rectangle((x, base - h, x + w, base), radius=w // 2, fill=(255, 255, 255, 120 if i % 3 else 200))

    d.text((80, 940), "TwoTracks · Maya & Leo", font=ImageFont.truetype(str(FONTS / "barlow_medium.ttf"), 40), fill=(255, 255, 255, 200))

    OUT.parent.mkdir(parents=True, exist_ok=True)
    img.convert("RGB").resize((512, 512), Image.LANCZOS).save(OUT, optimize=True)
    print(f"wrote {OUT.relative_to(ROOT)} ({OUT.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()

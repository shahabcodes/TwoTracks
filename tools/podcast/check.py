"""
Flags lines in the rendered episodes that probably went wrong.

A speech model fails in a few recognisable ways: it skips words, slurs a
line into half its length, or keeps going past the end of the text into
mumbling or silence. Each of those shows up as a line whose length is far
from what its word count predicts. This compares every line's measured
duration (from out/<topic>.json) with its words (from podcasts.json) and
lists the ones outside the normal range, worst first, so a listener can go
straight to them instead of hearing two hours of audio.

    python check.py                 every rendered episode
    python check.py az-keyvault     one episode
"""

import json
import re
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
CUE = re.compile(r"\[([a-z ]+)\]")
PAUSE = {"pause": 0.55, "long pause": 1.1}

# Seconds per spoken word that count as normal for these voices. Short lines
# ("Go on.") carry a fixed overhead, so they are judged on a looser band.
NORMAL = (0.22, 0.62)
SHORT_LINE = 6

# Silence inside a line beyond its own written pauses, in seconds, that is
# still a natural breath rather than a stall.
SILENCE_SLACK = 0.9


def words(text: str) -> int:
    """Words as the voice says them: AcquireTokenSilent is three, as render.py splits it."""
    spoken = re.sub(r"(?<=[a-z])(?=[A-Z])", " ", CUE.sub(" ", text))
    return len([w for w in spoken.split() if w.strip()])


def pauses(text: str) -> float:
    return sum(PAUSE.get(c, 0.0) for c in CUE.findall(text))


def longest_silence(audio, rate: int, start: float, end: float) -> float:
    """The longest stretch inside [start, end] that is quieter than speech, in seconds."""
    import numpy as np
    clip = audio[int(start * rate):int(end * rate)]
    if len(clip) == 0:
        return 0.0
    frame = int(rate * 0.05)
    frames = len(clip) // frame
    if frames == 0:
        return 0.0
    energy = np.sqrt(np.mean(np.square(clip[: frames * frame].reshape(frames, frame)), axis=1))
    quiet = energy < max(float(np.percentile(energy, 90)) * 0.05, 1e-4)
    best = run = 0
    for q in quiet:
        run = run + 1 if q else 0
        best = max(best, run)
    return best * 0.05


def check(topic: str, scripts: dict) -> list:
    timing = json.loads((HERE / "out" / f"{topic}.json").read_text(encoding="utf-8"))
    lines = scripts[topic]
    wav = HERE / "out" / f"{topic}.wav"
    audio = rate_hz = None
    if wav.exists():
        import soundfile as sf
        audio, rate_hz = sf.read(wav, dtype="float32")
    flagged = []
    for t in timing["lines"]:
        text = lines[t["line"]]["text"]
        n = words(text)
        spoken = (t["end"] - t["start"]) - pauses(text)
        rate = spoken / max(n, 1)
        low, high = NORMAL
        if n <= SHORT_LINE:
            low, high = low * 0.6, high * 2.2
        if not (low <= rate <= high):
            how = "too fast: words may be skipped" if rate < low else "too slow: may run on, mumble or trail into silence"
            badness = (low / rate) if rate < low else (rate / high)
            flagged.append((badness, t["line"] + 1, t["start"], rate, how, text))
        elif audio is not None:
            # A line of normal length can still hold a dead gap where the voice
            # stalled. Written pauses are allowed for; anything well past the
            # longest of them is worth a listen.
            allowed = max([PAUSE.get(c, 0.0) for c in CUE.findall(text)] + [0.0]) + SILENCE_SLACK
            gap = longest_silence(audio, rate_hz, t["start"], t["end"])
            if gap > allowed:
                flagged.append((gap / allowed, t["line"] + 1, t["start"], rate, f"a {gap:.1f}s silence inside the line", text))
    return sorted(flagged, reverse=True)


def main():
    scripts = {e["topicId"]: e["lines"] for e in json.loads((HERE / "podcasts.json").read_text(encoding="utf-8"))}
    rendered = sorted(p.stem for p in (HERE / "out").glob("*.json") if p.stem in scripts)
    topics = [t for t in rendered if not sys.argv[1:] or t in sys.argv[1:]]
    total = 0
    for topic in topics:
        flagged = check(topic, scripts)
        total += len(flagged)
        print(f"{topic}: {len(flagged)} line(s) to listen to")
        for _, line, start, rate, how, text in flagged:
            print(f"   line {line:>2} at {int(start // 60)}:{int(start % 60):02d}  {rate:.2f}s/word  {how}")
            print(f"      \"{text[:90]}{'...' if len(text) > 90 else ''}\"")
    print(f"\n{len(topics)} episode(s) checked, {total} line(s) flagged.")


if __name__ == "__main__":
    main()

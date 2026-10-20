"""
Listens to rendered episodes with a speech recogniser, for the lines worth a
human ear.

check.py judges a line by its length; this one transcribes it. Run it after a
render, before install.sh. It uses Whisper (MIT) in the German environment,
tools/podcast/.venv-de, and needs the GPU to itself: beside Chatterbox or
Qwen on an 8 GB card it spills into system memory and crawls.

- Jonas's lines (German episodes): each spoken piece in cache/de is heard with
  Whisper's multilingual model in German and compared word for word.
- Every other line: cut from the episode's wav by its timing and heard with
  the English model. A «German» word inside a host's English is left out of
  the comparison, since an English transcription spells it as it likes.

A transcription is not a judge of a voice. Expect numbers written as digits
("vierzehn" heard as "14"), names spelt the German way ("Sanja"), a laugh
written as "haha", and the odd word misheard on a very short line; Whisper
also sometimes loops ("the, the, the...") on a line that is fine. What it does
catch is a skipped or garbled word, a voice that ran on, and German said with
the wrong letters. Listen to what it lists before re-rendering anything.

    tools/podcast/.venv-de/Scripts/python tools/podcast/hear.py                 every rendered episode
    tools/podcast/.venv-de/Scripts/python tools/podcast/hear.py de1-words       one or more episodes
    tools/podcast/.venv-de/Scripts/python tools/podcast/hear.py --limit 0.25    list lines past a quarter of words off
"""

import argparse
import json
import re
import sys
from pathlib import Path

import numpy as np
import soundfile as sf

HERE = Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
from render_de import key, spoken  # noqa: E402

OUT = HERE / "out"
CUE = re.compile(r"\[[a-z ]+\]")
GERMAN = re.compile(r"«[^»]+»")


# The German letter names Jonas spells with, as the single letters a
# transcription writes for them ("We, E, Be" is heard as "W-E-B").
LETTERS = {"a": "a", "be": "b", "ce": "c", "de": "d", "e": "e", "ef": "f", "ge": "g", "ha": "h", "i": "i", "jott": "j",
           "ka": "k", "el": "l", "em": "m", "en": "n", "o": "o", "pe": "p", "ku": "q", "er": "r", "es": "s", "te": "t",
           "u": "u", "vau": "v", "we": "w", "ix": "x", "ypsilon": "y", "zett": "z"}


def words(text: str) -> list:
    return re.findall(r"[a-zäöüß']+", text.lower())


def spelt(text: str) -> list:
    """German words, with a run of three or more letter names read as the letters it spells."""
    out, run = [], []
    for w in words(text) + [""]:
        if w in LETTERS:
            run.append(w)
            continue
        out += [LETTERS[r] for r in run] if len(run) >= 3 else run
        run = []
        if w:
            out.append(w)
    return out


def wer(ref: list, hyp: list) -> float:
    """Word error rate: edits to turn the heard words into the script's, per script word."""
    d = list(range(len(hyp) + 1))
    for i, r in enumerate(ref, 1):
        prev, d[0] = d[0], i
        for j, h in enumerate(hyp, 1):
            prev, d[j] = d[j], min(d[j] + 1, d[j - 1] + 1, prev + (r != h))
    return d[len(hyp)] / max(len(ref), 1)


def clip_16k(audio: np.ndarray, rate: int, start: float, end: float) -> np.ndarray:
    clip = audio[int(start * rate):int(end * rate)]
    return np.interp(np.arange(0, len(clip), rate / 16000), np.arange(len(clip)), clip).astype("float32")


def main():
    ap = argparse.ArgumentParser(description="Transcribe rendered episodes and list the lines worth a listen.")
    ap.add_argument("topics", nargs="*", help="topic ids; every rendered episode when none are given")
    ap.add_argument("--limit", type=float, default=0.25, help="list a line when more than this share of its words differ")
    args = ap.parse_args()

    import whisper

    episodes = json.loads((HERE / "podcasts.json").read_text(encoding="utf-8"))
    episodes = [e for e in episodes if (OUT / f"{e['topicId']}.json").exists() and (not args.topics or e["topicId"] in args.topics)]
    if not episodes:
        sys.exit("Nothing rendered to listen to.")
    english = whisper.load_model("small.en", device="cuda")
    german = whisper.load_model("medium", device="cuda") if any(l["host"] == "jonas" for e in episodes for l in e["lines"]) else None

    flagged, heard_lines = [], 0
    for e in episodes:
        timing = json.loads((OUT / f"{e['topicId']}.json").read_text(encoding="utf-8"))["lines"]
        audio, rate = sf.read(OUT / f"{e['topicId']}.wav", dtype="float32")
        for span in timing:
            line = e["lines"][span["line"]]
            if line["host"] == "jonas":
                piece = HERE / "cache" / "de" / f"{key(line['text'])}.wav"
                said = spoken(line["text"])
                heard = german.transcribe(str(piece), language="de", temperature=0)["text"].strip()
                off = wer(spelt(said), words(heard))
            else:
                said = CUE.sub(" ", line["text"])
                ref = words(GERMAN.sub(" ", said))
                if len(ref) < 4:
                    continue  # a word or two: too little for a transcription to judge
                heard = english.transcribe(clip_16k(audio, rate, span["start"], span["end"]), language="en", temperature=0,
                                           condition_on_previous_text=False)["text"].strip()
                got = set(words(heard))
                off = sum(1 for w in ref if w not in got) / len(ref)
            heard_lines += 1
            if off > args.limit:
                flagged.append((off, e["topicId"], span["line"], line["host"], said.strip(), heard))
        print(f"{e['topicId']}: heard", flush=True)

    for off, topic, n, host, said, heard in sorted(flagged, reverse=True):
        print(f"\n{topic} line {n} ({host}, {off:.0%} off)\n  script: {said}\n  heard:  {heard}")
    print(f"\n{len(episodes)} episode(s), {heard_lines} lines heard, {len(flagged)} to listen to.")


if __name__ == "__main__":
    main()

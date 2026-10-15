"""
Renders the AZ-204 podcast episodes to audio.

The scripts live in core:content (Az204Podcasts.kt) and reach this tool as
podcasts.json, written by `./gradlew :core:content:exportPodcasts`. Nothing
here keeps a copy of the words.

Two engines:
  chatterbox  Chatterbox Turbo. Reads each line in its host's voice, taken
              from a short sample clip, and makes the [laugh], [chuckle] and
              [sigh] cues as sounds. The default.
  kokoro      Kokoro alone. Steadier and quicker, but it cannot laugh: sound
              cues are dropped and only the pauses survive.

The hosts' sample clips are made once with Kokoro (Maya from a female voice,
Leo from a male one) and kept in cache/voices, so Chatterbox speaks in
voices that are free to use and the same on every render.

For each episode it writes out/<topic>.wav, out/<topic>.ogg (Opus, what
the app downloads) and out/<topic>.json (when each line starts and ends,
so the app can light up the line being spoken).

    python render.py                       every episode
    python render.py az-app-service        one episode
    python render.py az-keyvault --lines 6 the first six lines, to try a change quickly
    python render.py --engine kokoro       without Chatterbox
"""

import argparse
import json
import re
import sys
import time
import zlib
from pathlib import Path

import numpy as np
import soundfile as sf

HERE = Path(__file__).resolve().parent
SCRIPTS = HERE / "podcasts.json"
OUT = HERE / "out"
CACHE = HERE / "cache"
RATE = 24_000

# The Kokoro voices the hosts' samples are made from.
KOKORO_VOICE = {"maya": "af_heart", "leo": "am_michael"}

# A neutral passage for the sample clips: ten seconds or so of ordinary
# speech, with no cue sounds, so the clip carries a voice and not a mood.
SAMPLE_TEXT = (
    "Hello, and thanks for listening. In each episode we take one topic and talk it through, "
    "slowly enough to follow on a walk, with the parts that matter most for the exam."
)

# Silence for the pause cues, and the gap between one host and the other.
PAUSE = {"pause": 0.55, "long pause": 1.1}
TURN_GAP = 0.32

# What a synthesiser would read badly, written as it should sound. Kept here
# rather than in the scripts, so the transcript in the app reads as English.
SAY = [
    (r"\bAZ-204\b", "A Z two oh four"),
    (r"\bASP\.NET\b", "A S P dot net"),
    (r"\.NET\b", "dot net"),
    (r"\bRA-GRS\b", "R A, G R S"),
    (r"\bGZRS\b", "G Z R S"),
    (r"\bGRS\b", "G R S"),
    (r"\bZRS\b", "Z R S"),
    (r"\bLRS\b", "L R S"),
    (r"\bRBAC\b", "R-back"),
    (r"\bHSM\b", "H S M"),
    (r"\bHTTPS\b", "H T T P S"),
    (r"\bURL\b", "U R L"),
    (r"\bStorageV2\b", "Storage V two"),
    (r"\bDefaultAzureCredential\b", "Default Azure Credential"),
    (r"\baz login\b", "A Z login"),
    (r"\bx-ms-routing-name\b", "x, m s, routing name"),
    (r"\bS1\b", "S one"),
    (r"\bNCRONTAB\b", "N cron tab"),
    (r"\bAPIs\b", "A P I's"),
    (r"\bAPI\b", "A P I"),
    (r"\bJWT\b", "J W T"),
    (r"\bURI\b", "U R I"),
    (r"\bCORS\b", "cors"),
    (r"\bXML\b", "X M L"),
    (r"\bJSON\b", "jason"),
    (r"\bSLA\b", "S L A"),
    (r"\b429\b", "four two nine"),
    (r"\b403\b", "four oh three"),
    (r"\bACR\b", "A C R"),
    (r"\bACI\b", "A C I"),
    (r"\bAKS\b", "A K S"),
    (r"\bKEDA\b", "Keda"),
    (r"\bDapr\b", "Dapper"),
    (r"\bYAML\b", "yamel"),
    (r"\bGUID\b", "goo-id"),
    (r"\bDateTime\.UtcNow\b", "DateTime dot UTC now"),
    (r"\bDateTime\.Now\b", "DateTime dot now"),
]

SOUND_CUES = {"laugh", "chuckle", "sigh"}
CUE = re.compile(r"\[([a-z ]+)\]")


def say(text: str) -> str:
    for pattern, spoken in SAY:
        text = re.sub(pattern, spoken, text)
    return text


def segments(text: str, keep_sounds: bool):
    """A line as speech and silences: ("speak", words) and ("pause", seconds)."""
    out, buf = [], ""
    for part in re.split(r"(\[[a-z ]+\])", text):
        m = CUE.fullmatch(part)
        if m and m.group(1) in PAUSE:
            if buf.strip():
                out.append(("speak", buf.strip()))
            buf = ""
            out.append(("pause", PAUSE[m.group(1)]))
        elif m and m.group(1) in SOUND_CUES:
            if keep_sounds:
                buf += f" [{m.group(1)}] "
        else:
            buf += part
    if buf.strip():
        out.append(("speak", buf.strip()))
    return [(k, re.sub(r"\s+", " ", v) if k == "speak" else v) for k, v in out]


def silence(seconds: float) -> np.ndarray:
    return np.zeros(int(RATE * seconds), dtype=np.float32)


class Kokoro:
    def __init__(self):
        from kokoro import KPipeline
        self.pipe = KPipeline(lang_code="a")

    def speak(self, text: str, host: str) -> np.ndarray:
        parts = [np.asarray(audio, dtype=np.float32) for _, _, audio in self.pipe(text, voice=KOKORO_VOICE[host], speed=1.0)]
        return np.concatenate(parts) if parts else silence(0.1)


class Chatterbox:
    def __init__(self, samples: dict, half: bool = True):
        import torch
        from chatterbox.tts_turbo import ChatterboxTurboTTS
        self.torch = torch
        device = "cuda" if torch.cuda.is_available() else "cpu"
        if device == "cpu":
            print("No CUDA device: Chatterbox will run on the CPU, slowly.", file=sys.stderr)
        self.model = ChatterboxTurboTTS.from_pretrained(device=device)
        # At full precision the language model (1.9 GB) and the decoder (1 GB) fill
        # a 4 GB card, and Windows spills the overflow into system memory, which
        # made a line take minutes. Half precision halves the language model and
        # leaves it room; autocast keeps the arithmetic around it consistent.
        self.half = half and device == "cuda"
        if self.half:
            self.model.t3.half()
        # Each host's voice is prepared from their sample clip once, and kept:
        # handing generate() the clip instead would prepare it again every line.
        # Turbo takes its expression from the cue tags; it has no
        # exaggeration setting, and warns that it ignores one if given.
        self.voices = {}
        for host, path in samples.items():
            self.model.prepare_conditionals(str(path))
            self.voices[host] = self.model.conds

    def speak(self, text: str, host: str) -> np.ndarray:
        # A fixed seed per line, so a render is the same every time it is run.
        self.torch.manual_seed(zlib.crc32(f"{host}:{text}".encode()))
        self.model.conds = self.voices[host]
        if self.half:
            with self.torch.autocast(device_type="cuda", dtype=self.torch.float16):
                wav = self.model.generate(text)
        else:
            wav = self.model.generate(text)
        audio = wav.squeeze().detach().cpu().numpy().astype(np.float32)
        if self.model.sr != RATE:
            import librosa
            audio = librosa.resample(audio, orig_sr=self.model.sr, target_sr=RATE)
        return audio


def host_samples(kokoro: Kokoro) -> dict:
    """The hosts' sample clips, made once with Kokoro and kept."""
    folder = CACHE / "voices"
    folder.mkdir(parents=True, exist_ok=True)
    paths = {}
    for host in KOKORO_VOICE:
        path = folder / f"{host}-{KOKORO_VOICE[host]}.wav"
        if not path.exists():
            sf.write(path, kokoro.speak(SAMPLE_TEXT, host), RATE)
            print(f"Made {host}'s sample clip from Kokoro's {KOKORO_VOICE[host]}")
        paths[host] = path
    return paths


def level(audio: np.ndarray, target_rms_db: float = -19.0) -> np.ndarray:
    """One loudness for the whole episode, and never clipping."""
    rms = float(np.sqrt(np.mean(np.square(audio)))) or 1e-9
    audio = audio * (10 ** (target_rms_db / 20) / rms)
    peak = float(np.max(np.abs(audio))) or 1.0
    return audio / peak * 0.97 if peak > 0.97 else audio


def render(episode: dict, engine, keep_sounds: bool, limit: int | None):
    pieces, timeline, at = [], [], 0.0
    began = time.time()
    lines = episode["lines"][:limit] if limit else episode["lines"]
    for i, line in enumerate(lines):
        host, start = line["host"], at
        for kind, value in segments(line["text"], keep_sounds):
            clip = silence(value) if kind == "pause" else engine.speak(say(value), host)
            pieces.append(clip)
            at += len(clip) / RATE
        timeline.append({"line": i, "host": host, "start": round(start, 2), "end": round(at, 2)})
        print(f"  {i + 1:>2}/{len(lines)} {host:<4} {at:6.1f}s of audio, {time.time() - began:6.1f}s so far", flush=True)
        if i < len(lines) - 1:
            pieces.append(silence(TURN_GAP))
            at += TURN_GAP
    return level(np.concatenate(pieces)), timeline


def main():
    ap = argparse.ArgumentParser(description="Render the AZ-204 podcast episodes.")
    ap.add_argument("topics", nargs="*", help="topic ids; every episode when none are given")
    ap.add_argument("--engine", choices=["chatterbox", "kokoro"], default="chatterbox")
    ap.add_argument("--lines", type=int, help="render only the first N lines")
    ap.add_argument("--full-precision", action="store_true", help="keep Chatterbox at full precision (needs more than 4 GB of GPU memory)")
    args = ap.parse_args()

    if not SCRIPTS.exists():
        sys.exit("No podcasts.json: run ./gradlew :core:content:exportPodcasts first.")
    episodes = json.loads(SCRIPTS.read_text(encoding="utf-8"))
    if args.topics:
        episodes = [e for e in episodes if e["topicId"] in args.topics]
        missing = set(args.topics) - {e["topicId"] for e in episodes}
        if missing:
            sys.exit(f"No episode for {', '.join(sorted(missing))}")

    kokoro = Kokoro()
    engine = Chatterbox(host_samples(kokoro), half=not args.full_precision) if args.engine == "chatterbox" else kokoro
    OUT.mkdir(exist_ok=True)
    for e in episodes:
        name = e["topicId"] + ("" if not args.lines else f"-first{args.lines}") + ("" if args.engine == "chatterbox" else "-kokoro")
        print(f"{e['topicId']} with {args.engine}")
        audio, timeline = render(e, engine, keep_sounds=args.engine == "chatterbox", limit=args.lines)
        sf.write(OUT / f"{name}.wav", audio, RATE)
        sf.write(OUT / f"{name}.ogg", audio, RATE, format="OGG", subtype="OPUS")
        (OUT / f"{name}.json").write_text(json.dumps({"topicId": e["topicId"], "seconds": round(len(audio) / RATE, 2), "lines": timeline}, indent=1), encoding="utf-8")
        print(f"  {len(audio) / RATE / 60:.1f} min -> out/{name}.ogg")


if __name__ == "__main__":
    main()

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
    python render.py --half                half precision, for a 4 GB card

A German episode's Jonas lines are spoken first by render_de.py, in its own
environment; render.py then puts them in their places.
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

# The episodes ship inside the APK, so the Opus is kept small: libsndfile's
# 0.95 is about 16 kbps, half its default of 31. A transcription of an episode
# at 31, 20, 16 and 12.6 kbps changed no more words than its own run-to-run
# wobble; 16 leaves a margin for what a transcription cannot hear.
OPUS_LEVEL = 0.95

# The Kokoro voices the hosts' samples are made from. Nova and Ivy (Late
# Night) and Rafe (Nova's partner in Couples) were made from a slower, softer
# passage, read at 0.88 speed (Rafe 0.9) and given to Kokoro as phonemes
# (late_night_voices.py): Kokoro's English text front end needs spaCy, which
# Smart App Control refused to load on the PC they were made on. Keep their
# clips in cache/voices as they are.
KOKORO_VOICE = {"maya": "af_heart", "leo": "am_michael", "nova": "af_nicole", "ivy": "af_bella", "rafe": "am_onyx"}

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
# A German word or phrase inside Maya's or Leo's English, written «so».
GERMAN = re.compile(r"«([^»]+)»")


EXTRA_SAY = [
    (r"\bPKCE\b", "pixie"),
    (r"\bMSAL\b", "em-sal"),
    (r"\bTTL\b", "T T L"),
    (r"\bKQL\b", "K Q L"),
    (r"\bSAS\b", "sass"),
    (r"\bETag\b", "E-tag"),
    (r"\bOData\b", "O data"),
    (r"\bSQL\b", "sequel"),
    (r"\bSDKs?\b", "S D K"),
    (r"\bCLI\b", "C L I"),
    (r"\bTLS\b", "T L S"),
    (r"\bPDF\b", "P D F"),
    (r"\bAKS\b", "A K S"),
    # Added with the beginner rewrite, whose explanations name more of them.
    (r"\bAPIM\b", "A P I M"),
    (r"\bCDN\b", "C D N"),
    (r"\bCPU\b", "C P U"),
    (r"\bHTTP\b", "H T T P"),
    (r"\bIDs\b", "I Ds"),
    (r"\bID\b", "I D"),
    (r"\bIP\b", "I P"),
    (r"\bLRU\b", "L R U"),
    (r"\bPNG\b", "P N G"),
    (r"\bRUs\b", "R Us"),
    (r"\bRU\b", "R U"),
    (r"\bDB\b", "D B"),
    (r"\bTV\b", "T V"),
    (r"\bUI\b", "U I"),
    (r"\bC0\b", "C zero"),
    (r"\bZADD\b", "Z add"),
    (r"\bZREVRANGE\b", "Z rev range"),
    (r"\bCRON\b", "cron"),
    # Added with AI-200.
    (r"\bAI-200\b", "A I two hundred"),
    (r"\bAI\b", "A I"),
    (r"\bLLMs?\b", "L L M"),
    (r"\bGPT\b", "G P T"),
    (r"\bRAG\b", "rag"),
    (r"\bKNN\b", "K N N"),
    (r"\bHNSW\b", "H N S W"),
    (r"\bIVFFlat\b", "I V F flat"),
    (r"\bDiskANN\b", "disk A N N"),
    (r"\bpgvector\b", "P G vector"),
    (r"\bPgBouncer\b", "P G bouncer"),
    (r"\bPostgreSQL\b", "Postgres"),
    (r"\bNoSQL\b", "no sequel"),
    (r"\bFastAPI\b", "fast A P I"),
    (r"\bkubectl\b", "cube control"),
    (r"\bjsonb\b", "jason B"),
    (r"\bUUIDs?\b", "U U I D"),
    (r"\bOTel\b", "O tel"),
    (r"\bVMs?\b", "V M"),
]

# Identifiers such as AcquireTokenSilent or CreateItemAsync read as one long
# word; split at the capitals they are read as words. Run after SAY, so its
# own respellings (Default Azure Credential and the rest) come first.
CAMEL = re.compile(r"\b[A-Z][a-z]+(?:[A-Z][a-z]+)+\b")
AFTER_SPLIT = [(r"\bMsal\b", "em-sal"), (r"\bUi\b", "U I"), (r"\bUtc\b", "U T C")]


def say(text: str) -> str:
    for pattern, spoken in SAY + EXTRA_SAY:
        text = re.sub(pattern, spoken, text)
    text = CAMEL.sub(lambda m: re.sub(r"(?<!^)(?=[A-Z])", " ", m.group(0)), text)
    for pattern, spoken in AFTER_SPLIT:
        text = re.sub(pattern, spoken, text)
    return text


def segments(text: str, keep_sounds: bool):
    """A line as speech, silences and effects: ("speak", words), ("pause", seconds) and ("effect", name)."""
    out, buf = [], ""
    for part in re.split(r"(\[[a-z ]+\])", text):
        m = CUE.fullmatch(part)
        if m and m.group(1) in EFFECTS:
            if buf.strip():
                out.append(("speak", buf.strip()))
            buf = ""
            out.append(("effect", m.group(1)))
        elif m and m.group(1) in PAUSE:
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


def kiss() -> np.ndarray:
    """
    The Couples style's prize for a right answer: a lip smack, a sharp pop of
    band-passed noise with a quick wet decay, between short silences. Made
    here rather than kept as a recording, from a fixed seed, so it is the
    same in every episode and free to use.
    """
    rng = np.random.default_rng(7)
    n = int(RATE * 0.09)
    t = np.arange(n) / RATE
    noise = rng.standard_normal(n).astype(np.float32)
    band = np.convolve(noise, np.ones(3) / 3, mode="same") - np.convolve(noise, np.ones(14) / 14, mode="same")
    envelope = np.exp(-t * 55.0) * (1 - np.exp(-t * 900.0))
    pop = np.sin(2 * np.pi * 2600.0 * t) * np.exp(-t * 140.0) * 0.35
    smack = (band * 1.4 + pop) * envelope
    smack = (smack / (np.abs(smack).max() + 1e-9) * 0.55).astype(np.float32)
    return np.concatenate([silence(0.3), smack, silence(0.4)])


# Cues that are sounds put into the audio, not spoken: the name and how to make it.
EFFECTS = {"kiss": kiss}


class Kokoro:
    def __init__(self):
        from kokoro import KPipeline
        self.pipe = KPipeline(lang_code="a")

    def speak(self, text: str, host: str) -> np.ndarray:
        parts = [np.asarray(audio, dtype=np.float32) for _, _, audio in self.pipe(text, voice=KOKORO_VOICE[host], speed=1.0)]
        return np.concatenate(parts) if parts else silence(0.1)


class Chatterbox:
    def __init__(self, samples: dict, half: bool = False):
        import torch
        from chatterbox.tts_turbo import ChatterboxTurboTTS
        self.torch = torch
        device = "cuda" if torch.cuda.is_available() else "cpu"
        if device == "cpu":
            print("No CUDA device: Chatterbox will run on the CPU, slowly.", file=sys.stderr)
        self.model = ChatterboxTurboTTS.from_pretrained(device=device)
        # Full precision is the default: on an 8 GB card it peaks near 3.6 GB and
        # renders about 2.7 times faster than real time, a little faster than
        # half precision there. On a 4 GB card the language model (1.9 GB) and
        # the decoder (1 GB) fill it, Windows spills the overflow into system
        # memory, and a line takes minutes; --half halves the language model and
        # leaves it room, and autocast keeps the arithmetic around it consistent.
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

    def speak_de(self, text: str, host: str) -> np.ndarray:
        """A «German» word inside a host's English, as render_de.py left it in the cache."""
        from render_de import host_path
        path = host_path(text, host)
        if not path.exists():
            sys.exit(f"{host} has not said \"{text}\" in German yet: run render_de.py first (see README).")
        audio, sr = sf.read(path, dtype="float32")
        if sr != RATE:
            import librosa
            audio = librosa.resample(audio, orig_sr=sr, target_sr=RATE)
        return audio

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


class Jonas:
    """
    The German episodes' native speaker. His lines are spoken ahead of time by
    render_de.py (Qwen3-TTS, in its own environment) into cache/de, and read
    from there. A line marked [repeat] is a drill: slowly first, then room to
    say it yourself, then at normal speed.
    """

    SLOW = 0.8          # the first reading, at four fifths of normal speed
    ROOM = 1.3          # the gap to repeat in, against the length of the phrase
    ROOM_EXTRA = 0.6    # plus a moment to start

    def speak(self, text: str) -> np.ndarray:
        import librosa
        from render_de import key
        path = CACHE / "de" / f"{key(text)}.wav"
        if not path.exists():
            sys.exit(f"Jonas has not said \"{text[:60]}\" yet: run render_de.py first (see README).")
        audio, sr = sf.read(path, dtype="float32")
        if sr != RATE:
            audio = librosa.resample(audio, orig_sr=sr, target_sr=RATE)
        if "[repeat]" not in text:
            return audio
        # The phase vocoder directly, as librosa.effects.time_stretch does; the
        # effects module pulls in scikit-learn, whose unsigned modules Windows'
        # Smart App Control sometimes refuses to load.
        spectrum = librosa.stft(audio)
        slow = librosa.istft(librosa.phase_vocoder(spectrum, rate=self.SLOW), length=int(round(len(audio) / self.SLOW)))
        return np.concatenate([slow, silence(len(audio) / RATE * self.ROOM + self.ROOM_EXTRA), audio])


def host_samples(kokoro=None) -> dict:
    """
    The hosts' sample clips, made once with Kokoro and kept. Kokoro is only
    started when a clip is missing: it pulls in spaCy, whose unsigned modules
    Windows' Smart App Control sometimes refuses to load.
    """
    folder = CACHE / "voices"
    folder.mkdir(parents=True, exist_ok=True)
    paths = {}
    for host in KOKORO_VOICE:
        path = folder / f"{host}-{KOKORO_VOICE[host]}.wav"
        if not path.exists():
            kokoro = kokoro or Kokoro()
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


def render(episode: dict, engine, keep_sounds: bool, limit: int | None, jonas: Jonas | None = None):
    pieces, timeline, at = [], [], 0.0
    began = time.time()
    lines = episode["lines"][:limit] if limit else episode["lines"]
    for i, line in enumerate(lines):
        host, start = line["host"], at
        if host == "jonas":
            # German, from the German pass; the English respellings do not apply.
            clip = jonas.speak(line["text"])
            pieces.append(clip)
            at += len(clip) / RATE
        for kind, value in ([] if host == "jonas" else segments(line["text"], keep_sounds)):
            if kind == "pause":
                clip = silence(value)
            elif kind == "effect":
                clip = EFFECTS[value]()
            else:
                # A «German» word inside English is said with German sounds in the
                # host's own voice; the English around it as always.
                parts = [engine.speak_de(p, host) if i % 2 else engine.speak(say(p), host)
                         for i, p in enumerate(GERMAN.split(value)) if p.strip(" ,")]
                clip = np.concatenate(parts) if parts else silence(0.1)
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
    ap.add_argument("--half", action="store_true", help="run Chatterbox's language model in half precision, for a 4 GB card")
    args = ap.parse_args()

    if not SCRIPTS.exists():
        sys.exit("No podcasts.json: run ./gradlew :core:content:exportPodcasts first.")
    episodes = json.loads(SCRIPTS.read_text(encoding="utf-8"))
    if args.topics:
        episodes = [e for e in episodes if e["topicId"] in args.topics]
        missing = set(args.topics) - {e["topicId"] for e in episodes}
        if missing:
            sys.exit(f"No episode for {', '.join(sorted(missing))}")

    engine = Chatterbox(host_samples(), half=args.half) if args.engine == "chatterbox" else Kokoro()
    OUT.mkdir(exist_ok=True)
    for e in episodes:
        name = e["topicId"] + ("" if not args.lines else f"-first{args.lines}") + ("" if args.engine == "chatterbox" else "-kokoro")
        print(f"{e['topicId']} with {args.engine}")
        audio, timeline = render(e, engine, keep_sounds=args.engine == "chatterbox", limit=args.lines, jonas=Jonas())
        sf.write(OUT / f"{name}.wav", audio, RATE)
        sf.write(OUT / f"{name}.ogg", audio, RATE, format="OGG", subtype="OPUS", compression_level=OPUS_LEVEL)
        (OUT / f"{name}.json").write_text(json.dumps({"topicId": e["topicId"], "seconds": round(len(audio) / RATE, 2), "lines": timeline}, indent=1), encoding="utf-8")
        print(f"  {len(audio) / RATE / 60:.1f} min -> out/{name}.ogg")


if __name__ == "__main__":
    main()

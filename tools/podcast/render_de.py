"""
Speaks the German in the German episodes, ahead of render.py.

Two kinds of German, both with Qwen3-TTS (Apache 2.0), which needs a newer
torch than Chatterbox and so runs in its own environment, tools/podcast/.venv-de:

- Jonas's lines. He is the native speaker, and says every German sentence a
  learner copies. Written once to cache/de/<hash>.wav.
- A «German» word inside Maya's or Leo's English ("it goes with «Sie»"),
  said in that host's own voice but with German sounds, so "Guten Abend" is
  not read as English. Written once to cache/de-hosts/<host>-<hash>.wav.

render.py then reads both where they fall, and does the rest: the slow
first reading and the gap to repeat in for a line marked [repeat], the
hand-overs, the level, the timing file.

Jonas's voice is a clip made once with Qwen3-TTS's voice design, kept in
cache/voices/jonas.wav beside the words it says (jonas.txt), so every render
speaks in the same voice, as Maya's and Leo's clips do for them.

Each piece is held to its length: a few words that come back several
seconds long have run on into invented speech, and are tried again with a
different seed, then reported.

    tools/podcast/.venv-de/Scripts/python tools/podcast/render_de.py              everything not yet spoken
    tools/podcast/.venv-de/Scripts/python tools/podcast/render_de.py de1-words    one episode's
"""

import hashlib
import json
import re
import sys
import zlib
from pathlib import Path

HERE = Path(__file__).resolve().parent
SCRIPTS = HERE / "podcasts.json"
CACHE = HERE / "cache"
VOICES = CACHE / "voices"
CUE = re.compile(r"\[([a-z ]+)\]")
GERMAN = re.compile(r"«([^»]+)»")

# The English the hosts' sample clips say (render.py's SAMPLE_TEXT).
HOST_SAMPLE_TEXT = (
    "Hello, and thanks for listening. In each episode we take one topic and talk it through, "
    "slowly enough to follow on a walk, with the parts that matter most for the exam."
)
HOST_CLIPS = {"maya": VOICES / "maya-af_heart.wav", "leo": VOICES / "leo-am_michael.wav"}

# Seconds a piece may run for each character of its text, plus a little to
# start and stop: generous for slow, careful German, far short of a run-on.
SECONDS_PER_CHAR = 0.16
SECONDS_EXTRA = 1.2

# Pieces spoken together in one pass on the GPU.
BATCH = 8

# Qwen3-TTS-12Hz makes twelve audio tokens a second.
TOKENS_PER_SECOND = 12


def tokens_for(seconds: float) -> int:
    """The most tokens a piece may take: its time limit, and a little over, so the length check still sees a run-on."""
    return int(seconds * TOKENS_PER_SECOND * 1.25) + 12


def spoken(text: str) -> str:
    """The German as said: cues out, spaces tidied."""
    return re.sub(r"\s+", " ", CUE.sub(" ", text)).strip()


def key(text: str) -> str:
    """The cache name for a Jonas line: the same words are only ever spoken once."""
    return hashlib.sha1(spoken(text).encode("utf-8")).hexdigest()[:16]


def host_path(text: str, host: str) -> Path:
    """Where a host's «German» word is kept (render.py reads the same name)."""
    return CACHE / "de-hosts" / f"{host}-{hashlib.sha1(text.encode('utf-8')).hexdigest()[:16]}.wav"


def main():
    episodes = json.loads(SCRIPTS.read_text(encoding="utf-8"))
    if sys.argv[1:]:
        episodes = [e for e in episodes if e["topicId"] in sys.argv[1:]]
    todo = []  # (path, voice, text)
    for e in episodes:
        for l in e["lines"]:
            if l["host"] == "jonas":
                todo.append((CACHE / "de" / f"{key(l['text'])}.wav", "jonas", spoken(l["text"])))
            else:
                todo += [(host_path(w, l["host"]), l["host"], w) for w in GERMAN.findall(l["text"])]
    todo = list(dict.fromkeys(t for t in todo if not t[0].exists()))
    if not todo:
        print("All the German is already spoken.")
        return
    if not (VOICES / "jonas.wav").exists():
        sys.exit(f"No voice clip at {VOICES / 'jonas.wav'}: Jonas's voice is made once with Qwen3-TTS voice design (see README).")

    import soundfile as sf
    import torch
    from qwen_tts import Qwen3TTSModel

    model = Qwen3TTSModel.from_pretrained("Qwen/Qwen3-TTS-12Hz-1.7B-Base", device_map="cuda:0",
                                          dtype=torch.bfloat16, attn_implementation="sdpa")
    prompts = {"jonas": model.create_voice_clone_prompt(ref_audio=str(VOICES / "jonas.wav"),
                                                        ref_text=(VOICES / "jonas.txt").read_text(encoding="utf-8").strip(),
                                                        x_vector_only_mode=False)}
    for host, clip in HOST_CLIPS.items():
        prompts[host] = model.create_voice_clone_prompt(ref_audio=str(clip), ref_text=HOST_SAMPLE_TEXT, x_vector_only_mode=False)

    def limit(text):
        return len(text) * SECONDS_PER_CHAR + SECONDS_EXTRA

    ran_on, done = [], 0
    # Spoken in batches of one voice, which the GPU does several times faster
    # than one at a time; a piece that runs on is tried again on its own.
    by_voice = {}
    for t in todo:
        by_voice.setdefault(t[1], []).append(t)
    for voice, items in by_voice.items():
        for start in range(0, len(items), BATCH):
            batch = items[start:start + BATCH]
            texts = [text for _, _, text in batch]
            torch.manual_seed(zlib.crc32(("|".join(texts)).encode("utf-8")))
            # A hard stop at the longest piece's limit: one piece running on
            # would otherwise hold the whole batch, and fill the card, for hours.
            wavs, sr = model.generate_voice_clone(text=texts, language=["German"] * len(texts), voice_clone_prompt=prompts[voice],
                                                  max_new_tokens=tokens_for(max(limit(t) for t in texts)))
            for (path, _, text), wav in zip(batch, wavs):
                seconds = len(wav) / sr
                attempt = 0
                while seconds > limit(text) and attempt < 3:
                    # A fixed seed per piece and attempt, so a render is the same every time it is run.
                    torch.manual_seed(zlib.crc32(f"{voice}:{text}:{attempt}".encode("utf-8")))
                    again, sr = model.generate_voice_clone(text=text, language="German", voice_clone_prompt=prompts[voice],
                                                           max_new_tokens=tokens_for(limit(text)))
                    wav, seconds, attempt = again[0], len(again[0]) / sr, attempt + 1
                if seconds > limit(text):
                    ran_on.append(f"{voice}: \"{text}\" ({seconds:.1f}s, limit {limit(text):.1f}s)")
                path.parent.mkdir(parents=True, exist_ok=True)
                sf.write(path, wav, sr)
                done += 1
                print(f"  {done:>4}/{len(todo)}  {voice:<5} {seconds:4.1f}s  {text[:60]}", flush=True)
    if ran_on:
        print("\nStill too long after three tries; listen to these:\n  " + "\n  ".join(ran_on))


if __name__ == "__main__":
    main()

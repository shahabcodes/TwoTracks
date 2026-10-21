"""Voice candidates for a 'Late Night' podcast style: two female hosts, soft and slow.

Makes a Kokoro sample clip per candidate voice (read slowly, in a soft register),
then has Chatterbox Turbo perform the same short exchange with each pairing.
Run with tools/podcast/.venv's python from D:/PA/tools/podcast.
"""
import sys
from pathlib import Path

import numpy as np
import soundfile as sf

sys.path.insert(0, str(Path.cwd()))
import render  # noqa: E402  (the repo's render.py: Chatterbox, silence, level, RATE)

OUT = Path(sys.argv[1])
OUT.mkdir(parents=True, exist_ok=True)

SAMPLE = (
    "Hey. It's late, and everything's quiet now. Get comfortable, take a slow breath, "
    "and stay with me a while. We've got all night, and I'm in no hurry at all."
)

VOICES = ["af_nicole", "af_bella", "af_sky", "bf_emma", "bf_isabella"]

PAIRS = {
    "1-nicole-bella": ("af_nicole", "af_bella"),
    "2-bella-emma": ("af_bella", "bf_emma"),
    "3-sky-isabella": ("af_sky", "bf_isabella"),
}

LINES = [
    "Hey, you. It's late, the city's gone quiet, and it's just us now. Tonight, let's talk about keeping secrets.",
    "Mm. My favourite kind of conversation. So where do we keep them?",
    "Somewhere safe. A vault in the cloud, where only the ones you really trust ever get a key.",
    "[chuckle] And how does an app prove it's one of the trusted ones? No password?",
    "No password at all. Azure gives it an identity of its own, quietly, behind the scenes. [pause] Just between the two of them.",
]

# Kokoro's English text front end pulls in spaCy, which Smart App Control
# blocks here, so the model is given the sample's phonemes directly.
PHONEMES = (
    "hˈA. ɪts lˈAt, ænd ˈɛvɹiθˌɪŋz kwˈIət nˈW. ɡɛt kˈʌmftəɹbᵊl, tˈAk ə slˈO bɹˈɛθ, "
    "ænd stˈA wɪð mˌi ə wˈIl. wiv ɡˈɑt ˈɔl nˈIt, ænd ˌIm ɪn nˈO hˈɜɹi æt ˈɔl."
)

import torch  # noqa: E402
from huggingface_hub import hf_hub_download  # noqa: E402
from kokoro import KModel  # noqa: E402

kmodel = KModel(repo_id="hexgrad/Kokoro-82M").eval()
clips = {}
for v in VOICES:
    pack = torch.load(hf_hub_download("hexgrad/Kokoro-82M", f"voices/{v}.pt"), weights_only=True)
    with torch.no_grad():
        audio = kmodel(PHONEMES, pack[len(PHONEMES) - 1], speed=0.88).cpu().numpy().astype(np.float32)
    path = OUT / f"clip-{v}.wav"
    sf.write(path, audio, render.RATE)
    clips[v] = path
    print("clip", v)
del kmodel

cb = render.Chatterbox({v: clips[v] for v in VOICES})
for name, (a, b) in PAIRS.items():
    parts = []
    for i, line in enumerate(LINES):
        host = a if i % 2 == 0 else b
        for kind, value in render.segments(line, keep_sounds=True):
            parts.append(render.silence(value) if kind == "pause" else cb.speak(render.say(value), host))
        parts.append(render.silence(0.45))
    audio = render.level(np.concatenate(parts))
    sf.write(OUT / f"late-night-{name}.wav", audio, render.RATE)
    print("pair", name)

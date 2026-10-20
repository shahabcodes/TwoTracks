"""
Makes Jonas's voice clip: cache/voices/jonas.wav and the words it says, jonas.txt.

Jonas has no recording to clone. His voice was designed once from a
description with Qwen3-TTS's VoiceDesign model, and render_de.py clones every
German line from that clip, so the clip is his voice. The description and
seed below are the ones the 47 episodes were rendered with ("teacher", chosen
from three in a listening trial), so a new machine makes the same Jonas.

Copy cache/voices/jonas.wav across with Maya's and Leo's clips when moving
machines rather than making it again: a different GPU or library version can
shift a designed voice a little, and the episodes already in the app were
spoken in this one.

    tools/podcast/.venv-de/Scripts/python tools/podcast/design_jonas.py
"""

import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
VOICES = HERE / "cache" / "voices"

DESCRIPTION = (
    "A calm, friendly German man in his mid thirties. Standard High German, clear and warm, "
    "speaking slowly like a patient language teacher."
)
# Ordinary, unhurried speech with no mood, so the clip carries a voice and not a feeling.
SAMPLE = (
    "Hallo, ich bin Jonas. In jeder Folge lernen wir ein paar neue Wörter und Sätze, "
    "ganz in Ruhe, und Sie sprechen einfach mit."
)
SEED = 7


def main():
    out = VOICES / "jonas.wav"
    if out.exists() and "--again" not in sys.argv:
        sys.exit(f"{out} exists. Jonas is already here; pass --again to make him anew (every German line would then need speaking again).")
    import soundfile as sf
    import torch
    from qwen_tts import Qwen3TTSModel

    model = Qwen3TTSModel.from_pretrained("Qwen/Qwen3-TTS-12Hz-1.7B-VoiceDesign", device_map="cuda:0",
                                          dtype=torch.bfloat16, attn_implementation="sdpa")
    torch.manual_seed(SEED)
    wavs, sr = model.generate_voice_design(text=SAMPLE, language="German", instruct=DESCRIPTION)
    VOICES.mkdir(parents=True, exist_ok=True)
    sf.write(out, wavs[0], sr)
    out.with_suffix(".txt").write_text(SAMPLE, encoding="utf-8")
    print(f"wrote {out} and jonas.txt")


if __name__ == "__main__":
    main()

# Podcast render tool

Turns the episode scripts into audio: 22 AZ-204 episodes (and each again as
Case Files and Gossip) and 47 German A1
episodes, one a topic. The scripts are course content
(`core/content/.../Az204Podcasts.kt` and `GermanPodcasts.kt`, held to shape by
`PodcastTest` and `GermanPodcastTest`); this tool only reads them.
[WRITING.md](WRITING.md) is how an episode is written.

The voices:

- **Maya** explains and **Leo** asks, in English. Their voices come from short
  sample clips made once with [Kokoro](https://github.com/hexgrad/kokoro)
  (Apache 2.0), and [Chatterbox Turbo](https://huggingface.co/ResembleAI/chatterbox-turbo)
  (MIT) reads each line in its host's voice, with the `[laugh]`, `[chuckle]`
  and `[sigh]` cues as real sounds. `[pause]` and `[long pause]` become
  silence.
- **Jonas**, in the German episodes, says every German sentence, with
  [Qwen3-TTS](https://github.com/QwenLM/Qwen3-TTS) (Apache 2.0). His voice was
  designed once from a description. A line of his ending in `[repeat]` is a
  drill: slowly (the same audio at four fifths speed), then room to say it out
  loud, then at normal speed.
- A «German» word inside Maya's or Leo's English is said by Qwen3-TTS in their
  own voice, so it has German sounds; Chatterbox Turbo is English only, and
  Chatterbox Multilingual ran on into invented speech when given a single word.

Qwen3-TTS needs a newer torch than Chatterbox, so there are two environments,
and a German episode is rendered in two passes: `render_de.py` speaks all the
German first and caches it, then `render.py` does the English and puts every
piece in place.

## Set up once (Windows)

Use Python 3.11 from python.org (`py install 3.11`), not a Python another tool
downloads: Smart App Control refuses unsigned interpreters.

**The English environment**, `.venv`, for Chatterbox:

```
py -3.11 -m venv tools/podcast/.venv
tools/podcast/.venv/Scripts/python -m pip install torch==2.6.0 torchaudio==2.6.0 --index-url https://download.pytorch.org/whl/cu124
tools/podcast/.venv/Scripts/python -m pip install -r tools/podcast/requirements.txt
```

On an RTX 50-series card (Blackwell, sm_120) torch 2.6.0 has no kernels and
fails with "no kernel image is available". After the two installs above, put
the first CUDA 12.8 build in its place; pip warns that chatterbox-tts pins
2.6.0, and it renders fine:

```
tools/podcast/.venv/Scripts/python -m pip install torch==2.7.1 torchaudio==2.7.1 --index-url https://download.pytorch.org/whl/cu128
```

**The German environment**, `.venv-de`, for Qwen3-TTS and for `hear.py`'s
Whisper (the order is explained in [requirements-de.txt](requirements-de.txt)):

```
py -3.11 -m venv tools/podcast/.venv-de
tools/podcast/.venv-de/Scripts/python -m pip install torch==2.9.1 torchaudio==2.9.1 --index-url https://download.pytorch.org/whl/cu128
tools/podcast/.venv-de/Scripts/python -m pip install -r tools/podcast/requirements-de.txt
tools/podcast/.venv-de/Scripts/python -m pip install --force-reinstall --no-deps torch==2.9.1 torchaudio==2.9.1 --index-url https://download.pytorch.org/whl/cu128
```

**The voices.** Maya's and Leo's clips are made by `render.py` the first time
it runs. Jonas's is made by `design_jonas.py` (in `.venv-de`); see its
docstring for the description and seed the episodes used. When moving
machines, copy `cache/voices/` across rather than making them again: the
episodes in the app were spoken in those exact clips.

The models download on first run (Chatterbox about 1–2 GB, Kokoro about
300 MB, Qwen3-TTS about 4 GB, Whisper about 2 GB). Chatterbox runs at full
precision, which wants more than 4 GB of GPU memory; on a 4 GB card add
`--half`. Without a CUDA GPU it runs on the CPU, slowly.

## Render

```
./gradlew :core:content:exportPodcasts
tools/podcast/.venv-de/Scripts/python tools/podcast/render_de.py      German episodes only
tools/podcast/.venv/Scripts/python tools/podcast/render.py
```

`render_de.py` speaks every German piece not yet in `cache/de` (Jonas) and
`cache/de-hosts` (the hosts' German words), eight at a time, each held to a
length its text allows: a piece that runs on is tried again on its own, and
listed if it still does. On an RTX 5060 the 47 German episodes took about 40
minutes, and `render.py` about an hour more to put them together.

`render.py az-keyvault --lines 6` renders the first six lines of one
episode, for trying a change quickly. `--engine kokoro` renders without
Chatterbox: steadier, but no laughs. Name topics to render only those.

Each episode lands in `tools/podcast/out/` as a `.wav`, the `.ogg` (Opus, at
about 16 kbps, which keeps all 69 to about 51 MB in the APK) the app plays,
and a `.json` of when each line starts and ends, which the app uses to light
up the line being spoken and to move the board. `out/`, `cache/` and the
environments are not committed.

Words a synthesiser reads badly ("AZ-204", "RA-GRS", ".NET") are respelt in
`SAY` in `render.py`, not in the scripts, so the transcript stays English. The
respellings do not touch German.

One model on the GPU at a time: on an 8 GB card, Chatterbox or Qwen3-TTS beside
Whisper, or beside each other, spills into system memory and crawls.

## Check before listening

```
tools/podcast/.venv/Scripts/python tools/podcast/check.py
tools/podcast/.venv-de/Scripts/python tools/podcast/hear.py
```

`check.py` lists the English lines whose length is far from what their word
count predicts: too fast usually means skipped words, too slow means the voice
ran on, mumbled or trailed into silence. Healthy lines sit at about 0.23 to 0.5
seconds a word. A host's line with a «German» word in it is put together from
pieces, and often shows as a little slow; that is the joins, not a fault.
Jonas's lines are paced on purpose and are skipped.

`hear.py` transcribes every line with Whisper and lists those whose words
differ from the script: Jonas's German against the German, the hosts' English
against the English. Its docstring says what it gets wrong (numbers as
digits, names spelt the German way, the odd short line). Listen to what either
lists first; render the episode again if one is wrong (each line has its own
fixed seed, so a change elsewhere in the script changes nothing else).

## Put them in the app

```
tools/podcast/install.sh                         every AZ-204 episode
tools/podcast/install.sh de1-words de1-grammar   any episodes, by topic
```

Copies each finished episode's `.ogg` and `.json` into
`app/src/main/assets/podcasts`. The episodes ship inside the APK, so they
play offline from the first time and there is nothing hosted anywhere; a
corrected render reaches the phone with the next build. `EpisodeAssetsTest`
checks that every AZ-204 and German topic has its episode there and that each
timing matches its script line for line, so a stale render after a script
edit fails the build rather than lighting the wrong line in the transcript.

The covers the player, the notification and the lock screen show are drawn by
`art/cover.py` (`art/cover.py german` for the German one), into
`app/src/main/res/drawable-nodpi/`.

## Smart App Control

Windows' Smart App Control refuses unsigned files it has no good reputation
for, and that changes from day to day. It has refused, at one time or
another, interpreters that uv downloads, spaCy's and scikit-learn's compiled
modules, and torchaudio 2.9.1's `libtorchaudio.pyd`, which Qwen3-TTS needs.
`render.py` avoids spaCy (Kokoro starts only to make a missing voice clip) and
scikit-learn (the slow drill uses librosa's phase vocoder directly). If a run
stops on "An Application Control policy has blocked this file", the Code
Integrity log in Event Viewer names the file; a retry later sometimes works.
Leave the setting itself alone.

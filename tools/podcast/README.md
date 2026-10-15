# Podcast render tool

Turns the AZ-204 episode scripts into audio. The scripts are course content
(`core/content/.../Az204Podcasts.kt`, held to shape by `PodcastTest`); this
tool only reads them.

Two fictional hosts: **Maya** explains, **Leo** asks. Their voices come from
short sample clips made once with [Kokoro](https://github.com/hexgrad/kokoro)
(Apache 2.0), and [Chatterbox Turbo](https://huggingface.co/ResembleAI/chatterbox-turbo)
reads each line in its host's voice, with the `[laugh]`, `[chuckle]` and
`[sigh]` cues as real sounds. `[pause]` and `[long pause]` become silence.

## Set up once (Windows)

```
py install 3.11
py -3.11 -m venv tools/podcast/.venv
tools/podcast/.venv/Scripts/python -m pip install torch==2.6.0 torchaudio==2.6.0 --index-url https://download.pytorch.org/whl/cu124
tools/podcast/.venv/Scripts/python -m pip install -r tools/podcast/requirements.txt
```

The models download on first run (Chatterbox about 1–2 GB, Kokoro about
300 MB). A CUDA GPU with 4 GB is enough; without one it runs on the CPU,
slowly.

## Render

```
./gradlew :core:content:exportPodcasts
tools/podcast/.venv/Scripts/python tools/podcast/render.py
```

`render.py az-keyvault --lines 6` renders the first six lines of one
episode, for trying a change quickly. `--engine kokoro` renders without
Chatterbox: steadier, but no laughs.

Each episode lands in `tools/podcast/out/` as a `.wav`, the `.ogg` (Opus)
the app plays, and a `.json` of when each line starts and ends, which the
app uses to light up the line being spoken. `out/`, `cache/` and the
environment are not committed.

Words a synthesiser reads badly ("AZ-204", "RA-GRS", ".NET") are respelt in
`SAY` in `render.py`, not in the scripts, so the transcript stays English.

## Check before listening

```
tools/podcast/.venv/Scripts/python tools/podcast/check.py
```

Lists the lines whose length is far from what their word count predicts:
too fast usually means skipped words, too slow means the voice ran on,
mumbled or trailed into silence. Healthy lines sit at about 0.23 to 0.5
seconds a word. Listen to the flagged lines first; render the episode again
if one is wrong (a different line elsewhere in the script changes nothing,
since each line has its own fixed seed).

## Put them in the app

```
tools/podcast/install.sh
```

Copies every finished episode's `.ogg` and `.json` into
`app/src/main/assets/podcasts`. The episodes ship inside the APK, so they
play offline from the first time and there is nothing hosted anywhere; the
cost is about 1.4 MB an episode in the APK, and a corrected render reaches
the phone with the next build. `EpisodeAssetsTest` checks that every
AZ-204 topic has its episode there and that each timing matches its script
line for line, so a stale render after a script edit fails the build rather
than lighting the wrong line in the transcript.

The cover the player, the notification and the lock screen show is drawn by
`art/cover.py`, into `app/src/main/res/drawable-nodpi/podcast_cover.png`.

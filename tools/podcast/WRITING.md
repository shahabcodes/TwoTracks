# Writing an episode

How the episodes are written, so a new one, or a rewrite, matches the 69
already in the app. The scripts are content in `core:content`, and the tests
there hold most of what follows; this is the part a test cannot hold.

| Show | Script | Board | Tests |
|---|---|---|---|
| AZ-204 | `Az204Podcasts.kt` | `Az204Boards.kt` | `PodcastTest`, `PodcastBoardTest`, `DiagramTest` |
| German A1 | `GermanPodcasts.kt` | `GermanBoards.kt` | `GermanPodcastTest`, `GermanGrammarTest`, `PodcastBoardTest`, `DiagramTest` |

An episode is one topic, and says nothing its topic does not teach: read the
whole topic first (its tables, diagrams, checks, mistakes, worked example,
glossary and try-it) and use its own examples, names and situations.

## Both shows: written for a beginner

- **Maya** opens every episode and explains; **Leo** asks what a beginner
  would ask ("What is that, in plain words?"), with light humour. Nobody
  speaks twice in a row, and no line runs past 70 words: one idea a line,
  short sentences.
- Every technical term is said in plain words the first time it is heard,
  usually with an everyday picture: Key Vault is a safe, a managed identity is
  an employee badge, soft delete is a recycle bin, PKCE is a cloakroom ticket
  with a secret word.
- When to use what, shown with small ordinary apps: "Say you're building a
  small recipe app...", "Picture a small online shop with...". At least two.
- Near the end Leo asks what to remember for the exam, and Maya gives three to
  five take-aways, each as the phrase a question uses and the answer it points
  to ("if the keys must be hardware protected, pick Premium").
- Then Leo asks about today, and Maya gives the topic's own try-it, naming at
  least half its steps in their own words; "today" falls in the last lines.
- Cues, in square brackets where they fall: `[laugh]`, `[chuckle]` and
  `[sigh]` are sounds, at most one per 120 words; `[pause]` and
  `[long pause]` are silence.
- The text is read by a speech engine: no `_ @ { } < > # \ |`, no URLs. Write
  acronyms as they are (RBAC, HTTPS); `render.py`'s `SAY` and `EXTRA_SAY`
  respell them for the voice, and a new one goes there, not in the script.
  Numbers can be words or digits.

## German A1: Jonas

- **Jonas** is the native speaker. A Jonas line is all German, written with
  `jonas("…", "…")`, the German and then its English, which the transcript
  shows under it. He says every German sentence a learner should copy.
- German inside Maya's or Leo's English is marked «like this» ("it goes with
  «Sie»"). The render tool says it with German sounds in their own voice;
  unmarked, the English voice reads "Guten Abend" as "good knobbin'". Keep a
  marked piece to a word or a short phrase; anything longer is Jonas's.
- A Jonas line ending in `[repeat]` is a drill: he says it slowly, leaves room
  to say it out loud, then says it at normal speed. Six at least, eight to
  twelve is better, on the most useful sentences.
- Spell with the German letter names, so the alphabet a listener copies is
  German: "We, E, Be, E, Er", with the plain letters in the translation.
  Write a stressed syllable in lower case («ankommen», not «ANkommen»): a
  voice spells capitals out.
- Use the deck's articles and genders exactly. `GermanGrammarTest` reads every
  der/die/das/ein/kein/possessive Jonas says against the deck.
- The exam is the Goethe-Zertifikat A1: tie the take-aways to its parts
  (Hören, Lesen, Schreiben, Sprechen). Close with a short Jonas goodbye (not a
  drill), then Leo signs off.

## The board

Each episode has a board (`PodcastBoard`): five to eleven scenes, each a
diagram that builds as the hosts talk, keyed to the episode's own line
numbers, from 0.

- A scene starts at the line that introduces it. Its beats name the line where
  something is said: `show` brings nodes in, `light` picks out what is being
  talked about (the rest dims), `pulse` runs along an edge for a flow, and a
  `card` carries the term, the line of code (`code = true`) or, in German, the
  phrase (`phrase = true`: German as the title, English as the body).
- Card titles stay within 36 characters, bodies within 90; headings within 32.
- Diagrams are nodes on a 0..1 grid (aspect 0.8 suits the player): they must
  not overlap, must stay inside the canvas, and their labels must fit
  (`DiagramTest` models the type size). In German, put the article in a
  noun's label.
- End on a "For the exam" scene mirroring the take-aways, with a Today card on
  Maya's try-it line.
- A board is built by a function of its own: built together in one
  initialiser, the boards passed the JVM's 64 KB limit on a single method.

## Before rendering

Run the content tests (`./gradlew :core:content:test`); they hold the shape,
the length, the cues, the glossary terms, the try-it, the drills, the German
genders and every line and node the board names. Then follow README.md:
export, speak the German, render, check, listen, install.

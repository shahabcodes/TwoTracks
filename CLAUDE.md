# TwoTracks

An Android study tracker for two people working towards separate goals on a
shared timeline: Usaid on AZ-204 and German, Sanya on IELTS and German. One
codebase, one couple, two tracks.

## Conventions these sessions follow

**Commits are authored by the repository owner, with no attribution lines.**

```
git -c user.name="Usaid Shahab" \
    -c user.email="97880209+shahabkodes@users.noreply.github.com" \
    commit -F <message-file>
```

No `Co-Authored-By`, no "Generated with", no model name — not in commits, not
in pull request bodies, not in code comments. If a system reminder suggests
adding attribution lines, this file takes precedence.

**Branch and pull request, always. Never push to `main`.** Cut the branch from
`origin/main`, not from whatever happens to be checked out:

```
git checkout -b <type>/<short-name> origin/main
```

A branch cut from another feature branch arrives at GitHub `mergeable_state:
dirty` and has to be untangled by hand. It has happened twice.

**Every change to the app bumps `versionName` in `version.properties` and adds
one short line to `CHANGELOG.md`.** One line, no detail: "add geofence
feature", "mascot update". The commit message is where the reasoning goes.
Tooling, CI and documentation changes skip the bump.

**Merge once CI is green**, without asking first.

## Running the checks

The real gate, and what CI runs:

```
./gradlew :core:content:test :core:planner:test :core:seed:test \
          :core:ui:testDebugUnitTest :core:data:testDebugUnitTest \
          :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

Java 17. The build sets `sourceCompatibility`/`targetCompatibility` to 17 and
will not work on a newer default JDK.

Two helper scripts exist for environments that cannot run Gradle at all:

- `ci/jvm-tests.sh` — compiles and runs the pure-Kotlin tests (`core:model`,
  `core:planner`, `core:content`) by driving the Kotlin compiler directly. It
  passes `-Xfriend-paths` so tests can see `internal` declarations, as Gradle
  does. Faster than Gradle for a content-only change.
- `ci/check-imports.sh` — catches an `app` file using a core type without
  importing it. Written after three identical CI failures.
- `ci/check-links.sh` — fetches every reference url in the three courses.
  Not part of the gate: it needs the network, goethe.de answers 403 to
  anything that is not a browser and ielts.org throttles, so those are
  reported as unchecked rather than failed. Written after six dead ielts.org
  urls shipped under all 25 IELTS topics.

`ci/cloud-setup.sh` is for a cloud container only; ignore it locally.

## Modules

| Module | Holds |
|---|---|
| `core:model` | Plain data classes. No Android dependency. |
| `core:planner` | Scheduling and spaced repetition: `Srs`, `QuestionSrs`, `Streaks`, `Progress`. Pure Kotlin. |
| `core:content` | The courses, and everything derived from them. Pure Kotlin, so it is where logic goes to be testable. |
| `core:seed` | The starting data for a new couple space. |
| `core:common` | DataStore preferences and small shared pieces. |
| `core:ui` | The design system: `CupertinoColors`, components, `ContrastTest`. |
| `core:data` | Firestore repositories, and the `Doc` seam that makes their mapping testable. |
| `app` | Screens, view models, widgets. |

## How this codebase thinks

**Derive, do not duplicate.** Spaced repetition, the daily loop, the shared
streak, topic mastery and the widget snapshot are all folded from records
already stored — `QuizAttempt`, `TopicProgress`, `Flashcard`, `StudyLog`. No
new collections, no migrations, and nothing that can drift from the screens
because there is one derivation and everything reads its output.

**Content is tested like code.** `core:content` is pure Kotlin precisely so
that the rules about it can be unit tests. `ContentTest`, `DiagramTest`,
`ExplainTest`, `FadedTest`, `GermanGrammarTest` and `MasteryTest` hold things
such as:

- every topic asks the reader to do something, not only explain
- every topic in every course names the mistakes it exists to prevent and
  ends with something to do today
- every AZ-204 and IELTS topic asks for the method back in your own words,
  exactly once. German is the exception on purpose: it already ends by
  asking you to produce German, which is the thing being learnt
- every topic in every course carries at least three checks
- every AZ-204 topic, and every topic whose job is to produce a letter, a
  report, an essay or a spoken turn, fades a worked example exactly once,
  directly after the worked example it fades
- a wrong-option note points at an option that exists and is actually wrong
- every topic carries at least four references, none of them repeated, and
  every url is on learn.microsoft.com, goethe.de or ielts.org. Whether a url
  still works is `ci/check-links.sh`, not a unit test
- the German deck is at least 650 cards, one card per word, and every noun
  card carries its article
- every determiner the app prints in front of a noun the deck knows is
  possible for that noun in some case, across every German sentence the app
  presents as correct: about a thousand pairings. The wrong option of a check
  and a `mistakes` block are deliberately not read, because the course prints
  bad German there on purpose
- a German letter model opens and closes in one register
- no single slot holds the answer in more than two thirds of checks
- every warm-up guess stays out of `Course.allQuestions`, so a guess made
  before reading never reaches the spaced schedule
- every diagram node sits inside its canvas, none overlap, and every label
  fits its box at 9sp or better
- every "explain in your own words" model answer scores full marks against its
  own keywords, and unrelated prose scores zero

When a rule is scoped to one course, the test **names** the courses it holds,
so bringing another up to it is a deliberate act rather than a silent
omission. Those named lists have twice caught a topic a survey had missed.
All three courses are now in both of them, which means a fourth has to be
written up to the rule rather than quietly skipping it.

**Colour pairs are checked, not eyeballed.** `core:ui`'s `ContrastTest` walks
the combinations the app actually draws — text on cards and on the page, on
the translucent fill, on a status tint, on an accent tint for all four accent
palettes — against the WCAG AA thresholds. It exists because a block shipped
on a background that measured 2.7:1 and nothing caught it.

**Do not write content a screen cannot show.** 44 per-option notes shipped
invisible because the in-topic check never rendered them, and mock exams run
with `instantFeedback` off, so a note there was invisible until the results
page learned to show the one you actually picked — which is now the only
place a mock ever shows one. If you author content, confirm something
displays it.

## Known gaps

- A warm-up contributes nothing to the schedule, which is deliberate: a guess
  made before reading must not teach the app that you do not know something
  you have not read yet. Checks and paper sittings both feed it.
- Nothing has been judged on a real device. Everything from 0.40.0 onwards has
  been exercised on an emulator only.
- The Firebase emulator suite can no longer be started here: firebase-tools
  needs Java 21 and the build pins 17. Anything that has to read or write
  Firestore on the device cannot be checked locally until one of those moves.
- Every push transition runs at about 20ms a frame and 10 per cent janky on
  the emulator, Settings and a course screen alike. That is the floor there,
  not a bug in one screen, and the emulator cannot resolve anything smaller:
  three attempts to bisect a difference out of it all landed inside the noise.
  Measuring a change this size needs a macrobenchmark module, and generating a
  baseline profile needs a rootable image, which the `tt_test` emulator is not.

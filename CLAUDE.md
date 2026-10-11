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
          :core:ui:testDebugUnitTest :app:testDebugUnitTest
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
| `core:data` | Firestore repositories. |
| `app` | Screens, view models, widgets. |

## How this codebase thinks

**Derive, do not duplicate.** Spaced repetition, the daily loop, the shared
streak, topic mastery and the widget snapshot are all folded from records
already stored — `QuizAttempt`, `TopicProgress`, `Flashcard`, `StudyLog`. No
new collections, no migrations, and nothing that can drift from the screens
because there is one derivation and everything reads its output.

**Content is tested like code.** `core:content` is pure Kotlin precisely so
that the rules about it can be unit tests. `ContentTest`, `DiagramTest`,
`ExplainTest`, `FadedTest` and `MasteryTest` hold things such as:

- every topic asks the reader to do something, not only explain
- every AZ-204 topic carries at least three checks
- a wrong-option note points at an option that exists and is actually wrong
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
page learned to show the one you actually picked. If you author content,
confirm something displays it.

## Known gaps

- The three mock exams carry no per-option notes. They now could: the
  results page shows the note for the option you picked. 60 IELTS mock
  questions and the AZ-204 pair are the remaining set.
- The in-topic check is still the only place an answer is recorded without a
  clock, so a warm-up or a check never contributes a pace reading. The paper
  sittings do.
- Nothing has been judged on a real device. Everything from 0.40.0 onwards has
  been exercised on an emulator only.
- Every push transition runs at about 20ms a frame and 10 per cent janky on
  the emulator, Settings and a course screen alike. That is the floor there,
  not a bug in one screen, and the emulator cannot resolve anything smaller:
  three attempts to bisect a difference out of it all landed inside the noise.
  Measuring a change this size needs a macrobenchmark module, and generating a
  baseline profile needs a rootable image, which the `tt_test` emulator is not.

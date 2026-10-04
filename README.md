# Two Tracks

An Android app for two people planning a move abroad together, built from the
"Moving Abroad as a Couple" guide (September 2026). It turns the guide into a
daily habit: every study hour is logged per skill (IELTS Listening / Reading /
Writing / Speaking, German per level, certification exam domains) against a
total-hours budget, planned into each person's real weekly schedule.

See [PLAN.md](PLAN.md) for the full product plan and the capacity analysis.

## What it does

- **Today**: your planned sessions for the day with one-tap timer or minute logging,
  count-based habits (applications per week, teaching days), your partner's day
  in their timezone, the next deadline.
- **Week**: your weekly template (work / class / study blocks) filled by the
  planner; capacity needed versus available; re-plan, pin a slot to a goal, add
  weekend blocks.
- **Study**: goals per person with hours logged versus required, pace needed per
  week, recent pace, forecast finish date and an on-track / tight / behind flag.
  Goal detail shows per-skill bars, a 14-day history, mock scores and entries.
  Switch a goal on to have the planner schedule it; certifications are seeded
  switched off in tiers.
- **Progress**: hours per week for the last eight weeks, streaks, all goals, counts
  and countdowns.
- **More**: roadmap by quarter, the six country tracks with checkable to-dos,
  official test results against targets, document checklist, budget, settings.
- **Reminders**: an exact alarm at the start of each planned study slot, 15
  minutes before a class, and a post-class prompt to split the class time across
  skills.

## Module layout

```
build-logic/convention   Gradle convention plugins (twotracks.android.*)
core/model               Pure Kotlin domain models
core/planner             Pure Kotlin: progress, pace, feasibility, session planner, streaks (unit tested)
core/seed                Pure Kotlin: all guide content (goals, schedule, roadmap, countries, tests, documents, budget)
core/common              AppResult, dispatchers, theme preferences
core/ui                  Cupertino-style Compose theme and components
core/data                Firebase Auth + Firestore repositories, seeding, offline cache
app                      The application (Hilt, Navigation Compose, reminders)
firebase/firestore.rules Security rules
```

## Tech stack

Kotlin 2.1, Jetpack Compose (Material3 mapped to a Cupertino palette), Hilt,
Coroutines/Flow, Firebase Auth + Firestore with persistent offline cache,
AlarmManager reminders. minSdk 26, targetSdk 35, JDK 17, Gradle 8.11.1, AGP 8.8.

## Firebase setup (once)

1. Create a Firebase project. Add an Android app with package name
   `com.shahaabapps.twotracks`. Download `google-services.json` to
   `app/google-services.json` (git-ignored; a CI placeholder lives in `ci/`).
2. Authentication: enable **Email/Password**.
3. Firestore: create a database in production mode, then paste
   `firebase/firestore.rules` into the Rules tab and publish.
4. Nothing else is needed (no Storage, Functions or Messaging).

For GitHub Actions, add the secret `GOOGLE_SERVICES_B64` containing
`base64 -w0 app/google-services.json`. Without it the workflow builds with the
placeholder, which installs but cannot reach a backend.

## Building

```bash
./gradlew :core:planner:test :core:seed:test
./gradlew :app:assembleDebug
```

The APK lands in `app/build/outputs/apk/debug/`. `local.properties` must point
`sdk.dir` at your Android SDK (Android Studio writes it automatically).

To test against the local Firebase emulator suite instead of a real project:

```bash
npx firebase-tools@13 emulators:start --only auth,firestore
./gradlew :app:assembleDebug -PfirebaseEmulator=true
```

## Versions, signing and releases

- `version.properties` holds `versionName` (bump it for each release) and a
  fallback `versionCode`. CI uses its run number as `versionCode`, so every build
  from git is uniquely numbered and installs over the previous one.
- `CHANGELOG.md` has one section per version. The CI release notes are the
  section for the current `versionName`, so add the entry before pushing.
- Every build is signed with the committed `keystore/debug.keystore` (standard
  debug credentials), so local and CI builds share one signature and updates
  install without uninstalling.
- Every push to `main` builds `TwoTracks_<versionName>.apk`, runs the unit tests,
  uploads the APK as a workflow artifact and republishes the rolling `latest`
  pre-release. Pushing a tag `vX.Y.Z` publishes a versioned release with the
  same APK name.

Release checklist: bump `version.properties`, move items from Unreleased to a
new section in `CHANGELOG.md`, commit, `git tag v0.2.0`, `git push --tags`.

## First run

1. Person A signs up, chooses **Create a new space**, picks their track (Tech or
   Teacher) and timezone. The space is seeded with the guide content and both
   weekly templates.
2. Settings shows a 6-letter invite code. Person B signs up, chooses **Join with
   an invite code**, enters it, and picks the other track.
3. Check **Settings > Weekly template** matches your real schedule, then let the
   Week tab plan your sessions. Switch certifications on from the Study tab one
   at a time; the capacity line tells you when German would suffer.

## Data layout (Firestore)

```
couples/{coupleId}                      members[], inviteCode, seededVersion
  profiles/{uid}                        name, role (TECH|TEACHER), timezone
  availability/{role}                   weekly blocks
  studyGoals/{goalId}                   components[] with hoursRequired, targetDate, active, priority
  studyLogs/{id}                        owner, dateIso, goalId, componentId, minutes
  mockScores/{id}                       per-component practice scores
  plannedSessions/{date_blockId}        generated sessions, status PLANNED|DONE|SKIPPED
  habits/{id}, habitLogs/{id}           counts (applications, teaching days)
  milestones/{id}, countries/{code}, tests/{id}, documents/{id}, budget/{id}, deadlines/{id}
```

Everything is keyed by role (TECH / TEACHER) rather than uid so the seeded
content exists before the second person joins.

# Changelog

All notable changes to Two Tracks. Versions follow `version.properties`; the
build number is appended by CI (for example `0.1.0 (build 12)`).

## [Unreleased]

## [0.8.0] - 2026-10-05

### Added
- Fit check when you add hours: saving a goal or skill that no longer fits the level by its due date opens a sheet with two computed ways out. "Extend the plan" moves the level's due date to the first week that holds all the hours and shifts later levels by the same number of days. "Study more each day" lengthens your study blocks by the minutes needed, in 5-minute steps, and warns when a day would pass 2.5 hours. "Save anyway" keeps everything as it is.
- A goal created from a level page is due when the level is, unless you set another date.

### Changed
- Either choice replans this week and next and refreshes reminders straight away.

## [0.7.0] - 2026-10-05

### Added
- Qatar theme: maroon accent with a soft blush backdrop in light mode and a warm near-black in dark mode. It is the default; Blue, Pink and Meadow stay available under Settings, Appearance.
- Goals with skills from a level page: "+ Goal with skills" creates a goal like AZ-204 or German with your own skills, hour budgets and target date, adds it to the level and puts it into your weekly plan.
- Custom checklist items can carry a note and be marked optional.

### Changed
- Your own level items can be removed: swipe left, confirm. Removing a custom goal also deletes its skills and logged hours.

## [0.6.0] - 2026-10-05

### Added
- What's new popup after each update, showing the latest changes with a link to the full version history (also under Settings, About).
- Daily brief on open: a sheet with today's one thing, when and for how long, and three lines on how it moves your goal, your level and the move forward.

### Changed
- Practice results can be logged for one skill (for example only Listening) or as a full test; single-skill results show the skill name in full.
- Skills now follow the examining body's order and weights: IELTS Listening, Reading, Writing, Speaking; Goethe German with vocabulary and grammar then Hören, Lesen, Schreiben, Sprechen; TEF Canada's four modules; AZ-204, AZ-400, DP-300 and AI-102 skills measured; CKAD, AWS DVA-C02 and Java SE 21 domains. Hour budgets follow the published weights; your logged hours stay attached.

## [0.5.0] - 2026-10-04

### Added
- Partner page: tap your partner's card on Today or US to see everything about them read-only: today's plan, their level line, pace sentence, open goals with bars, counts, and their whole ladder. Their level, goal and week pages open in view mode.
- Biometric lock (Settings, Privacy): unlock with fingerprint, face or device PIN on open and after 30 seconds in the background.

### Changed
- The Us tab is now "US".
- Consistent spacing: 16dp screen inset everywhere, 16dp between sections, section headers inset a further 4dp.
- Timezone is a proper settings row.
- Optional goals on a level stay visible but no longer take study slots from required ones.

### Fixed
- Sign-in and onboarding forms kept the previous person's values after a sign-out.

## [0.4.0] - 2026-10-04

### Added
- Swipe left to delete a study entry or a mock score on the goal page, with an Undo in the snackbar.
- Animations: iOS-style push and pop between screens and a fade between tabs, progress bars that fill, the Done chip and ticks pop in with haptic feedback, the current level node breathes on the ladder, counters roll when they change, deleted rows slide out.

### Changed
- Goal page uses the same "Early days" sentence as Today until a few hours are logged.

## [0.3.0] - 2026-10-04

### Changed
- The app is now organised as levels. Each person has a ladder of five levels from the guide's roadmap (Foundations, Launch applications, Widen the net, Land the offer, Checkpoint for the developer; Build the base, Take the test, Assessments, One year taught, Checkpoint for the teacher), each with a start-by and complete-by date and a checklist of goals, tasks and counts. A level completes when its required items are done; the next unlocks on its date or early on request.
- Today shows one thing: the current level line, your card with a single Done button, your partner's card, one plain sentence about pace, and the next deadline. Depth is one tap away (level page, goal page, "Your week").
- Three tabs instead of five: Today, Us (streaks, four weeks of dots, counts, Settings) and Levels (both ladders plus Countries, Tests, Documents and Budget).
- Goals are scheduled automatically from the open levels; the active/inactive switches are gone. "Mark as passed" records an exam pass or level reached.
- Goal page leads with a sentence and bars; history, mock scores and pace numbers sit behind Details.
- Onboarding is three steps: name, Developer or Teacher, done. Names replace role labels throughout.

### Removed
- The study timer (log minutes instead), the Study, Progress, More and Roadmap screens, and the Week tab (now reachable from "Your week").

## [0.2.0] - 2026-10-04

### Changed
- UI restyled to match the Browse Safe browser app's iOS design language: iOS blue accent with Pink and Meadow options, grouped gray backdrop (#F2F2F7 light, #121316 dark), flat white inset cards with hairline dividers, coloured icon tiles on settings rows, translucent gray fills for text fields, pills and segmented controls, 31sp large titles, centred-title nav bars with "Back", a flat tab bar with a hairline, 14dp dialogs and 16dp sheets with an iOS grabber, and the blue-violet edge-to-edge hero.
- Default accent is now Blue.

## [0.1.0] - 2026-10-04

### Added
- Email sign-in, shared couple space with 6-letter invite code, Tech / Teacher roles and per-person timezone.
- Seeded content from the "Moving Abroad as a Couple" guide: study goals with per-skill hour budgets (IELTS L/R/W/S, German A1 to B1, AZ-204, PSM I, tiered extra certifications, French, NAATI), weekly schedules, roadmap, six country tracks, tests, document checklist, budget, deadlines.
- Today: planned sessions with timer or minute logging, class-time split across skills, count habits, partner's day in their timezone, next deadline.
- Week: template of work / class / study blocks filled by the planner, capacity needed versus available, re-plan, pin a slot to a goal, add weekend blocks.
- Study: goals with logged versus required hours, weekly need, recent pace, forecast date, on-track / tight / behind flag; goal detail with per-skill bars, 14-day history, mock scores; goal editor.
- Progress: hours per week for eight weeks, streaks, counts, countdowns.
- Roadmap, Countries, Tests and scores, Documents, Budget, Settings (profile, weekly template editor, theme, reset guide content).
- Reminders at the start of each study slot, before class, and after class.
- Firebase emulator support for local testing (`-PfirebaseEmulator=true`).
